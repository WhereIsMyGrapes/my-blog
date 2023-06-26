package com.wwk.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.IOUtils;
import com.wwk.entity.BlogFile;
import com.wwk.exception.ServiceException;
import com.wwk.mapper.BlogFileMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.FolderDTO;
import com.wwk.model.vo.FileVO;
import com.wwk.model.vo.PageResult;
import com.wwk.service.BlogFileService;
import com.wwk.strategy.context.UploadStrategyContext;
import com.wwk.utils.FileUtils;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.wwk.constant.CommonConstant.FALSE;
import static com.wwk.constant.CommonConstant.TRUE;

/**
 * (BlogFile)表服务实现类
 *
 * @author makejava
 */
@Service("blogFileService")
public class BlogFileServiceImpl extends ServiceImpl<BlogFileMapper, BlogFile> implements BlogFileService {

    /**
     * 本地路径
     */
    @Value("${upload.local.path}")
    private String localPath;

    @Autowired
    private BlogFileMapper blogFileMapper;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private HttpServletResponse response;

    @Override
    public PageResult<FileVO> listFileVOList(ConditionDTO condition) {
        // 查数量
        Long count = blogFileMapper.selectCount(new LambdaQueryWrapper<BlogFile>()
                .like(BlogFile::getFilePath, condition.getFilePath()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查信息
        List<FileVO> fileVOList = blogFileMapper.selectFileVOList(PageUtils.getLimit(), PageUtils.getSize()
                , condition.getFilePath());
        return new PageResult<>(fileVOList, count);
    }

    @Override
    public void uploadFile(MultipartFile file, String path) {
        try {
            String uploadPath = "/".equals(path) ? path : path + "/";
            // 上传文件
            String url = uploadStrategyContext.executeUploadStrategy(file, uploadPath);
            // md5值
            String fileMd5 = FileUtils.getMd5(file.getInputStream());
            // 文件扩展名
            String extendName = FileUtils.getExtension(file);
            // 查重
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getExtendName, extendName)
                    .eq(BlogFile::getFileName, fileMd5));
            Assert.isNull(existFile, "文件已存在");
            // 存数据库
            BlogFile newFile = BlogFile.builder()
                    .fileUrl(url)
                    .fileName(fileMd5)
                    .filePath(path)
                    .extendName(extendName)
                    .fileSize((int) file.getSize())
                    .isDir(FALSE)
                    .build();
            baseMapper.insert(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createFolder(FolderDTO folder) {
        // 目录查重
        String fileName = folder.getFileName();
        String filePath = folder.getFilePath();
        // 判断目录是否存在
        BlogFile blogFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getId)
                .eq(BlogFile::getFilePath, folder.getFilePath())
                .eq(BlogFile::getFileName, fileName));
        Assert.isNull(blogFile, "目录已存在");
        File directory = new File(localPath + filePath + "/" + fileName);
        if (FileUtils.mkdir(directory)) {
            BlogFile newBlogFile = BlogFile.builder()
                    .fileName(fileName)
                    .filePath(filePath)
                    .isDir(TRUE)
                    .build();
            baseMapper.insert(newBlogFile);
        } else {
            throw new ServiceException("目录创建失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFile(List<Integer> fileIdList) {
        List<BlogFile> blogFiles = blogFileMapper.selectList(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getFileName, BlogFile::getFilePath, BlogFile::getExtendName, BlogFile::getIsDir)
                .in(BlogFile::getId, fileIdList));
        // 删除数据库中数据
        blogFileMapper.deleteBatchIds(fileIdList);
        // 删除实体
        blogFiles.forEach(blogFile -> {
            File file;
            String fileName = localPath + blogFile.getFilePath() + "/" + blogFile.getFileName();
            // 判断是否为目录
            if (blogFile.getIsDir().equals(TRUE)) {
                // 清除旗下所有子项
                String filePath = blogFile.getFilePath() + blogFile.getFileName();
                blogFileMapper.delete(new LambdaQueryWrapper<BlogFile>().eq(BlogFile::getFilePath, filePath));
                // 删除目录
                file = new File(fileName);
                if (file.exists()) {
                    FileUtils.deleteFile(file);
                }
            } else {
                // 不是目录就直接删
                file = new File(fileName + "." + blogFile.getExtendName());
                if (file.exists()) {
                    file.delete();
                }
            }
        });
    }

    @Override
    public void downloadFile(Integer fileId) {
        // 查询文件信息
        BlogFile blogFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getFilePath, BlogFile::getFileName,
                        BlogFile::getExtendName, BlogFile::getIsDir)
                .eq(BlogFile::getId, fileId));
        Assert.notNull(blogFile, "文件不存在");
        String filePath = localPath + blogFile.getFilePath() + "/";
        // 不是目录就直接下载
        if(blogFile.getIsDir().equals(FALSE)) {
            String fileName = blogFile.getFileName() + "." + blogFile.getExtendName();
            downloadFile(filePath, fileName);
        } else {
            // 是目录就压缩再下载
            String fileName = filePath + blogFile.getFileName();
            File src = new File(fileName);
            File dest = new File(fileName + ".zip");
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(dest));
                // 压缩文件
                toZip(src, zipOutputStream, src.getName());
                zipOutputStream.close();
                // 下载压缩包
                downloadFile(filePath, blogFile.getFileName() + ".zip");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dest.exists()) {
                    dest.delete();
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    private void downloadFile(String filePath, String fileName) {
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            fileInputStream = new FileInputStream(filePath + fileName);
            outputStream = response.getOutputStream();
            IOUtils.copyLarge(fileInputStream, outputStream);
        } catch (IOException e) {
            throw new ServiceException("文件下载失败");
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 压缩文件夹
     *
     * @param src             源文件
     * @param zipOutputStream 压缩输出流
     * @param name            文件名
     * @throws IOException IO异常
     */
    public static void toZip(File src, ZipOutputStream zipOutputStream, String name) throws IOException{
        for (File file : Objects.requireNonNull(src.listFiles())) {
            if (file.isFile()) {
                // 判断是否为文件, 是, 就 变成ZipEntry对象，放入到压缩包中
                ZipEntry zipEntry = new ZipEntry(name + "/" + file.getName());
                // 读取文件中的数据，写到压缩包
                zipOutputStream.putNextEntry(zipEntry);
                FileInputStream fileInputStream = new FileInputStream(file);
                int b;
                while ((b = fileInputStream.read()) != -1) {
                    zipOutputStream.write(b);
                }
                fileInputStream.close();
                zipOutputStream.close();
            } else {
                // 目录, 就递归压缩
                toZip(file, zipOutputStream, name + "/" + file.getName());
            }
        }
    }
}


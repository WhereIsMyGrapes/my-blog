package com.wwk.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.Album;
import com.wwk.entity.BlogFile;
import com.wwk.entity.Photo;
import com.wwk.mapper.AlbumMapper;
import com.wwk.mapper.BlogFileMapper;
import com.wwk.mapper.PhotoMapper;
import com.wwk.model.dto.AlbumDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.AlbumBackVO;
import com.wwk.model.vo.AlbumVO;
import com.wwk.model.vo.PageResult;
import com.wwk.service.AlbumService;
import com.wwk.strategy.context.UploadStrategyContext;
import com.wwk.utils.BeanCopyUtils;
import com.wwk.utils.FileUtils;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.wwk.constant.CommonConstant.FALSE;
import static com.wwk.enums.FilePathEnum.PHOTO;

/**
 * (Album)表服务实现类
 *
 * @author makejava
 */
@Service("albumService")
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private BlogFileMapper blogFileMapper;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Override
    public PageResult<AlbumBackVO> listAlbumBackVO(ConditionDTO condition) {
        // 查询相册数量
        Long count = albumMapper.selectCount(new LambdaQueryWrapper<Album>()
                .like(StringUtils.hasText(condition.getKeyword()), Album::getAlbumName, condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<AlbumBackVO> albumList = albumMapper.selectAlbumBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(albumList, count);
    }

    @Override
    public void addAlbum(AlbumDTO album) {
        // 相册是否存在
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isNull(existAlbum, album.getAlbumName() + "相册已存在");
        // 添加新相册
        Album newAlbum = BeanCopyUtils.copyBean(album, Album.class);
        baseMapper.insert(newAlbum);
    }

    @Override
    public void deleteAlbum(Integer albumId) {
        // 查询照片数量
        Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        Assert.isFalse(count > 0, "相册下存在照片");
        // 相册中没有照片则删除
        albumMapper.deleteById(albumId);
    }

    @Override
    public void updateAlbum(AlbumDTO album) {
        // 相册是否存在
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isFalse(Objects.nonNull(existAlbum) && !existAlbum.getId().equals(album.getId()),
                album.getAlbumName() + "相册已存在");
        // 修改相册
        Album newAlbum = BeanCopyUtils.copyBean(album, Album.class);
        baseMapper.updateById(newAlbum);
    }

    @Override
    public AlbumDTO editAlbum(Integer albumId) {
        return albumMapper.selectAlbumById(albumId);
    }

    @Override
    public List<AlbumVO> listAlbumVO() {
        return albumMapper.selectAlbumVOList();
    }

    @Override
    public String uploadAlbumCover(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, PHOTO.getPath());
        try {
            // 获取md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtension(file);
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getFileName, md5)
                    .eq(BlogFile::getFilePath, PHOTO.getFilePath()));
            if(Objects.isNull(existFile)) {
                // 保存文件信息
                BlogFile newFile = BlogFile.builder()
                        .fileUrl(url)
                        .fileName(md5)
                        .filePath(PHOTO.getFilePath())
                        .extendName(extName)
                        .fileSize((int) file.getSize())
                        .isDir(FALSE)
                        .build();
                blogFileMapper.insert(newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}


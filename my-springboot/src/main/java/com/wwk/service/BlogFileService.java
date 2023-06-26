package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.BlogFile;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.FolderDTO;
import com.wwk.model.vo.FileVO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (BlogFile)表服务接口
 *
 * @author makejava
 * @since 2023-05-11 17:49:53
 */
public interface BlogFileService extends IService<BlogFile> {

    /**
     * 查看文件列表
     *
     * @param condition 查询条件
     * @return {@link Result <FileVO>} 文件列表
     */
    PageResult<FileVO> listFileVOList(ConditionDTO condition);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 文件路径
     */
    void uploadFile(MultipartFile file, String path);

     /**
     * 创建文件夹
     *
     * @param folder 文件夹信息
     */
    void createFolder(FolderDTO folder);

    /**
     * 删除文件
     *
     * @param fileIdList 文件id列表
     */
    void deleteFile(List<Integer> fileIdList);

    /**
     * 下载文件
     *
     * @param fileId
     */
    void downloadFile(Integer fileId);
}


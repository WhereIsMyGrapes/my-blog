package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Photo;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.PhotoDTO;
import com.wwk.model.dto.PhotoInfoDTO;
import com.wwk.model.vo.AlbumBackVO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.PhotoBackVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 照片表服务接口
 *
 * @author makejava
 */
public interface PhotoService extends IService<Photo> {

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return 后台相册列表
     */
    PageResult<PhotoBackVO> listPhotoBackVO(ConditionDTO condition);

    /**
     * 查看照片相册信息
     *
     * @param albumId 相册id
     * @return 相册信息
     */
    AlbumBackVO getAlbumInfo(Integer albumId);

    /**
     * 添加照片
     *
     * @param photo 照片
     */
    void addPhoto(PhotoDTO photo);

    /**
     * 修改照片信息
     *
     * @param photoInfo 照片信息
     */
    void updatePhoto(PhotoInfoDTO photoInfo);


    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     */
    void deletePhoto(List<Integer> photoIdList);

    /**
     * 移动照片
     *
     * @param photo 照片
     */
    void movePhoto(PhotoDTO photo);

    /**
     * 上传照片
     *
     * @param file 文件
     * @return 照片地址
     */
    String uploadPhoto(MultipartFile file);

    /**
     * 查看照片列表
     *
     * @param condition 条件
     * @return 照片列表
     */
    Map<String, Object> listPhotoVO(ConditionDTO condition);

}


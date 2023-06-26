package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.Photo;
import com.wwk.model.vo.PhotoBackVO;
import com.wwk.model.vo.PhotoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {
    /**
     * 查询后台照片列表
     *
     * @param limit   页码
     * @param size    大小
     * @param albumId 相册id
     * @return 后台照片列表
     */
    List<PhotoBackVO> selectPhotoBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("albumId") Integer albumId);

    /**
     * 查询照片列表
     *
     * @param albumId 相册id
     * @return 后台照片列表
     */
    List<PhotoVO> selectPhotoVOList(@Param("albumId") Integer albumId);}

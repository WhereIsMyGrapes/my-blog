package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.Album;
import com.wwk.model.dto.AlbumDTO;
import com.wwk.model.vo.AlbumBackVO;
import com.wwk.model.vo.AlbumVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

    /**
     * 查询后台相册列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台相册列表
     */
    List<AlbumBackVO> selectAlbumBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);

     /**
     * 根据id查询相册信息
     *
     * @param albumId 相册id
     * @return 相册
     */
    AlbumDTO selectAlbumById(@Param("albumId") Integer albumId);

    /**
     * 查看相册列表
     *
     * @return 相册列表
     */
    List<AlbumVO> selectAlbumVOList();

    /**
     * 根据id查询照片相册信息
     *
     * @param albumId 相册id
     * @return 照片相册信息
     */
    AlbumBackVO selectAlbumInfoById(Integer albumId);
}

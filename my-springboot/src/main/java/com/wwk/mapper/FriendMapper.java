package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.Friend;
import com.wwk.model.vo.FriendBackVO;
import com.wwk.model.vo.FriendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    /**
     * 查看友链列表
     * @return
     */
    List<FriendVO> selectFriendVOList();

    /**
     * 查看友链后台列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 条件
     * @return 友链后台列表
     */
    List<FriendBackVO> selectFriendBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);

}

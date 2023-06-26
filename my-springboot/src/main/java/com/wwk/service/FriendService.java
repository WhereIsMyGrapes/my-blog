package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Friend;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.FriendBackVO;
import com.wwk.model.dto.FriendDTO;
import com.wwk.model.vo.FriendVO;
import com.wwk.model.vo.PageResult;

import java.util.List;


/**
 * (Friend)表服务接口
 *
 * @author makejava
 */
public interface FriendService extends IService<Friend> {

    /**
     * 查看友链列表
     *
     */
    List<FriendVO> listFriendVO();

    /**
     * 后台查看友链列表
     *
     * @param condition 查询条件
     * @return {@link PageResult< FriendBackVO>}
     */
    PageResult<FriendBackVO> listFriendBackVO(ConditionDTO condition);

    /**
     * 添加友链
     *
     * @param friend 友链
     */
    void addFriend(FriendDTO friend);

    /**
     * 修改友链
     *
     * @param friend
     * @return
     */
    void updateFriend(FriendDTO friend);
}


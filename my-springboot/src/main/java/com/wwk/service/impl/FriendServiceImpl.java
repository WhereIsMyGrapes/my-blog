package com.wwk.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.Friend;
import com.wwk.mapper.FriendMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.FriendBackVO;
import com.wwk.model.dto.FriendDTO;
import com.wwk.model.vo.FriendVO;
import com.wwk.model.vo.PageResult;
import com.wwk.service.FriendService;
import com.wwk.utils.BeanCopyUtils;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * (Friend)表服务实现类
 *
 * @author makejava
 */
@Service("friendService")
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Override
    public List<FriendVO> listFriendVO() {
        // 列表
        return friendMapper.selectFriendVOList();
    }

    @Override
    public PageResult<FriendBackVO> listFriendBackVO(ConditionDTO condition) {
        // 数量
        Long count = friendMapper.selectCount(new LambdaQueryWrapper<Friend>()
                .like(StringUtils.hasText(condition.getKeyword()), Friend::getName, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 列表信息
        List<FriendBackVO> friendBackVOS = friendMapper.selectFriendBackVOList(PageUtils.getLimit(), PageUtils.getSize()
                , condition.getKeyword());
        return new PageResult<>(friendBackVOS,count);
    }

    @Override
    public void addFriend(FriendDTO friend) {
        // 查重
        Friend existFriend = friendMapper.selectOne(new LambdaQueryWrapper<Friend>()
                .eq(Friend::getName, friend.getName())
                .eq(Friend::getUrl,friend.getUrl()));
        Assert.isFalse(Objects.nonNull(existFriend) && !friend.getId().equals(existFriend.getId()),
                "友链已经存在");
        // 添加
        Friend newFriend = BeanCopyUtils.copyBean(friend, Friend.class);
        baseMapper.insert(newFriend);
    }


    @Override
    public void updateFriend(FriendDTO friend) {
        Friend newFriend = BeanCopyUtils.copyBean(friend, Friend.class);
        baseMapper.updateById(newFriend);
    }
}


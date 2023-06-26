package com.wwk.strategy.context;

import com.wwk.enums.LikeTypeEnum;
import com.wwk.strategy.LikeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 点赞策略上下文
 *
 * @author WWK
 * @program: my-springboot
 */
@Service
public class LikeStrategyContext {
    @Autowired
    private Map<String, LikeStrategy> likeStrategyMap;

    /**
     * 点赞
     *
     * @param likeType 点赞类型
     * @param typeId   类型id (文章id, 评论id, 说说id 等)
     */
    public void executeLikeStrategy(LikeTypeEnum likeType, Integer typeId){
        likeStrategyMap.get(likeType.getStrategy()).like(typeId);
    }
}

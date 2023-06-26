package com.wwk.strategy.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wwk.entity.Article;
import com.wwk.mapper.ArticleMapper;
import com.wwk.service.RedisService;
import com.wwk.strategy.LikeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.wwk.constant.CommonConstant.TRUE;
import static com.wwk.constant.RedisConstant.ARTICLE_LIKE_COUNT;
import static com.wwk.constant.RedisConstant.USER_ARTICLE_LIKE;

/**
 * @author WWK
 * @program: my-springboot
 */
@Service("articleLikeStrategyImpl")
public class ArticleLikeStrategyImpl implements LikeStrategy {
    @Autowired
    private RedisService redisService;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void like(Integer articleId) {
        // 判断文章是否回收
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getIsDelete)
                .eq(Article::getId, articleId));
        Assert.isFalse(Objects.isNull(article) || article.getIsDelete().equals(TRUE), "文章不存在");
        // 用户id作为建, 文章id作为值, 记录用户点赞记录
        String userLikeArticleKey = USER_ARTICLE_LIKE + StpUtil.getLoginIdAsInt();
        // 判断是否点赞
        if (redisService.hasSetValue(userLikeArticleKey, articleId)) {
            // 取消点赞则删除用户id中的文章id
            redisService.deleteSet(userLikeArticleKey, articleId);
            // 文章点赞量-1
            redisService.decrHash(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 点赞则在用户id记录文章id
            redisService.setSet(userLikeArticleKey, articleId);
            // 文章点赞量+1
            redisService.incrHash(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }
}

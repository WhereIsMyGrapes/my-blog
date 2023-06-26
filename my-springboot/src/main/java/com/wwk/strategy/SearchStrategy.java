package com.wwk.strategy;

import com.wwk.model.vo.ArticleSearchVO;

import java.util.List;

/**
 * 文章搜索策略
 */
public interface SearchStrategy {
    /**
     * 搜索文章
     *
     * @param keyword 关键词
     * @return {@link List< ArticleSearchVO>} 文章列表
     */
    List<ArticleSearchVO> searchArticle(String keyword);
}

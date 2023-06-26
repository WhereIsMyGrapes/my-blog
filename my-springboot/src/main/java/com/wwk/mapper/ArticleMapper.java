package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.Article;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 展示首页文章(带分页)
     * @param limit
     * @param size
     * @return
     */
    List<ArticleHomeVO> selectArticleHomeList(@Param("limit")Long limit, @Param("size")Long size);

    /**
     * 根据 文章id 查询 首页文章详情
     * @param articleId
     * @return 文章详情VO
     */
    ArticleVO selectArticleHomeById(Integer articleId);

    /**
     * 查询上一篇文章
     *
     * @param articleId 文章id
     * @return 上一篇文章
     */
    ArticlePaginationVO selectLastArticle(Integer articleId);

    /**
     * 查询下一篇文章
     *
     * @param articleId 文章id
     * @return 下一篇文章
     */
    ArticlePaginationVO selectNextArticle(Integer articleId);

    /**
     * 查询推荐文章
     * @return
     */
    List<ArticleRecommendVO> selectArticleRecommend();

    /**
     * 根据id查询文章信息
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleInfoVO selectArticleInfoById(@Param("articleId") Integer articleId);

    /**
     * 查询后台文章数量
     *
     * @param condition 条件
     * @return 文章数量
     */
    Long countArticleBackVO(@Param("condition") ConditionDTO condition);

    /**
     * 查询后台文章列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 后台文章列表
     */
    List<ArticleBackVO> selectArticleBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

    /**
     * 文章搜索
     *
     * @param keyword 关键字
     * @return 文章列表
     */
    List<ArticleSearchVO> searchArticle(@Param("keyword") String keyword);

    /**
     * 查询文章统计
     *
     * @return 文章统计
     */
    List<ArticleStatisticsVO> selectArticleStatistics();

    /**
     * 根据条件查询文章
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticleConditionVO> listArticleByCondition(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

    /**
     * 查询文章归档
     *
     * @param limit 页码
     * @param size  大小
     * @return 文章归档
     */
    List<ArchiveVO> selectArchiveList(@Param("limit") Long limit, @Param("size") Long size);
}

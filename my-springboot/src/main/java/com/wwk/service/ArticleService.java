package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Article;
import com.wwk.model.dto.*;
import com.wwk.model.vo.*;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService extends IService<Article> {
    PageResult<ArticleHomeVO> listArticleHomeVO();

    /**
     * 查看文章详情
     * @param articleId 文章id
     * @return 文章VO
     */
    ArticleVO getArticleHomeById(Integer articleId);

    /**
     * 搜索文章
     * @param keyword 关键字
     * @return {@link Result < ArticleSearchVO >}
     */
    List<ArticleSearchVO> listArticlesBySearch(String keyword);

    /**
     * 查看推荐文章
     * @return ArticleRecommendVO
     */
    List<ArticleRecommendVO> listArticleRecommendVO();

    /**
     * 添加文章
     * @param article 文章
     * @return
     */
    void addArticle(ArticleDTO article);

    /**
     * 上传文章图片
     * @param file
     * @return {@link String} 文章图片地址
     */
    String saveArticleImages(MultipartFile file);

    /**
     * 修改文章
     *
     * @param article
     * @return
     */
    void updateArticle(ArticleDTO article);

    /**
     * 修改置顶状态
     * @param top
     */
    void updateArticleTop(TopDTO top);

    /**
     * 修改文章推荐状态
     *
     * @param recommend 推荐
     */
    void updateArticleRecommend(RecommendDTO recommend);

    /**
     * 查看后台文章列表
     *
     * @param condition 条件
     * @return 后台文章列表
     */
    PageResult<ArticleBackVO> listArticleBackVO(ConditionDTO condition);

    /**
     * 回显文章
     *
     * @param articleId 文章id
     * @return {@link Result<ArticleInfoVO>} 后台回显数据库中的文章
     */
    ArticleInfoVO editArticle(Integer articleId);

    /**
     * 删除文章
     *
     * @param articleIdList 待删除文章id列表
     * @return {@link Result<?>}
     */
    void deleteArticle(List<Integer> articleIdList);

    /**
     * 回收或恢复文章
     *
     * @param deleteDTO 逻辑删除
     * @return {@link Result<>}
     */
    void updateArticleDelete(DeleteDTO deleteDTO);


    /**
     * 查看文章归档
     *
     * @return 文章归档
     */
    PageResult<ArchiveVO> listArchiveVO();
}

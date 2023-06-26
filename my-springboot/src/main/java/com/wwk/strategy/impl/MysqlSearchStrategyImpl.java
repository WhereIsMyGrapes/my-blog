package com.wwk.strategy.impl;

import com.wwk.mapper.ArticleMapper;
import com.wwk.model.vo.ArticleSearchVO;
import com.wwk.strategy.SearchStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wwk.constant.ElasticConstant.POST_TAG;
import static com.wwk.constant.ElasticConstant.PRE_TAG;

/**
 * mysql策略
 *
 * @author WWK
 * @program: my-springboot
 */
@Service("mySqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements SearchStrategy {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleSearchVO> searchArticle(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>();
        }
        List<ArticleSearchVO> articleSearchVOList = articleMapper.searchArticle(keyword);
        return articleSearchVOList.stream()
                .map(article -> {
                    // 获取关键词第一次出现索引
                    String articleContent = article.getArticleContent();
                    int index = article.getArticleContent().indexOf(keyword);
                    if (index != -1) {
                        // 获取关键词前25个字, 一并高亮, 提升用户体验
                        int preIndex = index > 25 ? index - 25 : 0;
                        String preText = article.getArticleContent().substring(preIndex, index);
                        // 获取关键词到后面的字
                        int last = index + keyword.length();
                        int postLength = article.getArticleContent().length() - last;
                        int postIndex = postLength > 175 ? last + 175 : last + postLength;
                        String postText = article.getArticleContent().substring(last, postIndex);
                        // 文本内容高亮
                        articleContent = (preText + postText).replaceAll(keyword, PRE_TAG + keyword + POST_TAG);
                    }
                    // 文章标题高亮
                    String articleTitle = article.getArticleTitle().replaceAll(keyword, PRE_TAG + keyword + POST_TAG);
                    return ArticleSearchVO.builder()
                            .id(article.getId())
                            .articleTitle(articleTitle)
                            .articleContent(articleContent)
                            .build();
                })
                .collect(Collectors.toList());
    }
}

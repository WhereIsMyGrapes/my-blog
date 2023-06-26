package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Tag;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.TagDTO;
import com.wwk.model.vo.*;

import java.util.List;

/**
 * (Tag)表服务接口
 *
 * @author makejava
 */
public interface TagService extends IService<Tag> {

    /**
     * 查看文章标签选项
     * @param
     * @return {@link List< TagOptionVO>}
     */
    List<TagOptionVO> listTapOption();

    /**
     * 查看后台标签列表
     *
     * @param condition 条件
     * @return 后台标签列表
     */
    PageResult<TagBackVO> listTagBackVO(ConditionDTO condition);

    /**
     * 添加标签
     *
     * @param tag 标签信息
     */
    void addTag(TagDTO tag);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void delTag(List<Integer> tagIdList);

    /**
     * 修改标签
     *
     * @param tag 标签信息
     * @return {@link Result <>}
     */
    void updateTag(TagDTO tag);

    /**
     * 查看标签列表
     *
     * @return 标签列表
     */
    List<TagVO> listTagVO();

    /**
     * 查看标签下的文章
     * @param condition 条件
     * @return 文章列表
     */
    ArticleConditionList listArticleTag(ConditionDTO condition);

}


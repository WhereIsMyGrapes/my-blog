package com.wwk.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.wwk.entity.ArticleTag;
import com.wwk.entity.Tag;
import com.wwk.mapper.ArticleMapper;
import com.wwk.mapper.ArticleTagMapper;
import com.wwk.mapper.TagMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.TagDTO;
import com.wwk.model.vo.*;
import com.wwk.service.TagService;
import com.wwk.utils.BeanCopyUtils;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author WWK
 * @program: my-springboot
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<TagOptionVO> listTapOption() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .orderByDesc(Tag::getId));
        return BeanCopyUtils.copyBeanList(tagList, TagOptionVO.class);
    }

    @Override
    public PageResult<TagBackVO> listTagBackVO(ConditionDTO condition) {
        // 查数量
        Long count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.hasText(condition.getKeyword()), Tag::getTagName,
                        condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查符合条件的标签列表
        List<TagBackVO> tagList = tagMapper.listTagBackVO(PageUtils.getLimit(), PageUtils.getSize(),
                condition.getKeyword());
        return new PageResult<>(tagList, count);
    }

    @Override
    public void addTag(TagDTO tag) {
        // 是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tag.getTagName()));
        Assert.isNull(existTag, tag.getTagName() + "标签已存在");
        // 添加标签
        Tag newTag = Tag.builder()
                .tagName(tag.getTagName())
                .build();
        baseMapper.insert(newTag);
    }

    @Override
    public void delTag(List<Integer> tagIdList) {
        // 标签下是否有文章
        Long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        Assert.isFalse(count > 0, "删除失败，标签下存在文章");
        // 批量删除标签
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    public void updateTag(TagDTO tag) {
        // 标签是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tag.getTagName()));
        Assert.isFalse(Objects.nonNull(existTag) && !existTag.getId().equals(tag.getId())
                , tag.getTagName() + "标签重复存在");
        // 修改
        Tag newTag = Tag.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .build();
        baseMapper.updateById(newTag);
    }

    @Override
    public List<TagVO> listTagVO() {
        return tagMapper.selectTagVOList();
    }

    @Override
    public ArticleConditionList listArticleTag(ConditionDTO condition) {
        List<ArticleConditionVO> articleConditionVOList = articleMapper.listArticleByCondition(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        String name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName)
                    .eq(Tag::getId, condition.getTagId()))
                .getTagName();
        return ArticleConditionList.builder()
                .articleConditionVOList(articleConditionVOList)
                .name(name)
                .build();
    }
}

package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.Tag;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.TagBackVO;
import com.wwk.model.vo.TagOptionVO;
import com.wwk.model.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据标签名查询标签
     *
     * @param tagNameList 标签名列表
     * @return 标签
     */
    List<Tag> selectTagList(List<String> tagNameList);

    /**
     * 根据文章id查询文章标签名称
     *
     * @param articleId 文章id
     * @return 文章标签名称
     */
    List<String> selectTagNameByArticleId(@Param("articleId") Integer articleId);

    List<TagBackVO> listTagBackVO(@Param("limit") long limit, @Param("size") long size, @Param("keyword") String keyword);

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    List<TagOptionVO> selectTagOptionList();

    /**
     * 查询文章标签列表
     *
     * @return 文章标签列表
     */
    List<TagVO> selectTagVOList();

}

package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Category;
import com.wwk.model.dto.CategoryDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.*;

import java.util.List;

/**
 * (Category)表服务接口
 *
 * @author makejava
 * @since 2023-04-09 14:26:11
 */
public interface CategoryService extends IService<Category> {

    /**
     * 返回文章列表
     * @return
     */
    List<CategoryVO> listCategoryVO();

    /**
     * 返回分类标签
     * @param
     * @return {@link List< CategoryOptionVO>}
     */
    List<CategoryOptionVO> listCategoryOption();

    /**
     * 查看后台分类列表
     *
     * @param condition 查询条件
     * @return 后台分类列表
     */
    PageResult<CategoryBackVO> listCategoryBackVO(ConditionDTO condition);

    /**
     * 添加分类
     *
     * @param category 分类DTO
     * @return
     */
    void addCategory(CategoryDTO category);

    /**
     * 删除分类
     *
     * @param categoryIdList 待删除分类id列表
     * @return
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 修改分类
     *
     * @param category 分类DTO
     * @return {@link Result<?>}
     */
    void updateCategory(CategoryDTO category);

    /**
     * 查看分类下的文章
     *
     * @param condition 条件
     * @return 文章列表
     */
    ArticleConditionList listArticleCategory(ConditionDTO condition);

}


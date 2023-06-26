package com.wwk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wwk.annotation.OptLogger;
import com.wwk.annotation.VisitLogger;
import com.wwk.entity.Category;
import com.wwk.model.dto.CategoryDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.*;
import com.wwk.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wwk.constant.OptTypeConstant.*;

/**
 * 分类控制器
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查看后台分类列表
     *
     * @param condition 查询条件
     * @return 后台分类列表
     */
    @ApiOperation(value = "查看后台分类列表")
    @SaCheckPermission("blog:category:list")
    @GetMapping("/admin/category/list")
    public Result<PageResult<CategoryBackVO>> listCategoryBackVO(ConditionDTO condition){
        return Result.success(categoryService.listCategoryBackVO(condition));
    }

    /**
     * 添加分类
     *
     * @param category 分类信息
     * @return {@link Result<>}
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加分类")
    @SaCheckPermission("blog:category:add")
    @PostMapping("/admin/category/add")
    public Result<?> addCategory(@Validated @RequestBody CategoryDTO category) {
        categoryService.addCategory(category);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "添加分类")
    @SaCheckPermission("blog:category:delete")
    @DeleteMapping("/admin/category/delete")
    public Result<?> deleteCategory(@RequestBody List<Integer> categoryIdList){
        categoryService.deleteCategory(categoryIdList);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param category 分类DTO
     * @return {@link Result<?>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改分类")
    @SaCheckPermission("blog:category:update")
    @DeleteMapping("/admin/category/update")
    public Result<?> updateCategory(@Validated @RequestBody CategoryDTO category){
        categoryService.updateCategory(category);
        return Result.success();
    }

    /**
     * 查看文章分类选项
     * @param
     * @return {@link Result< List< CategoryOptionVO>>}
     */
    @ApiOperation(value = "查看文章分类选项")
    @GetMapping("/admin/category/option")
    public Result<List<CategoryOptionVO>> listCategoryOption(){
        return Result.success(categoryService.listCategoryOption());
    }



    /**
     * 查看文章分类
     * @param
     * @return {@link Result< List< CategoryVO>>}
     */
    @VisitLogger(value = "文章分类")
    @ApiOperation(value ="查看文章分类")
    @GetMapping("/category/list")
    public Result<List<CategoryVO>> listCategoryVO(){
        return Result.success(categoryService.listCategoryVO());
    }

    /**
     * 查看分类下的文章
     *
     * @param condition 查询条件
     * @return 文章列表
     */
    @VisitLogger(value = "分类文章")
    @ApiOperation(value = "查看分类下的文章")
    @GetMapping("/category/article")
    public Result<ArticleConditionList> listArticleCategory(ConditionDTO condition) {
        return Result.success(categoryService.listArticleCategory(condition));
    }

}

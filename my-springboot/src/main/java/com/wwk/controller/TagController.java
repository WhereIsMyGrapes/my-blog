package com.wwk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wwk.annotation.OptLogger;
import com.wwk.annotation.VisitLogger;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.TagDTO;
import com.wwk.model.vo.*;
import com.wwk.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wwk.constant.OptTypeConstant.*;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-05-07 15:27:35
 */
@Api(tags = "标签模块")
@RestController
public class TagController {

    @Autowired
    private TagService tagService;


    /**
     * 查看后台文章标签
     *
     * @param
     * @return {@link Result< PageResult< TagBackVO>>}
     */
    @ApiOperation(value = "查看后台文章标签")
    @GetMapping("/admin/tag/list")
    public Result<PageResult<TagBackVO>> listTagBackVO(ConditionDTO condition) {
        return Result.success(tagService.listTagBackVO(condition));
    }

    /**
     * 查看文章标签选项
     * @param
     * @return {@link Result< List< TagOptionVO>>}
     */
    @ApiOperation(value = "查看文章标签选项")
    @GetMapping("/admin/tag/option")
    public Result<List<TagOptionVO>> listTagOption() {
        return Result.success(tagService.listTapOption());
    }


    /**
     * 添加标签
     *
     * @param tag 标签信息
     * @return {@link Result<>}
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加标签")
    @SaCheckPermission("blog:tag:add")
    @PostMapping("/admin/tag/add")
    public Result<?> addTag(@Validated @RequestBody TagDTO tag){
        tagService.addTag(tag);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除标签")
    @SaCheckPermission("blog:tag:delete")
    @DeleteMapping("/admin/tag/delete")
    public Result<?> delTag(@RequestBody List<Integer> tagIdList){
        tagService.delTag(tagIdList);
        return Result.success();
    }

    /**
     * 修改标签
     *
     * @param tag 标签信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改标签")
    @SaCheckPermission("blog:tag:update")
    @PutMapping("/admin/tag/update")
    public Result<?> updateTag(@Validated @RequestBody TagDTO tag) {
        tagService.updateTag(tag);
        return Result.success();
    }

    /**
     * 查看标签列表
     *
     * @return {@link Result<TagVO>} 标签列表
     */
    @VisitLogger(value = "文章标签")
    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tag/list")
    public Result<List<TagVO>> listTagVO() {
        return Result.success(tagService.listTagVO());
    }

    /**
     * 查看标签下的文章
     *
     * @param condition 查询条件
     * @return 文章列表
     */
    @VisitLogger(value = "标签文章")
    @ApiOperation(value = "查看标签下的文章")
    @GetMapping("/tag/article")
    public Result<ArticleConditionList> listArticleTag(ConditionDTO condition) {
        return Result.success(tagService.listArticleTag(condition));
    }

}

package com.wwk.controller;



import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwk.annotation.AccessLimit;
import com.wwk.annotation.OptLogger;
import com.wwk.entity.Comment;
import com.wwk.enums.LikeTypeEnum;
import com.wwk.model.dto.CheckDTO;
import com.wwk.model.dto.CommentDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.*;
import com.wwk.service.CommentService;
import com.wwk.strategy.LikeStrategy;
import com.wwk.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

import static com.wwk.constant.OptTypeConstant.*;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2023-05-28 21:54:32
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 查看后台评论列表
     *
     * @param condition 条件
     * @return {@link Result<CommentBackVO>} 后台评论
     */
    @ApiOperation(value = "查看后台评论")
    @SaCheckPermission("news:comment:list")
    @GetMapping("/admin/comment/list")
    public Result<PageResult<CommentBackVO>> listCommentBackVO(ConditionDTO condition) {
        return Result.success(commentService.listCommentBackVO(condition));
    }

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return {@link Result<>}
     */
    @SaCheckLogin
    @ApiOperation(value = "添加评论")
    @SaCheckPermission("news:comment:add")
    @PostMapping("/comment/add")
    public Result<?> addComment(@Validated @RequestBody CommentDTO comment) {
        commentService.addComment(comment);
        return Result.success();
    }

    /**
     * 删除评论
     *
     * @param commentIdList 评论id
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除评论")
    @SaCheckPermission("news:comment:delete")
    @DeleteMapping("/admin/comment/delete")
    public Result<?> deleteComment(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.success();
    }

    /**
     * 审核评论
     *
     * @param check 审核信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "审核评论")
    @SaCheckPermission("news:comment:pass")
    @PutMapping("/admin/comment/pass")
    public Result<?> updateCommentCheck(@Validated @RequestBody CheckDTO check) {
        commentService.updateCommentCheck(check);
        return Result.success();
    }

    /**
     * 查看最新评论
     *
     * @return {@link List<RecentCommentVO>}
     */
    @ApiOperation(value = "查看最新评论")
    @GetMapping("/recent/comment")
    public Result<List<RecentCommentVO>> listRecentCommentVO() {
        return Result.success(commentService.listRecentCommentVO());
    }

    /**
     * 查看评论
     *
     * @param condition 条件
     * @return {@link Result<CommentVO>}
     */
    @ApiOperation(value = "查看评论")
    @GetMapping("/comment/list")
    public Result<PageResult<CommentVO>> listCommentVO(ConditionDTO condition) {
        return Result.success(commentService.listCommentVO(condition));
    }

    /**
     * 查看回复评论
     *
     * @param commentId 评论id
     * @return {@link Result< List< ReplyVO>>}
     */
    @ApiOperation(value = "查看回复评论")
    @GetMapping("/comment/{commentId}/reply")
    public Result<List<ReplyVO>> listReply(@PathVariable("commentId") Integer commentId){
        return Result.success(commentService.listReply(commentId));
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     * @return {@link Result<>}
     */
    @SaCheckLogin
    @ApiOperation(value = "点赞评论")
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("news:comment:like")
    @PostMapping("/comment/{commentId}/like")
    public Result<?> likeComment(@PathVariable("commentId") Integer commentId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.COMMENT, commentId);
        return Result.success();
    }

}


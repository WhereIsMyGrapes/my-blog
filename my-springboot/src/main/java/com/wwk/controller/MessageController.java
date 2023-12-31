package com.wwk.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wwk.annotation.AccessLimit;
import com.wwk.annotation.OptLogger;
import com.wwk.annotation.VisitLogger;
import com.wwk.entity.Message;
import com.wwk.model.dto.CheckDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.MessageDTO;
import com.wwk.model.vo.MessageBackVO;
import com.wwk.model.vo.MessageVO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.Result;
import com.wwk.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wwk.constant.OptTypeConstant.*;

/**
 * (Message)表控制层
 *
 * @author makejava
 * @since 2023-05-30 11:39:27
 */
@Api(tags = "留言模块")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 查看留言列表
     *
     * @return {@link MessageVO} 留言列表
     */
    @VisitLogger(value = "留言")
    @ApiOperation(value = "查看留言列表")
    @GetMapping("/message/list")
    public Result<List<MessageVO>> listMessageVO() {
        return Result.success(messageService.listMessageVO());
    }

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return {@link Result<MessageBackVO>} 留言列表
     */
    @ApiOperation(value = "查看后台留言列表")
    @SaCheckPermission("news:message:list")
    @GetMapping("/admin/message/list")
    public Result<PageResult<MessageBackVO>> listMessageBackVO(ConditionDTO condition) {
        return Result.success(messageService.listMessageBackVO(condition));
    }

    /**
     * 删除留言
     *
     * @param messageIdList 留言Id列表
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除留言")
    @SaCheckPermission("news:message:delete")
    @DeleteMapping("/admin/message/delete")
    public Result<?> delMessage(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }

    @AccessLimit(seconds = 60, maxCount = 3)
    @ApiOperation(value = "添加留言")
    @PostMapping("/message/add")
    public Result<?> addMessage(@Validated @RequestBody MessageDTO message) {
        messageService.addMessage(message);
        return Result.success();
    }

    /**
     * 审核留言
     *
     * @param check 审核信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "审核留言")
    @SaCheckPermission("news:message:pass")
    @PutMapping("/admin/message/pass")
    public Result<?> updateMessageCheck(@Validated @RequestBody CheckDTO check) {
        messageService.updateMessageCheck(check);
        return Result.success();
    }

}


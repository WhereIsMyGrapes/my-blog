package com.wwk.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wwk.model.dto.EmailDTO;
import com.wwk.model.dto.UserDTO;
import com.wwk.model.dto.UserInfoDTO;
import com.wwk.model.vo.Result;
import com.wwk.model.vo.UserInfoVO;
import com.wwk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-24 22:07:54
 */
@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {
    @Autowired
    private UserService userService;

    /**
     * 获取登录用户信息
     *
     * @return {@link UserInfoVO} 用户信息
     */
    @SaCheckLogin
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/user/getUserInfo")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }

    /**
     * 修改用户邮箱
     *
     * @param email 邮箱信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户邮箱")
    @SaCheckPermission(value = "user:email:update")
    @PutMapping("/user/email")
    public Result<?> updateUserEmail(@Validated @RequestBody EmailDTO email) {
        userService.updateUserEmail(email);
        return Result.success();
    }

    /**
     * 修改用户头像
     *
     * @param file 文件
     * @return {@link Result<String>} 头像地址
     */
    @ApiOperation(value = "修改用户头像")
    @SaCheckPermission(value = "user:avatar:update")
    @PostMapping("/user/avatar")
    public Result<String> updateUserAvatar(@RequestParam(value = "file") MultipartFile file) {
        return Result.success(userService.updateUserAvatar(file));
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户信息")
    @SaCheckPermission(value = "user:info:update")
    @PutMapping("/user/info")
    public Result<?> updateUserInfo(@Validated @RequestBody UserInfoDTO userInfo) {
        userService.updateUserInfo(userInfo);
        return Result.success();
    }

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户密码")
    @PutMapping("/user/password")
    public Result<?> updatePassword(@Validated @RequestBody UserDTO user) {
        userService.updatePassword(user);
        return Result.success();
    }

}

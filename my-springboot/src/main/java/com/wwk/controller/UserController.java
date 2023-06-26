package com.wwk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.wwk.annotation.OptLogger;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.DisableDTO;
import com.wwk.model.dto.PasswordDTO;
import com.wwk.model.dto.UserRoleDTO;
import com.wwk.model.vo.*;
import com.wwk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.wwk.constant.OptTypeConstant.KICK;
import static com.wwk.constant.OptTypeConstant.UPDATE;

import java.util.List;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-24 21:22:41
 */
@Api(tags = "用户模块")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取后台登录用户的信息
     * @param
     * @return {@link Result< UserBackInfoVo>} 登录用户信息
     */
    @GetMapping("/admin/user/getUserInfo")
    public Result<UserBackInfoVo> getUserBackInfo(){
        return Result.success(userService.getUserBackInfo());
    }

    /**
     * 获取管理系统登录用户的菜单信息
     * @param
     * @return {@link Result< List< RouterVO>>} 登录用户菜单
     */
    @ApiOperation(value = "登录用户权限菜单")
    @GetMapping("/admin/user/getUserMenu")
    public Result<List<RouterVO>> getUserMenu(){
        return Result.success(userService.getUserMenu());
    }


    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return {@link UserBackVO} 用户后台列表
     */
    @ApiOperation(value = "查看后台用户列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("/admin/user/list")
    public Result<PageResult<UserBackVO>> listUserBackVO(ConditionDTO condition) {
        return Result.success(userService.listUserBackVO(condition));
    }

    /**
     * 查看用户角色选项
     *
     * @return {@link UserRoleVO} 用户角色选项
     */
    @ApiOperation(value = "查看用户角色选项")
    @SaCheckPermission("system:user:list")
    @GetMapping("/admin/user/role")
    public Result<List<UserRoleVO>> listUserRoleDTO() {
        return Result.success(userService.listUserRoleDTO());
    }

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改用户")
    @SaCheckPermission("system:user:update")
    @PutMapping("/admin/user/update")
    public Result<?> updateUser(@Validated @RequestBody UserRoleDTO user) {
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 修改用户状态
     *
     * @param disable 禁用信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改用户状态")
    @SaCheckPermission("system:user:status")
    @PutMapping("/admin/user/changeStatus")
    public Result<?> updateUserStatus(@Validated @RequestBody DisableDTO disable) {
        userService.updateUserStatus(disable);
        return Result.success();
    }


    /**
     * 修改管理员密码
     *
     * @param password 密码
     * @return {@link Result<>}
     */
    @SaCheckRole("1")
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/password")
    public Result<?> updateAdminPassword(@Validated @RequestBody PasswordDTO password) {
        userService.updateAdminPassword(password);
        return Result.success();
    }

    /**
     * 查看在线用户
     *
     * @param condition 条件
     * @return {@link OnlineVO} 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/admin/online/list")
    public Result<PageResult<OnlineVO>> listOnlineUser(ConditionDTO condition) {
        return Result.success(userService.listOnlineUser(condition));
    }

    /**
     * 下线用户
     *
     * @param token 在线token
     * @return {@link Result<>}
     */
    @OptLogger(value = KICK)
    @ApiOperation(value = "下线用户")
    @SaCheckPermission("monitor:online:kick")
    @GetMapping("/admin/online/kick/{token}")
    public Result<?> kickOutUser(@PathVariable("token") String token) {
        userService.kickOutUser(token);
        return Result.success();
    }



}

package com.wwk.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.wwk.annotation.AccessLimit;
import com.wwk.mapper.UserMapper;
import com.wwk.model.dto.CodeDTO;
import com.wwk.model.dto.LoginDTO;
import com.wwk.model.dto.RegisterDTO;
import com.wwk.model.vo.Result;
import com.wwk.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-23 09:58:59
 */
@Api(tags = "登录模块")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 管理系统登录
     * @param login
     * @return {@link Result< String>}
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginDTO login){
        return Result.success(loginService.login(login));
    }

    /**
     * 用户退出
     */
    @SaCheckLogin
    @ApiOperation(value = "用户退出")
    @GetMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 发送邮箱验证码
     *
     * @return {@link Result<>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping("/code")
    public Result<?> sendCode(String username) {
        loginService.sendCode(username);
        return Result.success();
    }

    /**
     * 用户邮箱注册
     *
     * @param register 注册信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "用户邮箱注册")
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO register) {
        loginService.register(register);
        return Result.success();
    }

    /**
     * Gitee登录
     *
     * @param data 第三方code
     * @return {@link Result< String>} Token
     */
    @ApiOperation(value = "Gitee登录")
    @PostMapping("/oauth/gitee")
    public Result<String> giteeLogin(@RequestBody CodeDTO data) {
        return Result.success(loginService.giteeLogin(data));
    }

}

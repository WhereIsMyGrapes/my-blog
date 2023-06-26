package com.wwk.service;

import com.wwk.model.dto.CodeDTO;
import com.wwk.model.dto.LoginDTO;
import com.wwk.model.dto.RegisterDTO;

public interface LoginService {
    /**
     * 用户登录
     * @param login 登录参数
     * @return
     */
    String login(LoginDTO login);

    /**
     * 发送验证码
     *
     * @param username 用户名
     */
    void sendCode(String username);

    /**
     * 用户注册
     *
     * @param register 注册信息
     */
    void register(RegisterDTO register);

    /**
     * Gitee登录
     *
     * @param data 第三方code
     * @return Token
     */
    String giteeLogin(CodeDTO data);
}

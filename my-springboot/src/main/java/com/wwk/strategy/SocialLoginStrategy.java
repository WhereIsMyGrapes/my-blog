package com.wwk.strategy;

import com.wwk.model.dto.CodeDTO;

/**
 * 第三方登录策略
 */
public interface SocialLoginStrategy {
    /**
     * 登录
     *
     * @param code 第三方code
     * @return     Token
     */
    String login(CodeDTO code);
}

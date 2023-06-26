package com.wwk.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码加密
 *
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-23 10:56:48
 */
public class SecurityUtils {

    public static boolean checkPw(String origin, String target){
        String encryptedPassword = sha256Encrypt(target);
        return StringUtils.equals(encryptedPassword,origin);
    }

    public static String sha256Encrypt(String password){
        return SaSecureUtil.sha256(password);
    }
}

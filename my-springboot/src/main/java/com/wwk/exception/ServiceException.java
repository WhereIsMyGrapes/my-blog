package com.wwk.exception;

import lombok.Getter;

import static com.wwk.enums.StatusCodeEnum.FAIL;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-18 23:55:53
 */
@Getter
public class ServiceException extends RuntimeException{
    /**
     * 返回失败状态码
     */
    private Integer code = FAIL.getCode();

    /**
     * 返回信息
     */
    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

}

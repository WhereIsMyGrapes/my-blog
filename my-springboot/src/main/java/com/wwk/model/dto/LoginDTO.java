package com.wwk.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 登录信息
 *
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-23 10:09:08
 */
@Data
@ApiModel(description = "登录信息")
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6,message = "长度不能少于6位")
    @ApiModelProperty(value = "用户密码")
    private String password;

}

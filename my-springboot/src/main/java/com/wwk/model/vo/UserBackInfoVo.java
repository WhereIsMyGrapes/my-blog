package com.wwk.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 后台管理系统登录的用户信息
 *
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-24 22:17:12
 */
@Data
@Builder
@ApiModel(description = "后台登录用户信息")
public class UserBackInfoVo {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer id;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 用户拥有的角色列表
     */
    @ApiModelProperty(value = "用户角色")
    private List<String> roleList;
    /**
     * 不同角色所拥有的权限标识列表
     */
    @ApiModelProperty(value = "权限标识列表")
    private List<String> permissionList;



}

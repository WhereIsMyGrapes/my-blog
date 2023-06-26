package com.wwk.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加菜单DTO
 *
 * @author WWK
 * @program: my-springboot
 */
@Data
@ApiModel(description = "添加菜单DTO")
public class MenuDTO {

    @ApiModelProperty(value = "菜单id")
    private Integer id;

    @ApiModelProperty(value = "父级id")
    private Integer parentId;

    @NotBlank(message = "菜单名不能为空")
    @ApiModelProperty(value = "菜单名称")
    private String  menuName;

    /**
     * 类型（M目录 C菜单 B按钮）
     */
    @NotBlank(message = "类型不能为空")
    @ApiModelProperty(value = "类型（M目录 C菜单 B按钮）")
    private String menuType;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址")
    private String path;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 菜单组件
     */
    @ApiModelProperty(value = "菜单组件")
    private String component;

    /**
     * 是否隐藏 (0否 1是)
     */
    @ApiModelProperty(value = "是否隐藏 (0否 1是)")
    private Integer isHidden;

    /**
     * 是否禁用 (0否 1是)
     */
    @ApiModelProperty(value = "是否禁用 (0否 1是)")
    private Integer isDisable;

    /**
     * 菜单排序
     */
    @NotNull(message = "菜单排序不能为空")
    @ApiModelProperty(value = "菜单排序")
    private Integer orderNum;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String perms;
}

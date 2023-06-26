package com.wwk.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-11 20:50:00
 */
@Data
@ApiModel(description = "分类选项VO")
public class CategoryOptionVO {
    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Integer id;

    /**
     * 分类名
     */
    @ApiModelProperty(value = "分类名")
    private String categoryName;

}

package com.wwk.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-04-11 20:54:48
 */
@Data
@ApiModel(description = "标签选项VO")
public class TagOptionVO {
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private Integer id;

    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String tagName;
}


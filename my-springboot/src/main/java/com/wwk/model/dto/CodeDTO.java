package com.wwk.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Code信息
 *
 * @author WWK
 * @program: my-springboot
 */
@Data
@ApiModel
public class CodeDTO {
    /**
     * code
     */
    @NotBlank(message = "code不能为空")
    @ApiModelProperty(value = "code")
    private String code;
}

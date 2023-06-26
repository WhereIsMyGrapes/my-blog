package com.wwk.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.wwk.enums.StatusCodeEnum.SUCCESS;
import static com.wwk.enums.StatusCodeEnum.FAIL;


@ApiModel(description = "结果返回类")
@Data
public class Result<T> {

    /**
     * 返回状态
     */
    @ApiModelProperty(value = "返回状态")
    private boolean flag;

    /**
     * 返回状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    public static <T> Result<T> success() {
        return buildResult(true, null, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    public static <T> Result<T> success(T data) {
        return buildResult(true, data, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    public static <T> Result<T> fail(String msg) {
        return buildResult(false, null, FAIL.getCode(), msg);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return buildResult(false, null, code, msg);
    }

    private static <T> Result<T> buildResult(Boolean flag, T data, Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setData(data);
        r.setFlag(flag);
        r.setMsg(msg);
        return r;
    }

}

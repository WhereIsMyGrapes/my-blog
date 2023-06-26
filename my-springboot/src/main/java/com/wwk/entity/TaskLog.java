package com.wwk.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 定时任务日志表实体类
 *
 * @author makejava
 * @since 2023-05-31 00:16:14
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLog extends Model<TaskLog> {
    //任务日志id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //任务名称
    private String taskName;
    //任务组名
    private String taskGroup;
    //调用目标字符串
    private String invokeTarget;
    //日志信息
    private String taskMessage;
    //执行状态 (0失败 1正常)
    private Integer status;
    //错误信息
    private String errorInfo;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}


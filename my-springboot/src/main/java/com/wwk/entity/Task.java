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
 * (Task)表实体类
 *
 * @author makejava
 * @since 2023-05-31 00:10:43
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends Model<Task> {
    //任务id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //任务名称
    private String taskName;
    //任务组名
    private String taskGroup;
    //调用目标
    private String invokeTarget;
    //cron执行表达式
    private String cronExpression;
    //计划执行错误策略 (1立即执行 2执行一次 3放弃执行)
    private Integer misfirePolicy;
    //是否并发执行 (0否 1是)
    private Integer concurrent;
    //任务状态 (0运行 1暂停)
    private Integer status;
    //任务备注信息
    private String remark;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


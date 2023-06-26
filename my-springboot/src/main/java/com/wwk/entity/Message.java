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
 * (Message)表实体类
 *
 * @author makejava
 * @since 2023-05-30 11:39:27
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message{
    //留言id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //昵称
    private String nickname;
    //头像
    private String avatar;
    //留言内容
    private String messageContent;
    //用户ip
    private String ipAddress;
    //用户地址
    private String ipSource;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //留言时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


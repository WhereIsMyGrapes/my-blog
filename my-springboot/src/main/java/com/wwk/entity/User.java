package com.wwk.entity;

import java.time.LocalDateTime;

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
 * (User)表实体类
 *
 * @author makejava
 * @since 2023-04-18 22:41:47
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Model<User> {
    //用户id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户昵称
    private String nickname;
    //用户名
    private String username;
    //用户密码
    private String password;
    //头像
    private String avatar;
    //个人网站
    private String webSite;
    //个人简介
    private String intro;
    //邮箱
    private String email;
    //登录ip
    private String ipAddress;
    //登录地址
    private String ipSource;
    //登录方式 (1邮箱 2QQ 3Gitee 4Github)
    private Integer loginType;
    //是否禁用 (0否 1是)
    private Integer isDisable;
    //登录时间
    private LocalDateTime loginTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


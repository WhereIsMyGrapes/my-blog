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
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2023-05-28 21:54:32
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Model<Comment> {
    //评论id
    @TableId(type= IdType.AUTO)
    private Integer id;
    //类型 (1文章 2友链 3说说)
    private Integer commentType;
    //类型id (类型为友链则没有值)
    private Integer typeId;
    //父评论id
    private Integer parentId;
    //回复评论id
    private Integer replyId;
    //评论内容
    private String commentContent;
    //评论用户id
    private Integer fromUid;
    //回复用户id
    private Integer toUid;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //评论时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


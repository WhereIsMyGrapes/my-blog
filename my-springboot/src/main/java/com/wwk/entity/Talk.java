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
 * (Talk)表实体类
 *
 * @author makejava
 * @since 2023-05-30 00:08:33
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Talk extends Model<Talk> {
    //说说id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户id
    private Integer userId;
    //说说内容
    private String talkContent;
    //说说图片
    private String images;
    //是否置顶 (0否 1是)
    private Integer isTop;
    //状态 (1公开  2私密)
    private Integer status;
    //发表时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


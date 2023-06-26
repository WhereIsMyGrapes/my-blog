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
 * 相册表实体类
 *
 * @author makejava
 * @since 2023-06-07 20:37:45
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album extends Model<Album> {
    //相册id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //相册名
    private String albumName;
    //相册封面
    private String albumCover;
    //相册描述
    private String albumDesc;
    //状态 (1公开 2私密)
    private Integer status;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


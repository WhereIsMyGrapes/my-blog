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
 * (BlogFile)表实体类
 *
 * @author makejava
 * @since 2023-05-09 17:37:13
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogFile extends Model<BlogFile> {
    //文件id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //文件url
    private String fileUrl;
    //文件名
    private String fileName;
    //文件大小
    private Integer fileSize;
    //文件类型
    private String extendName;
    //文件路径
    private String filePath;
    //是否为目录 (0否 1是)
    private Integer isDir;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}


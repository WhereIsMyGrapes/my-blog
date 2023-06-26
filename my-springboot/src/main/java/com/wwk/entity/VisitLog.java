package com.wwk.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * (TVisitLog)表实体类
 *
 * @author makejava
 * @since 2023-05-17 17:44:48
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitLog extends Model<VisitLog> {
    //id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //访问页面
    private String page;
    //访问ip
    private String ipAddress;
    //访问地址
    private String ipSource;
    //操作系统
    private String os;
    //浏览器
    private String browser;
    //访问时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}


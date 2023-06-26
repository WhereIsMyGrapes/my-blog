package com.wwk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * (UserRole)表实体类
 *
 * @author makejava
 * @since 2023-06-04 13:07:14
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends Model<UserRole> {
    //主键
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户id
    private Integer userId;
    //角色id
    private String roleId;

}


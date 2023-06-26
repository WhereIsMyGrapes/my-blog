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
 * (ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-05-11 19:43:22
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag extends Model<ArticleTag> {
    //主键
    @TableId(type = IdType.AUTO)
    private Integer id;
    //文章id
    private Integer articleId;
    //标签id
    private Integer tagId;

}


package com.wwk.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.Article;
import com.wwk.entity.Category;
import com.wwk.mapper.ArticleMapper;
import com.wwk.mapper.CategoryMapper;
import com.wwk.model.dto.CategoryDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.*;
import com.wwk.service.CategoryService;
import com.wwk.utils.BeanCopyUtils;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageResult<CategoryBackVO> listCategoryBackVO(ConditionDTO condition) {
        // 查数量
        Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(condition.getKeyword()), Category::getCategoryName,
                        condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryBackVO> categoryBackVOList = categoryMapper.selectCategoryBackVO(PageUtils.getLimit(), PageUtils.getSize(),
                condition.getKeyword());
        return new PageResult<>(categoryBackVOList, count);
    }

    /**
     * 添加分类
     *
     * @param category 分类DTO
     * @return
     */
    @Override
    public void addCategory(CategoryDTO category) {
        // 是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, category.getCategoryName()));
        // 不存在就放行
        Assert.isNull(existCategory, category.getCategoryName()+"分类以及存在");
        // 新建分类
        Category newCategory = Category.builder().categoryName(category.getCategoryName()).build();
        // 插入数据表
        baseMapper.insert(newCategory);
    }

    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 查看该分类下是否有文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        // 不存在就放行isFalse, 存在就断言
        Assert.isFalse(count > 0, "删除失败, 分类存在文章");
        // 批量删除
        baseMapper.deleteBatchIds(categoryIdList);
    }

    @Override
    public void updateCategory(CategoryDTO category) {
        // 是否已存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId).eq(Category::getCategoryName, category.getCategoryName()));
        // 不存在就放行走添加路线, 存在但是id对不上就断言
        Assert.isFalse(Objects.nonNull(existCategory) && !existCategory.getId().equals(category.getId()));
        // 更新/添加
        Category newCategory = Category.builder()
                .id(category.getId()).categoryName(category.getCategoryName()).build();
        baseMapper.updateById(newCategory);
    }

    @Override
    public List<CategoryVO> listCategoryVO() {
        return categoryMapper.selectCategoryVO();
    }

    @Override
    public List<CategoryOptionVO> listCategoryOption() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getId);
        // 查询分类
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        return BeanCopyUtils.copyBeanList(categoryList, CategoryOptionVO.class);
    }

    @Override
    public ArticleConditionList listArticleCategory(ConditionDTO condition) {
        List<ArticleConditionVO> articleConditionList = articleMapper.listArticleByCondition(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        String name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getCategoryName)
                .eq(Category::getId, condition.getCategoryId()))
                .getCategoryName();
        return ArticleConditionList.builder()
                .articleConditionVOList(articleConditionList)
                .name(name)
                .build();
    }

}


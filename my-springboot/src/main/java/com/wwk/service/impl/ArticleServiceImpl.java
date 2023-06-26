package com.wwk.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.*;
import com.wwk.mapper.*;
import com.wwk.model.dto.*;
import com.wwk.model.vo.*;
import com.wwk.service.ArticleService;
import com.wwk.service.RedisService;
import com.wwk.service.TagService;
import com.wwk.strategy.context.SearchStrategyContext;
import com.wwk.strategy.context.UploadStrategyContext;
import com.wwk.utils.BeanCopyUtils;
import com.wwk.utils.FileUtils;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import static com.wwk.constant.RedisConstant.*;
import static com.wwk.enums.ArticleStatusEnum.PUBLIC;
import static com.wwk.constant.CommonConstant.FALSE;
import static com.wwk.enums.FilePathEnum.ARTICLE;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private BlogFileMapper blogFileMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SearchStrategyContext searchStrategyContext;

    /**
     * 搜索文章
     * @param keyword 关键字
     * @return
     */
    @Override
    public List<ArticleSearchVO> listArticlesBySearch(String keyword) {
        return searchStrategyContext.executeSearchStrategy(keyword);
    }

    /**
     * 返回首页文章列表
     * @return
     */
    @Override
    public PageResult<ArticleHomeVO> listArticleHomeVO() {
        // 查询文章数量/状态 (公开, 未删除)
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Article::getIsDelete,FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus());
        Long count = articleMapper.selectCount(queryWrapper);
//        System.out.println(count);
        if(count == 0){
            return new PageResult<>();
        }
        // 查询首页文章列表返回
        List<ArticleHomeVO> articleHomeVOList = articleMapper.selectArticleHomeList(PageUtils.getLimit(),PageUtils.getSize());
        return new PageResult<>(articleHomeVOList,count);
    }

    /**
     * 查看文章详情
     * @param articleId 文章id
     * @return
     */
    @Override
    public ArticleVO getArticleHomeById(Integer articleId) {
        // 查询文章信息
        ArticleVO articleVO = articleMapper.selectArticleHomeById(articleId);
        if(Objects.isNull(articleVO)){
            return null;
        }
        // 浏览量+1
        redisService.incrZet(ARTICLE_VIEW_COUNT, articleId, 1D);
        // 查上一篇
        ArticlePaginationVO lastArticle = articleMapper.selectLastArticle(articleId);
        // 查下一篇
        ArticlePaginationVO nextArticle = articleMapper.selectNextArticle(articleId);

        articleVO.setLastArticle(lastArticle);
        articleVO.setNextArticle(nextArticle);

        // 查询浏览量
        Double viewCount = Optional.ofNullable(redisService.getZsetScore(ARTICLE_VIEW_COUNT,articleId))
                .orElse((double)0);
        articleVO.setViewCount(viewCount.intValue());

        // 查询点赞量
        Integer likeCount = redisService.getHash(ARTICLE_LIKE_COUNT, articleId.toString());
        articleVO.setLikeCount(Optional.ofNullable(likeCount).orElse(0));

        return articleVO;
    }

    /**
     * 查看后台文章列表
     *
     * @param condition 条件
     * @return 后台文章列表
     */
    @Override
    public PageResult<ArticleBackVO> listArticleBackVO(ConditionDTO condition) {
        // 查询文章数量
        Long count = articleMapper.countArticleBackVO(condition);
        if(count == 0){
            return new PageResult<>();
        }
        // 查询文章后台信息
        List<ArticleBackVO> articleBackVOS = articleMapper.selectArticleBackVO(PageUtils.getLimit(),PageUtils.getSize(),condition);
        // 浏览量
        Map<Object, Double> viewCountMap = redisService.getZsetAllScore(ARTICLE_VIEW_COUNT);
        // 点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(ARTICLE_LIKE_COUNT);
        // 封装信息
        articleBackVOS.forEach(item -> {
            Double viewCount = Optional.ofNullable(viewCountMap.get(item.getId())).orElse((double) 0);
            item.setViewCount(viewCount.intValue());
            Integer likeCount = likeCountMap.get(item.getId().toString());
            item.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        });
//        Long count =(long) 1;
        return new PageResult<>(articleBackVOS, count);
    }

    /**
     * 查询推荐文章并返回
     * @return
     */
    @Override
    public List<ArticleRecommendVO> listArticleRecommendVO() {
        return articleMapper.selectArticleRecommend();
    }

    /**
     * 添加文章
     * @param article 文章
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addArticle(ArticleDTO article) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(article);
        // 添加文章
        Article newArticle = BeanCopyUtils.copyBean(article, Article.class);
        // 没图就设置默认图片
        if(StringUtils.isBlank(newArticle.getArticleCover())){
            SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
            newArticle.setArticleCover(siteConfig.getArticleCover());
        }
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.insert(newArticle);;
        // 保存文章标签
        saveArticleTag(article, newArticle.getId());
    }

    /**
     * 保存文章图片
     *
     * @param file 文件
     * @return {@link String}
     */
    @Override
    public String saveArticleImages(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, ARTICLE.getPath());
        try {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取扩展名
            String extName = FileUtils.getExtension(file);
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getFileName, md5)
                    .eq(BlogFile::getFilePath, ARTICLE.getFilePath())
            );
            if (Objects.isNull(existFile)){
                // 相同路径下, 没有重名文件, 就保存进去
                BlogFile newFile = BlogFile.builder()
                        .fileUrl(url)
                        .fileName(md5)
                        .filePath(ARTICLE.getFilePath())
                        .extendName(extName)
                        .fileSize((int) file.getSize())
                        .isDir(FALSE)
                        .build();
                blogFileMapper.insert(newFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 回显文章
     * @param articleId 文章id
     * @return
     */
    @Override
    public ArticleInfoVO editArticle(Integer articleId) {
        // 查询文章, 断言是否存在
        ArticleInfoVO articleInfoVO = articleMapper.selectArticleInfoById(articleId);
        Assert.notNull(articleInfoVO, "文章不存在");
        // 查询文章分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getCategoryName)
                .eq(Category::getId, articleInfoVO.getCategoryId())
        );
        // 查询标签列表
        List<String> tagList = tagMapper.selectTagNameByArticleId(articleId);
        // 封装返回
        articleInfoVO.setCategoryName(category.getCategoryName());
        articleInfoVO.setTagNameList(tagList);
        return articleInfoVO;
    }

    /**
     * 修改文章
     * @param articleDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(articleDTO);
        // 修改文章
        Article newArticle = BeanCopyUtils.copyBean(articleDTO, Article.class);
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.updateById(newArticle);
        // 保存文章标签列表
        saveArticleTag(articleDTO, newArticle.getId());
    }

    /**
     * 保存文章分类
     *
     * @param article 文章信息
     * @return 文章分类
     */
    private Integer saveArticleCategory(ArticleDTO article){
        // 查询分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, article.getCategoryName())
        );
        // 分类不存在, 就新建一个保存进数据库
        if(Objects.isNull(category)){
            category = Category.builder()
                    .categoryName(article.getCategoryName())
                    .build();
            // 保存分类
            categoryMapper.insert(category);
        }
        // 返回文章分类id
        return category.getId();
    }

    /**
     * 保存文章标签
     *
     * @param article       文章信息
    	 * @param articleId 文章 id
     * @return
     */
    private void saveArticleTag(ArticleDTO article, Integer articleId){
        // 删除文章标签 (先删后加)
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
        // 标签名列表
        List<String> tagNameList = article.getTagNameList();
        if(CollectionUtils.isNotEmpty(tagNameList)){
            // 查询出已经存在的标签
            List<Tag> existTagList = tagMapper.selectTagList(tagNameList);
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 移除已存在的标签列表
            tagNameList.removeAll(existTagNameList);
            // 如若还有新标签, 就先存数据库 (map 映射成 Tag 类型)
            if (CollectionUtils.isNotEmpty(tagNameList)){
                List<Tag> newTagList = tagNameList.stream()
                        .map(item -> Tag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                // 批量存数据库
                tagService.saveBatch(newTagList);
                // 获取存入数据库之后 他们的 id 值
                List<Integer> newTagIdList = newTagList.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                // 新的标签id添加到id列表中
                existTagIdList.addAll(newTagIdList);
            }
            // 数据库操作: 绑定 文章-标签 关联表
            articleTagMapper.saveBatchArticleTag(articleId, existTagIdList);
        }

    }

    /**
     * 修改置顶状态
     * @param top
     */
    @Override
    public void updateArticleTop(TopDTO top) {
        // 修改文章置顶状态
        Article newArticle = Article.builder()
                .id(top.getId())
                .isTop(top.getIsTop())
                .build();
        articleMapper.updateById(newArticle);
    }

    /**
     * 修改文章推荐状态
     *
     * @param recommend 推荐
     */
    @Override
    public void updateArticleRecommend(RecommendDTO recommend) {
        Article newArticle = Article.builder()
                .id(recommend.getId())
                .isRecommend(recommend.getIsRecommend())
                .build();
        articleMapper.updateById(newArticle);
    }

    /**
     * 删除文章
     *
     * @param articleIdList 待删除文章id列表
     * @return {@link Result<?>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(List<Integer> articleIdList) {
        // 删除标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    /**
     * 回收或恢复文章
     *
     * @param deleteDTO 逻辑删除
     * @return {@link Result<>}
     */
    @Override
    public void updateArticleDelete(DeleteDTO deleteDTO) {
        // 批量更新逻辑状态
        List<Article> articleList = deleteDTO.getIdList()
                .stream()
                .map(id -> Article.builder()
                        .id(id)
                        .isDelete(deleteDTO.getIsDelete())
                        .isTop(FALSE)
                        .isRecommend(FALSE).build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Override
    public PageResult<ArchiveVO> listArchiveVO() {
        // 查询文章数量
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        List<ArchiveVO> archiveList = articleMapper.selectArchiveList(PageUtils.getLimit(), PageUtils.getSize());
        return new PageResult<>(archiveList, count);
    }
}

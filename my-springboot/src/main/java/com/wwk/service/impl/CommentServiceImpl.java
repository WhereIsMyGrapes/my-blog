package com.wwk.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.*;
import com.wwk.mapper.ArticleMapper;
import com.wwk.mapper.CommentMapper;
import com.wwk.mapper.TalkMapper;
import com.wwk.mapper.UserMapper;
import com.wwk.model.dto.CheckDTO;
import com.wwk.model.dto.CommentDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.MailDTO;
import com.wwk.model.vo.*;
import com.wwk.service.CommentService;
import com.wwk.service.RedisService;
import com.wwk.service.SiteConfigService;
import com.wwk.utils.HTMLUtils;
import com.wwk.utils.PageUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.wwk.constant.CommonConstant.*;
//import static com.wwk.constant.MqConstant.*;
import static com.wwk.constant.MqConstant.*;
import static com.wwk.constant.RedisConstant.COMMENT_LIKE_COUNT;
import static com.wwk.enums.CommentTypeEnum.*;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Value("${blog.url}")
    private String websiteUrl;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SiteConfigService siteConfigService;

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    @Override
    public PageResult<CommentBackVO> listCommentBackVO(ConditionDTO condition) {
        // 后台评论数量
        Long count = commentMapper.countComment(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 后台评论集合
        List<CommentBackVO> commentBackVOList = commentMapper.listCommentBackVO(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        return new PageResult<>(commentBackVOList, count);
    }

    @Override
    public void updateCommentCheck(CheckDTO check) {
        // 修改评论审核状态
        List<Comment> commentList = check.getIdList().stream()
                .map(id -> Comment.builder()
                            .id(id)
                            .isCheck(check.getIsCheck())
                            .build()
                ).collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public void addComment(CommentDTO comment) {
        // 校验评论参数, 获取网站配置
        verifyComment(comment);
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        Integer commentCheck = siteConfig.getCommentCheck();
        // 过滤标签
        comment.setCommentContent(HTMLUtils.filter(comment.getCommentContent()));
        Comment newComment = Comment.builder()
                .fromUid(StpUtil.getLoginIdAsInt())
                .toUid(comment.getToUid())
                .typeId(comment.getTypeId())
                .commentType(comment.getCommentType())
                .parentId(comment.getParentId())
                .replyId(comment.getReplyId())
                .commentContent(comment.getCommentContent())
                .isCheck(commentCheck.equals(FALSE) ? TRUE : FALSE)
                .build();
        // 保存评论
        commentMapper.insert(newComment);
        // 查询评论用户昵称
        String fromNickname = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getNickname)
                    .eq(User::getId, StpUtil.getLoginIdAsInt()))
                .getNickname();
        // 通知用户
        if (siteConfig.getEmailNotice().equals(TRUE)) {
            CompletableFuture.runAsync(() -> {
                System.out.println("你好");
                notice(newComment, fromNickname);
            });
        }
    }

    @Override
    public List<RecentCommentVO> listRecentCommentVO() {
        return commentMapper.selectRecentComment();
    }

    @Override
    public PageResult<CommentVO> listCommentVO(ConditionDTO condition) {
        // 查询父评论数量
        Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(condition.getTypeId()), Comment::getTypeId, condition.getTypeId())
                .eq(Comment::getCommentType, condition.getCommentType())
                .eq(Comment::getIsCheck, TRUE)
                .isNull(Comment::getParentId));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询寻父评论
        List<CommentVO> commentVOList = commentMapper.selectParentComment(PageUtils.getLimit(), PageUtils.getSize(), condition);
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new PageResult<>();
        }
        // 评论点赞数
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        // 父评论id集合
        List<Integer> parentCommentIdList = commentVOList.stream().map(CommentVO::getId).collect(Collectors.toList());
        // 分组查询每组父评论下子评论的前三条
        List<ReplyVO> replyVOList = commentMapper.selectReplyByParentIdList(parentCommentIdList);
        // 封装子评论已有的点赞量
        replyVOList.forEach(item -> item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0)));
        // 根据父评论id生成对应子评论Map
        Map<Integer, List<ReplyVO>> replyMap = replyVOList.stream().collect(Collectors.groupingBy(ReplyVO::getParentId));
        // 父评论的回复数量
        List<ReplyCountVO> replyCountList = commentMapper.selectReplyCountByParentId(parentCommentIdList);
        // 转换Map
        Map<Integer, Integer> replyCountMap = replyCountList.stream()
                .collect(Collectors.toMap(ReplyCountVO::getCommentId, ReplyCountVO::getReplyCount));
        // 封装评论数据返回
        commentVOList.forEach(item -> {
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            item.setReplyVOList(replyMap.get(item.getId()));
            item.setReplyCount(Optional.ofNullable(replyCountMap.get(item.getId())).orElse(0));
        });
        return new PageResult<>(commentVOList, count);
    }

    @Override
    public List<ReplyVO> listReply(Integer commentId) {
        // 分页查询子评论
        List<ReplyVO> replyVOList = commentMapper.selectReplyByParentId(PageUtils.getLimit(), PageUtils.getSize(), commentId);
        // 子评论点赞Map
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        replyVOList.forEach(item -> item.setLikeCount(likeCountMap.get(item.getId().toString())));
        return replyVOList;
    }

    /**
     * 校验评论
     *
     * @param comment 评论DTO
     */
    private void verifyComment(CommentDTO comment) {
        // 根据类型, 查看对印表下是否存在id
        if(comment.getCommentType().equals(ARTICLE.getType())) {
            Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId).eq(Article::getId, comment.getTypeId()));
            Assert.notNull(article, "文章不存在");
        }
        if (comment.getCommentType().equals(TALK.getType())) {
            Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getId).eq(Talk::getId, comment.getTypeId()));
            Assert.notNull(talk, "说说不存在");
        }
        // 评论为子评论，判断回复的评论和用户是否存在
        Optional.ofNullable(comment.getParentId()).ifPresent(parentId -> {
            // 判断父评论是否存在
            Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getCommentType).eq(Comment::getId, parentId));
            Assert.notNull(parentComment, "父评论不存在");
            Assert.isNull(parentComment.getParentId(), "当前评论为字评论, 不能作为父评论");
            Assert.isTrue(comment.getCommentType().equals(parentComment.getCommentType()), "只能以同类型的评论作为父评论");
            // 判断回复的评论和用户是否存在
            Comment replyComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getFromUid, Comment::getCommentType).eq(Comment::getId, comment.getReplyId()));
            User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getId).eq(User::getId, comment.getToUid()));
            Assert.notNull(replyComment, "回复的评论不存在");
            Assert.notNull(toUser, "回复的用户不存在");
            Assert.isTrue(comment.getCommentType().equals(replyComment.getCommentType()), "只能回复同类型的下的评论");
            if (Objects.nonNull(replyComment.getParentId())) {
                Assert.isTrue(replyComment.getParentId().equals(parentId), "提交的评论parentId与当前回复评论parentId不一致");
            }
            Assert.isTrue(replyComment.getFromUid().equals(comment.getToUid()), "提交的评论toUid与当前回复评论fromUid不一致");
            // 只能回复当前父评论及其子评论
            List<Integer> replyIdList = commentMapper.selectCommentIdByParentId(parentId);
            replyIdList.add(parentId);
            Assert.isTrue(replyIdList.contains(parentId), "当前父评论下不存在该子评论");

        });
    }

    /**
     * 评论通知
     *
     * @param comment      评论
     * @param fromNickname 评论用户昵称
     */
    private void notice(Comment comment, String fromNickname) {
        // 自己回复自己不用提醒
        if (comment.getFromUid().equals(comment.getToUid())){
            return ;
        }
        // 邮件标题
        String title = "友链";
        // 被回复用户id (评论接收者)
        Integer toUid = BLOGGER_ID;
        // 父评论
        if (Objects.isNull(comment.getParentId())) {
            if (comment.getCommentType().equals(ARTICLE.getType())) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .select(Article::getArticleTitle, Article::getUserId)
                        .eq(Article::getId, comment.getTypeId()));
                title = article.getArticleTitle();
                toUid = article.getUserId();
            }
            if (comment.getCommentType().equals(TALK.getType())) {
                title = "说说";
                toUid = talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                            .select(Talk::getUserId)
                            .eq(Talk::getId, comment.getTypeId()))
                        .getUserId();
            }
            // 自己评论自己, 不提醒
            if(comment.getFromUid().equals(toUid)) {
                return;
            }
        } else {
            // 子评论
            toUid = comment.getToUid();
            if (comment.getCommentType().equals(ARTICLE.getType())) {
                title = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .select(Article::getArticleTitle)
                        .eq(Article::getId, comment.getTypeId()))
                        .getArticleTitle();
            }
            if (comment.getCommentType().equals(TALK.getType())) {
                title = "说说";
            }
        }
        // 查询被回复用户的邮箱 昵称 id
        User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail, User::getNickname)
                .eq(User::getId, toUid));
        // 给他的邮箱发个提醒过去
        if (StringUtils.hasText(toUser.getEmail())) {
            sendEmail(comment, toUser, title, fromNickname);
        }
    }

    /**
     * 发送邮件
     *
     * @param comment      评论
     * @param toUser       回复用户的个人信息
     * @param title        标题
     * @param fromNickname 评论用户昵称
     */
    private void sendEmail(Comment comment, User toUser, String title, String fromNickname) {
        MailDTO mailDTO = new MailDTO();
        if(comment.getIsCheck().equals(TRUE)) {
            Map<String, Object> contentMap = new HashMap<>(7);
            // 评论链接
            String typeId = Optional.ofNullable(comment.getTypeId())
                    .map(Object::toString)
                    .orElse("");
            String url = websiteUrl + getCommentPath(comment.getCommentType()) + typeId;
            // 父评论就提醒文章作者
            if (Objects.isNull(comment.getParentId())) {
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(COMMENT_REMIND);
                mailDTO.setTemplate(AUTHOR_TEMPLATE);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                contentMap.put("url", url);
                contentMap.put("title", title);
                contentMap.put("nickname", fromNickname);
                contentMap.put("content", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            } else {
                // 子评论则回复的是用户提醒该用户
                Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>()
                        .select(Comment::getCommentContent, Comment::getCreateTime)
                        .eq(Comment::getId, comment.getReplyId()));
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(COMMENT_REMIND);
                mailDTO.setTemplate(USER_TEMPLATE);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                contentMap.put("url", url);
                contentMap.put("title", title);
                // 被回复用户昵称
                contentMap.put("toUser", toUser.getNickname());
                // 评论用户昵称
                contentMap.put("fromUser", fromNickname);
                // 被回复的评论内容
                contentMap.put("parentComment", parentComment.getCommentContent());
                // 回复评论内容
                contentMap.put("replyComment", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            }
            // 发送HTML邮件
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_HTML_KEY, mailDTO);
        } else {
            // 审核提醒
            String adminEmail = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getEmail)
                    .eq(User::getId, BLOGGER_ID)).getEmail();
            mailDTO.setToEmail(adminEmail);
            mailDTO.setSubject(CHECK_REMIND);
            mailDTO.setContent("您收到一条新的回复，请前往后台管理页面审核");
            // 发送普通邮件
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_SIMPLE_KEY, mailDTO);
        }
    }

}


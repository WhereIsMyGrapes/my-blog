package com.wwk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.annotation.VisitLogger;
import com.wwk.entity.Message;
import com.wwk.entity.SiteConfig;
import com.wwk.model.dto.CheckDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.MessageDTO;
import com.wwk.model.vo.MessageBackVO;
import com.wwk.model.vo.MessageVO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.Result;
import com.wwk.service.MessageService;
import com.wwk.mapper.MessageMapper;
import com.wwk.service.SiteConfigService;
import com.wwk.utils.BeanCopyUtils;
import com.wwk.utils.HTMLUtils;
import com.wwk.utils.IpUtils;
import com.wwk.utils.PageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wwk.constant.CommonConstant.FALSE;
import static com.wwk.constant.CommonConstant.TRUE;

/**
 * (Message)表服务实现类
 *
 * @author makejava
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SiteConfigService siteConfigService;

    @Override
    public List<MessageVO> listMessageVO() {
        List<Message> ms = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent)
                .eq(Message::getIsCheck, TRUE));
        return BeanCopyUtils.copyBeanList(ms, MessageVO.class);
    }


    @Override
    public PageResult<MessageBackVO> listMessageBackVO(ConditionDTO condition) {
        // 查数量 like(如果没参数返回所有)
        Long count = messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                .like(StringUtils.hasText(condition.getKeyword()), Message::getMessageContent, condition.getKeyword())
                .eq(Objects.nonNull(condition.getIsCheck()), Message::getIsCheck, condition.getIsCheck())
        );
        if (count == 0){
            return new PageResult<>();
        }

        // 查管理系统留言列表
        List<MessageBackVO> messageBackVOS = messageMapper.selectMessageBackVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(messageBackVOS, count);
    }

    @Override
    public void addMessage(MessageDTO message) {
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        Integer messageCheck = siteConfig.getMessageCheck();
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        Message newMessage = BeanCopyUtils.copyBean(message, Message.class);
        newMessage.setMessageContent(HTMLUtils.filter(message.getMessageContent()));
        newMessage.setIpAddress(ipAddress);
        newMessage.setIsCheck(messageCheck.equals(FALSE) ? TRUE : FALSE);
        newMessage.setIpSource(ipSource);
        messageMapper.insert(newMessage);
    }

    @Override
    public void updateMessageCheck(CheckDTO check) {
        List<Message> messagesList = check.getIdList()
                .stream()
                .map(id -> Message.builder()
                        .id(id)
                        .isCheck(check.getIsCheck())
                        .build()
                ).collect(Collectors.toList());
        this.updateBatchById(messagesList);
    }
}


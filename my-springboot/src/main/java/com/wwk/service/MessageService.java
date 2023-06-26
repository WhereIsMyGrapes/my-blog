package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.Message;
import com.wwk.model.dto.CheckDTO;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.dto.MessageDTO;
import com.wwk.model.vo.MessageBackVO;
import com.wwk.model.vo.MessageVO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.Result;

import java.util.List;

/**
 * (Message)表服务接口
 *
 * @author makejava
 * @since 2023-05-30 11:39:27
 */
public interface MessageService extends IService<Message> {

    /**
     * 查看留言列表
     *1
     * @return {@link MessageVO} 留言列表
     */
    List<MessageVO> listMessageVO();

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return {@link Result <MessageBackVO>} 留言列表
     */
    PageResult<MessageBackVO> listMessageBackVO(ConditionDTO condition);

    /**
     * 添加留言
     *
     * @param message 留言
     */
    void addMessage(MessageDTO message);

    /**
     * 审核留言
     *
     * @param check 审核信息
     */
    void updateMessageCheck(CheckDTO check);
}


package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.Message;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.MessageBackVO;
import com.wwk.service.MessageService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 查询后台留言列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 后台留言列表
     */
    List<MessageBackVO> selectMessageBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

}

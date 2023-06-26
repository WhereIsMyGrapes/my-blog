package com.wwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwk.entity.OperationLog;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.OperationLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    /**
     * 查询操作日志
     *
     * @param limit     页码
     * @param size      大小
     * @param conditionDTO 条件
     * @return 操作日志列表
     */
    List<OperationLogVO> selectOperationLogVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("conditionDTO") ConditionDTO conditionDTO);
}

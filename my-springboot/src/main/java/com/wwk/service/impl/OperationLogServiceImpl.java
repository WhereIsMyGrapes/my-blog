package com.wwk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.OperationLog;
import com.wwk.mapper.OperationLogMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.OperationLogVO;
import com.wwk.model.vo.PageResult;
import com.wwk.service.OperationLogService;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * (OperationLog)表服务实现类
 *
 * @author makejava
 */
@Service("operationLogService")
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void saveOperationLog(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }

    @Override
    public PageResult<OperationLogVO> listOperationLogVO(ConditionDTO conditionDTO) {
        // 查询操作日志数量
        Long count = operationLogMapper.selectCount(new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.hasText(conditionDTO.getOptModule()), OperationLog::getModule, conditionDTO.getOptModule())
                .or()
                .like(StringUtils.hasText(conditionDTO.getKeyword()), OperationLog::getDescription, conditionDTO.getKeyword())
        );
        if(count == 0){
            return new PageResult<>();
        }
        // 查询日志列表
        List<OperationLogVO> operationLogVOList = operationLogMapper.selectOperationLogVOList(
                PageUtils.getLimit(),
                PageUtils.getSize(),
                conditionDTO
        );
        return new PageResult<>(operationLogVOList, count);
    }
}


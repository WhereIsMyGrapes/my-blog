package com.wwk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.ExceptionLog;
import com.wwk.mapper.ExceptionLogMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.PageResult;
import com.wwk.service.ExceptionLogService;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * (TExceptionLog)表服务实现类
 *
 * @author makejava
 */
@Service("tExceptionLogService")
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {
    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    @Override
    public PageResult<ExceptionLog> listExceptionLog(ConditionDTO condition) {
        // 查数量
        Long count = exceptionLogMapper.selectCount(new LambdaQueryWrapper<ExceptionLog>()
                .like(StringUtils.hasText(condition.getOptModule()), ExceptionLog::getModule, condition.getOptModule())
                .or()
                .like(StringUtils.hasText(condition.getKeyword()), ExceptionLog::getDescription, condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查异常日志列表
        List<ExceptionLog> operationLogVOS = exceptionLogMapper.selectExceptionLogList(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        return new PageResult<>(operationLogVOS, count);
    }

    @Override
    public void saveExceptionLog(ExceptionLog exceptionLog) {
        // 保存异常日志
        exceptionLogMapper.insert(exceptionLog);
    }
}


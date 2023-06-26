package com.wwk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.VisitLog;
import com.wwk.mapper.VisitLogMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.PageResult;
import com.wwk.service.VisitLogService;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * (TVisitLog)表服务实现类
 *
 * @author makejava
 */
@Service("tVisitLogService")
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements VisitLogService {

    @Autowired
    private VisitLogMapper visitLogMapper;

    @Override
    public PageResult<VisitLog> listVisitLog(ConditionDTO condition) {
        // 查数量
        Long count = visitLogMapper.selectCount(new LambdaQueryWrapper<VisitLog>()
                .like(StringUtils.hasText(condition.getKeyword()), VisitLog::getPage, condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查访问页面日志列表
        List<VisitLog> visitLogVOList = visitLogMapper.selectVisitLogList(PageUtils.getLimit(),
                PageUtils.getSize(), condition.getKeyword());
//        System.out.println(PageUtils.getLimit());
        return new PageResult<>(visitLogVOList, count);
    }

    @Override
    public void saveVisitLog(VisitLog visitLog) {
        // 保存访问日志
        visitLogMapper.insert(visitLog);
    }
}


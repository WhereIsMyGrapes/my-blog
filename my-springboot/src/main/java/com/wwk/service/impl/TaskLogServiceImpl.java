package com.wwk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwk.entity.TaskLog;
import com.wwk.mapper.TaskLogMapper;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.TaskBackVO;
import com.wwk.model.vo.TaskLogVO;
import com.wwk.service.TaskLogService;
import com.wwk.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务日志业务实现类
 *
 * @author makejava
 */
@Service("taskLogService")
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements TaskLogService {

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Override
    public PageResult<TaskLogVO> listTaskLog(ConditionDTO condition) {
        // 查询定时任务日志数量
        Long count = taskLogMapper.selectTaskLogCount(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询定时任务日志列表
        List<TaskLogVO> taskLogVOList = taskLogMapper.selectTaskLogVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(taskLogVOList, count);
    }

    @Override
    public void clearTaskLog() {
        taskLogMapper.delete(null);
    }
}


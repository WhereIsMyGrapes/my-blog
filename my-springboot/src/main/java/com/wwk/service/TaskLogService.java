package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.TaskLog;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.PageResult;
import com.wwk.model.vo.TaskBackVO;
import com.wwk.model.vo.TaskLogVO;

/**
 * 定时任务日志业务接口
 *
 * @author makejava
 */
public interface TaskLogService extends IService<TaskLog> {

    /**
     * 查看后台定时任务日志
     *
     * @param condition 条件
     * @return 后台定时任务日志
     */
    PageResult<TaskLogVO> listTaskLog(ConditionDTO condition);

    /**
     * 清空定时任务日志
     */
    void clearTaskLog();
}


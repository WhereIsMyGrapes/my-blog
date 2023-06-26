package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.VisitLog;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.PageResult;

/**
 * (TVisitLog)表服务接口
 *
 * @author makejava
 * @since 2023-05-17 17:44:48
 */
public interface VisitLogService extends IService<VisitLog> {

    /**
     * 保存访问日志
     *
     * @param visitLog 访问日志信息
     */
    void saveVisitLog(VisitLog visitLog);

    /**
     * 查看访问日志列表
     *
     * @param condition 条件
     * @return 日志列表
     */
    PageResult<VisitLog> listVisitLog(ConditionDTO condition);
}


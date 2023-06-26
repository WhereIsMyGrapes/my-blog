package com.wwk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwk.entity.OperationLog;
import com.wwk.model.dto.ConditionDTO;
import com.wwk.model.vo.OperationLogVO;
import com.wwk.model.vo.PageResult;

/**
 * (OperationLog)表服务接口
 *
 * @author makejava
 * @since 2023-05-11 12:14:27
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 保存操作日志
     * @param operationLog
     * @return
     */
    void saveOperationLog(OperationLog operationLog);

    /**
     * 前端分页查看操作日志列表
     * @param conditionDTO 条件
     * @return {@link PageResult< OperationLogVO>} 分页日志列表
     */
    PageResult<OperationLogVO> listOperationLogVO(ConditionDTO conditionDTO);

}


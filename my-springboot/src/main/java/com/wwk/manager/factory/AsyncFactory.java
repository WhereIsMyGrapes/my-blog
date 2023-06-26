package com.wwk.manager.factory;

import com.wwk.entity.ExceptionLog;
import com.wwk.entity.OperationLog;
import com.wwk.entity.VisitLog;
import com.wwk.service.ExceptionLogService;
import com.wwk.service.OperationLogService;
import com.wwk.service.VisitLogService;
import com.wwk.utils.SpringUtils;

import java.util.TimerTask;

/**
 * 异步工厂 (产生任务)
 *
 * @author WWK
 * @program: my-springboot
 */
public class AsyncFactory {
    /**
     * 定期记录操作日志
     *
     * @param operationLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOperation(OperationLog operationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(OperationLogService.class).saveOperationLog(operationLog);
            }
        };
    }


    /**
     * 记录异常日志
     *
     * @param exceptionLog 异常日志信息
     * @return 任务task
     */
    public static TimerTask recordException(ExceptionLog exceptionLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(ExceptionLogService.class).saveExceptionLog(exceptionLog);
            }
        };
    }

    /**
     * 记录访问日志
     *
     * @param visitLog 访问日志信息
     * @return 任务task
     */
    public static TimerTask recordVisit(VisitLog visitLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(VisitLogService.class).saveVisitLog(visitLog);
            }
        };
    }
}

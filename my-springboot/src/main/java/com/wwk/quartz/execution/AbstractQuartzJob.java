package com.wwk.quartz.execution;

import com.wwk.constant.ScheduleConstant;
import com.wwk.entity.ExceptionLog;
import com.wwk.entity.Task;
import com.wwk.entity.TaskLog;
import com.wwk.mapper.TaskLogMapper;
import com.wwk.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;

import static com.wwk.constant.CommonConstant.FALSE;
import static com.wwk.constant.CommonConstant.TRUE;

/**
 * 抽象quartz调用
 *
 * @author WWK
 * @program: my-springboot
 */
@SuppressWarnings(value = "all")
public abstract class AbstractQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();



    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Task task = new Task();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstant.TASK_PROPERTIES), task);
        try {
            before(context, task);
            doExecute(context, task);
            after(context, task, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param task    系统计划任务
     */
    protected void before(JobExecutionContext context, Task task) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param task    系统计划任务
     */
    protected void after(JobExecutionContext context, Task task, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();
        final TaskLog taskLog = new TaskLog();
        taskLog.setTaskName(task.getTaskName());
        taskLog.setTaskGroup(task.getTaskGroup());
        taskLog.setInvokeTarget(task.getInvokeTarget());
        long runTime = new Date().getTime() - startTime.getTime();
        taskLog.setTaskMessage(taskLog.getTaskName() + " 总共耗时：" + runTime + "毫秒");
        if (e != null) {
            taskLog.setStatus(FALSE);
            String errorMsg = StringUtils.substring(e.getMessage(), 0, 2000);
            taskLog.setErrorInfo(errorMsg);
        } else {
            taskLog.setStatus(TRUE);
        }
        // 写入日志数据库
        SpringUtils.getBean(TaskLogMapper.class).insert(taskLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param task    系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, Task task) throws  Exception;

}

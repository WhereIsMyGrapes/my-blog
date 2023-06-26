package com.wwk.quartz.execution;

import com.wwk.entity.Task;
import com.wwk.quartz.utils.TaskInvokeUtils;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author WWK
 * @program: my-springboot
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Task task) throws Exception {
        TaskInvokeUtils.invokeMethod(task);
    }
}

package com.wwk.quartz.execution;

import com.wwk.entity.Task;
import com.wwk.quartz.utils.TaskInvokeUtils;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author WWK
 * @program: my-springboot
 */
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Task task) throws Exception {
        TaskInvokeUtils.invokeMethod(task);
    }
}

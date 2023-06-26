package com.wwk.manager;

import ch.qos.logback.core.util.TimeUtil;
import com.wwk.utils.SpringUtils;
import com.wwk.utils.ThreadUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步管理器
 *
 * @author WWK
 * @program: my-springboot
 */
public class AsyncManager {

    /**
     * 单例模式, 保证只有一个实例, 避免重复
     */
    private AsyncManager(){}

    /**
     * 饿汉模式, 类加载立刻实例化
     */
    private static final AsyncManager INSTANCE = new AsyncManager();

    public static AsyncManager getInstance() {
        return INSTANCE;
    }

    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 延迟执行任务
     */
    public void execute(TimerTask task){
        executor.schedule(task,10, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }



}

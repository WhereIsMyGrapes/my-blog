package com.wwk.manager.factory;

import com.wwk.manager.AsyncManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 确保应用退出时关闭后台线程
 *
 * @author WWK
 * @program: my-springboot
 */
@Component
public class ShutdownManager {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownManager.class);

    @PreDestroy
    public void destory(){
        shutdownAsyncManager();
    }

    /**
     * 停止异步任务
     */
    private void shutdownAsyncManager(){
        try {
            logger.info("====关闭后台任务任务线程池====");
            AsyncManager.getInstance().shutdown();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}

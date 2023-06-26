package com.wwk.quartz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.wwk.mapper.VisitLogMapper;
import com.wwk.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.wwk.constant.RedisConstant.UNIQUE_VISITOR;


/**
 * 执行定时任务
 *
 * @author WWK
 * @program: my-springboot
 */
@SuppressWarnings(value = "all")
@Component("timedTask")
public class TimedTask {
    @Autowired
    private RedisService redisService;

    @Autowired
    private VisitLogMapper visitLogMapper;

    /**
     * 清除博客访问记录
     */
    public void clear() {
        redisService.deleteObject(UNIQUE_VISITOR);
    }

    /**
     * 测试任务
     */
    public void test() {
        System.out.println("测试任务");
    }

    /**
     * 测试添加
     */
    public void addTest() {
        System.out.println("成功添加");
    }

    /**
     * 清除一周前的访问日志
     */
    public void clearVistiLog() {
        DateTime endTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        visitLogMapper.deleteVisitLog(endTime);
    }
}

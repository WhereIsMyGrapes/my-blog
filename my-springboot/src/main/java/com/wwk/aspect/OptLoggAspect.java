package com.wwk.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wwk.annotation.OptLogger;
import com.wwk.entity.OperationLog;
import com.wwk.entity.User;
import com.wwk.manager.factory.AsyncFactory;
import com.wwk.manager.AsyncManager;
import com.wwk.mapper.UserMapper;
import com.wwk.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author WWK
 * @program: my-springboot
 * @date 2023-05-07 17:24:53
 */
@Aspect
@Component
public class OptLoggAspect {
    /**
     * 请求开始的时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private UserMapper userMapper;

    /**
     * 设置操作日志切入点，记录操作日志，在注解的位置切入代码
     */
    @Pointcut(value = "@annotation(com.wwk.annotation.OptLogger)")
    public void optLogPointCut(){
    }
    
    /** 
     * 前置通知 
     */
    @Before("optLogPointCut()")
    public void doBefore(){
        // 记录请求开始时间
        startTime.set(System.currentTimeMillis());
    }
    
    @AfterReturning(value = "optLogPointCut()", returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result){
        // 获取签名 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在方法
        Method method = signature.getMethod();
        // 根据swagger的注解获取操作
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        // 使用 optLogger 中的信息记录日志
        OptLogger optLogger = method.getAnnotation(OptLogger.class);
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        // 创建实例, 保存到数据库
        OperationLog operationLog = new OperationLog();
        // 存操作模块
        operationLog.setModule(api.tags()[0]);
        // 存操作类型
        operationLog.setType(optLogger.value());
        // 获取请求URI (/admin/operation/*)
        operationLog.setUri(request.getRequestURI());
        // 获取请求的类名(包名)
        String className = joinPoint.getTarget().getClass().getName();
        // 获取方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 存请求方法包名
        operationLog.setName(methodName);
        // 存操作描述
        operationLog.setDescription(apiOperation.value());
        // 请求参数 param
        if (joinPoint.getArgs()[0] instanceof MultipartFile) {
            operationLog.setParams(((MultipartFile) joinPoint.getArgs()[0]).getOriginalFilename());
        } else {
            operationLog.setParams(JSON.toJSONString(joinPoint.getArgs()));
        }
        // 请求方式( post, get)
        operationLog.setMethod(Objects.requireNonNull(request).getMethod());
        // 返回前端的参数 {code: 200, msg: "ok", ...}
        operationLog.setData(JSON.toJSONString(result));
        // 请求用户的ID
        operationLog.setUserId(StpUtil.getLoginIdAsInt());
        // 请求用户的昵称
        int userId = StpUtil.getLoginIdAsInt();
        User nickNameTemp = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname)
                .eq(User::getId, userId));
        String nickName = nickNameTemp.getNickname();
        operationLog.setNickname(nickName);
        // 操作的ip和操作地址
        String ip = IpUtils.getIpAddress(request);
        operationLog.setIpAddress(ip);
        operationLog.setIpSource(IpUtils.getIpSource(ip));
        // 执行耗时
        Number timeConsume = (Number) (System.currentTimeMillis() - startTime.get());
        Integer timeExpend = ((Number) timeConsume).intValue();
        operationLog.setTimes(timeExpend);
        startTime.remove();
        // 保存到数据库
        AsyncManager.getInstance().execute(AsyncFactory.recordOperation(operationLog));
    }
    
}

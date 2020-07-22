package com.leateck.gmp.backup.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: WebLogAspect</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-21   luyangqian  Created
 * </pre>
 */
@Component
@Aspect
@Slf4j
public class WebLogAspect {

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("execution(public * com.leateck.gmp.backup.core..*Controller.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBeforeExecute(JoinPoint joinPoint) {
        //获取uri
        String uri = request.getRequestURI();
        //获取请求方法
        String method = request.getMethod();
        //获取类和方法名
        String classMethodName = joinPoint.getSignature().getDeclaringType().getSimpleName() + "." +
                joinPoint.getSignature().getName();
        //获取请求参数
        String args = getArgs(joinPoint);

        log.info("WebLogAspect: uri: {}, method: {}", uri, method);
        log.info("WebLogAspect: classMethod: {}, args: {}", classMethodName, args);
    }

    /**
     * 获取切点的请求参数
     * @param joinPoint
     */
    private String getArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<String> list = new ArrayList<>();
        for (Object arg : args) {
            if (null == arg || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                continue;
            }
            list.add(arg.toString());
        }
        return list.toString();
    }

    @AfterReturning(pointcut = "webLog()")
    public void doAfterReturning() {

    }


}

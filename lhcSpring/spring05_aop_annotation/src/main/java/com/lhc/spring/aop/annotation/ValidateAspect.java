package com.lhc.spring.aop.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 14:20
 * @ClassName:
 * @Description:
 */
@Component
@Aspect
@Order(1) //设置切面的优先级
public class ValidateAspect {

    //和公共的切入点表达式不在同一个切面类时,需要使用该方法的全类名
    @Before("com.lhc.spring.aop.annotation.LoggerAspect.pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("ValidateAspect-->前置通知，方法名：" + methodName + "，参数：" + args);
    }

}

package com.lhc.spring.aop.xml;

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
public class ValidateAspect {

    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("ValidateAspect-->前置通知，方法名：" + methodName + "，参数：" + args);
    }

}

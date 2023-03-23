package com.lhc.spring.aop.xml;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 12:24
 * @ClassName:
 * @Description:
 */
@Component //放入ioc容器中

public class LoggerAspect {


    public void beforeAdviceMethod(JoinPoint joinPoint) {
        //获取连接点所对应方法的方法名
        String methodName = joinPoint.getSignature().getName();
        //获取连接点所对应方法的参数
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("Logger-->前置通知，方法名：" + methodName + "，参数：" + args);
    }


    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->后置通知，方法名：" + methodName + "，执行完毕");
    }


    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->返回通知，方法名：" + methodName + "，结 果：" + result);
    }


    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->异常通知，方法名：" + methodName + "，异常：" + ex);
    }



    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

        Object result = null;
        try {
            System.out.println("环绕通知-->前置通知");
            //表示目标对象方法的执行
            result = proceedingJoinPoint.proceed();
            System.out.println("环绕通知-->返回通知");

        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("环绕通知-->异常通知");

        } finally {
            System.out.println("环绕通知-->后置通知");
        }
        return result;
    }

}

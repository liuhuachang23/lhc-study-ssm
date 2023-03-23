package com.lhc.spring.aop.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
@Aspect  //将当前组件标识为切面类
public class LoggerAspect {

    //@Pointcut 设置一个公共的切入点表达式
    @Pointcut("execution(* com.lhc.spring.aop.annotation.CalculatorImpl.*(..))")
    public void pointCut() {}

    /**
     * 前置通知：@Before(value="切入点表达式")
     *
     * @param joinPoint: 连接点信息
     */
    //@Before("execution(* com.lhc.spring.aop.annotation.CalculatorImpl.*(..))")
    @Before("pointCut()") //使用公共的切入点表达式
    public void beforeAdviceMethod(JoinPoint joinPoint) {

        //获取连接点所对应方法的方法名
        String methodName = joinPoint.getSignature().getName();
        //获取连接点所对应方法的参数
        String args = Arrays.toString(joinPoint.getArgs());

        System.out.println("Logger-->前置通知，方法名：" + methodName + "，参数：" + args);
    }


    /**
     * 后置通知：@After(value="切入点表达式")
     *
     * @param joinPoint: 连接信息
     */
    @After("pointCut()")
    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->后置通知，方法名：" + methodName + "，执行完毕");
    }

    /**
     * 返回通知：@AfterReturning(value = "切入点表达式", returning = "result")
     *              returning属性 就是用来接收目标对象方法执行之后的返回值
     *              属性值 "result" 就是将该返回值指定给 通知方法的同名参数
     *
     * @param joinPoint: 连接点信息
     * @param result:    表示目标对象方法执行之后的返回值 (参数名 和 returning属性值 保持一致)
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->返回通知，方法名：" + methodName + "，结 果：" + result);
    }

    /**
     * 异常通知：@AfterThrowing(value = "切入点表达式", throwing = "ex")
     *              throwing属性：就是用来接收目标对象方法执行过程中出现的异常，
     *              属性值 "ex"： 就是将该异常指定给 通知方法的同名参数
     *
     * @param joinPoint: 连接点信息
     * @param ex:        表示目标对象方法执行出现的异常 （参数名 和 throwing属性值 保持一致）
     */
    @AfterThrowing(value = "pointCut()", throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->异常通知，方法名：" + methodName + "，异常：" + ex);
    }


    /**
     * 环绕通知：@Around(value = "切入点表达式")
     *
     * @param proceedingJoinPoint：可执行连接点
     * @return 目标对象方法执行的返回值
     */
    @Around("pointCut()")
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

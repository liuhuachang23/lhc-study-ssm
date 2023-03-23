package com.lhc.spring.aop.test;

import com.lhc.spring.aop.annotation.Calculator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 12:42
 * @ClassName:
 * @Description:
 */
public class AOPByAnnotationTest {

    @Test
    public void test(){
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-aop-annotation.xml");
        //这里我们使用了AOP之后，只能通过代理对象来访问目标对象（CalculatorImpl）的方法，不能通过直接访问目标对象了
        //在IOC中已经获取不到目标对象了，要通过代理对象的接口来获取代理对象
        //Calculator calculator = ioc.getBean(CalculatorImpl.class); --> NoSuchBeanDefinitionException
        Calculator calculator = ioc.getBean(Calculator.class);
        calculator.div(1,1);
    }
}

package com.lhc.spring.aop.test;

import com.lhc.spring.aop.xml.Calculator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 14:41
 * @ClassName:
 * @Description:
 */
public class AOPByXMLTest {

    @Test
    public void test(){
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-aop-xml.xml");

        //通过代理对象的接口来获取代理对象
        Calculator calculator = ioc.getBean(Calculator.class);
        calculator.div(1,1);
    }

}

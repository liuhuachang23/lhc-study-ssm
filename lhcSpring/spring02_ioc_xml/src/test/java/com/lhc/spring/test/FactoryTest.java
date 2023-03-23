package com.lhc.spring.test;

import com.lhc.spring.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 14:54
 * @ClassName:
 * @Description:
 */
public class FactoryTest {

    @Test
    public void testUserFactoryBean(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-factory.xml");
        User user = (User) ioc.getBean("user");
        System.out.println(user);
    }
}

package com.lhc.spring.test;

import com.lhc.spring.controller.UserController;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 15:32
 * @ClassName:
 * @Description:
 */
public class AutowireByXMLTest {

    @Test
    public void test() {

        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-autowire.xml");
        UserController userController = ioc.getBean(UserController.class);
        userController.saveUser();
    }

}

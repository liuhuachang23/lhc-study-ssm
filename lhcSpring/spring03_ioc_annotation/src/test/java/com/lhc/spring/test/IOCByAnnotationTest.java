package com.lhc.spring.test;

import com.lhc.spring.controller.UserController;
import com.lhc.spring.dao.UserDao;
import com.lhc.spring.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 14:58
 * @ClassName:
 * @Description:
 */
public class IOCByAnnotationTest {

    @Test
    public void testAutowireByAnnotation(){
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc-annotation.xml");

        UserController userController = ioc.getBean(UserController.class);
        System.out.println(userController);
        UserService userService = ioc.getBean(UserService.class);
        System.out.println(userService);
        UserDao userDao = (UserDao) ioc.getBean("userDaoImpl");
        System.out.println(userDao);

        userController.saveUser();


    }

}

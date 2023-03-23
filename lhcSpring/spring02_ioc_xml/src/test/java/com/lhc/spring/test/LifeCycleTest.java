package com.lhc.spring.test;

import com.lhc.spring.pojo.User;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 10:29
 * @ClassName:
 * @Description:
 */
public class LifeCycleTest {

    @Test
    public void test(){
        //ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-lifecycle.xml");
        //ApplicationContext没有提供 关闭和刷新容器的方法，他们的子接口和实现类有：
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("spring-lifecycle.xml");
        User bean = ioc.getBean(User.class);
        System.out.println(bean);
        ioc.close();
    }

}

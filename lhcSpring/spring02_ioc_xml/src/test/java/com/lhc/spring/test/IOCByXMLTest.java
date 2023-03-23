package com.lhc.spring.test;

import com.lhc.spring.pojo.Clazz;
import com.lhc.spring.pojo.Person;
import com.lhc.spring.pojo.Student;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/2 21:09
 * @ClassName:
 * @Description:
 */
public class IOCByXMLTest {

    @Test
    public void testIOC(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        //1）根据bean的id获取
        //Student bean = (Student) ioc.getBean("studentOne");
        //2）根据bean的类型获取
        //Student bean = ioc.getBean(Student.class);
        //3）根据bean的id和类型来获取
        //Student bean = ioc.getBean("studentOne",Student.class);

        //4）根据bean的 上级接口、父类 获取bean
        Person bean = ioc.getBean(Person.class);
        System.out.println(bean);
    }

    @Test
    public void testDI01(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        Student bean = ioc.getBean("studentTwo",Student.class);
        System.out.println(bean);
    }

    @Test
    public void testDI02(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        Student bean = ioc.getBean("studentThree",Student.class);
        System.out.println(bean);
    }

    @Test
    public void testDI03(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        Student bean = ioc.getBean("studentFour",Student.class);
        System.out.println(bean);
    }

    @Test
    public void testDI04(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        Student bean = ioc.getBean("studentFive",Student.class);
        System.out.println(bean);
    }

    @Test
    public void testDI05(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        Clazz bean = ioc.getBean("clazzTwo",Clazz.class);
        System.out.println(bean);
    }


    @Test
    public void testDI06(){
        //获取IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
        //获取bean
        Student bean = ioc.getBean("studentSex",Student.class);
        System.out.println(bean);
    }

}

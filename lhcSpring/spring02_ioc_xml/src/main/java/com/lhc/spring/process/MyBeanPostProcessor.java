package com.lhc.spring.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 11:44
 * @ClassName:
 * @Description:
 */
public class MyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("MyBeanPostProcessor-->后置处理器的postProcessBeforeInitialization");
        System.out.println("☆☆☆" + beanName + " = " + bean);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("MyBeanPostProcessor->后置处理器的postProcessAfterInitialization");
        System.out.println("★★★" + beanName + " = " + bean);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}

package com.lhc.spring.test;

import com.lhc.spring.proxy.Calculator;
import com.lhc.spring.proxy.CalculatorImpl;
import com.lhc.spring.proxy.CalculatorStaticProxy;
import com.lhc.spring.proxy.ProxyFactory;
import org.junit.Test;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 19:58
 * @ClassName:
 * @Description:
 */
public class ProxyTest {

    @Test
    public void test(){

//        CalculatorStaticProxy proxy = new CalculatorStaticProxy(new CalculatorImpl());
//        proxy.add(1,4);

        ProxyFactory proxyFactory = new ProxyFactory(new CalculatorImpl());
        //获取动态代理类
        //（我们不知道动态代理类的类型，但是我们知道 动态代理类和目标类 实现了相同的接口，即可以向上转型为 相同的接口类型）
        Calculator proxy = (Calculator) proxyFactory.getProxy();
        proxy.div(1,1);

    }
}

package com.lhc.spring.proxy;

import com.lhc.spring.proxy.Calculator;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 19:11
 * @ClassName:
 * @Description:
 */
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        int result = i + j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int sub(int i, int j) {

        int result = i - j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int mul(int i, int j) {

        int result = i * j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int div(int i, int j) {

        int result = i / j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

}

package com.lhc.spring.service;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/8 11:07
 * @ClassName:
 * @Description:
 */
public interface CheckoutService {
    void checkout(Integer[] bookIds, Integer userId);
}

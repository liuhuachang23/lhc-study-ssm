package com.lhc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/17 21:20
 * @ClassName: 异常类
 * @Description: 基于注解配置异常类，来处理异常
 */

//@ControllerAdvice将当前类标识为异常处理的组件
@ControllerAdvice
public class Exception01 {

    //@ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(ArithmeticException.class)
    //ex表示当前请求处理中出现的异常对象
    public String handleArithmeticException(Exception ex, Model model){
        model.addAttribute("ex", ex);
        return "error";
    }

}
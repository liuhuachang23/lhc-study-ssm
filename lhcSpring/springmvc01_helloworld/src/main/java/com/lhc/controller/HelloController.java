package com.lhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/8 16:44
 * @ClassName:
 * @Description:
 */
@Controller
public class HelloController {

    // @RequestMapping注解：处理请求和控制器方法之间的映射关系
    // @RequestMapping注解的value属性可以通过请求地址匹配请求，
    // value = "/" 表示当前工程的上下文路径 即：localhost:8080/springmvc01/
    @RequestMapping("/")
    public String portal() {
        //返回 逻辑视图
        return "index";
        //返回后会被配置文件中配置的视图解析器来解析,加上前置后置，通过thymeleaf的渲染 完成视图的跳转
    }

    @RequestMapping("/hello")
    public String hello(){
        return "success";
    }
}

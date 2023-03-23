package com.lhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/17 20:56
 * @ClassName:
 * @Description:
 */
@Controller
public class TestController {

    @RequestMapping("/test/exception")
    public String testException() {
        System.out.println(1 / 0);
        return "success";
    }
}

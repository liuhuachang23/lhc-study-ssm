package com.lhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/10 10:44
 * @ClassName:
 * @Description:
 */
@Controller
public class TestViewController {


    @RequestMapping("/test/view/thymeleaf")
    public String testThymeleafView(){
        return "success";
    }

    @RequestMapping("/test/view/forward")
    public String testInternalResourceView(){
        //转发到：/test/mav
        return "forward:/test/mav";
    }

    @RequestMapping("/test/view/redirect")
    public String testRedirectView(){
        //重定向到：/test/mav
        return "redirect:/test/mav";
    }
}

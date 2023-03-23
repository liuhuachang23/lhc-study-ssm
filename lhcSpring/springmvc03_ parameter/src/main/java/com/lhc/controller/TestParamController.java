package com.lhc.controller;

import com.lhc.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/9 17:14
 * @ClassName:
 * @Description:
 */
@Controller
public class TestParamController {


    @RequestMapping("/testParam/servletAPI")
    public String testParam1(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username:" + username + ",password:" + password);

        return "success";
    }

    @RequestMapping("/testParam")
    public String testParam2(
            @RequestParam(value = "username", required = false, defaultValue = "hello") String uname,
            String password,
            @RequestHeader("referer") String referer,
            @CookieValue("JSESSIONID") String jsessionid
    ) {
        System.out.println("username:" + uname + ",password:" + password);
        System.out.println("referer:" + referer);
        System.out.println("jsessionid:" + jsessionid);
        return "success";
    }

    @RequestMapping("/param/pojo")
    public String getParamByPojo(User user){
        System.out.println(user);
        return "success";
    }


}

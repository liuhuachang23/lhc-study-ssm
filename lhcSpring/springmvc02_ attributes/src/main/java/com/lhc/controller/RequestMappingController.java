package com.lhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/9 8:59
 * @ClassName:
 * @Description:
 */
@Controller
//@RequestMapping("/test") // 设置映射请求的请求路径的初始信息
public class RequestMappingController {

    // 设置映射请求请求路径的具体信息
    // 此时请求映射所映射的请求的请求路径为：/test/testRequestMapping
    @RequestMapping(
            value = {"/hello", "/abc"},
            method = {RequestMethod.GET, RequestMethod.POST},
            //params = {"username", "!password", "age=20", "gender!=女"},
            headers = {"referer"}
    )
    public String testRequestMapping() {
        return "success";
    }

    @RequestMapping("/**/test/ant")
    public String testAnt() {
        return "success";
    }

    @RequestMapping("/test/rest/{id}/{username}")
    public String testRest(@PathVariable("id") Integer id, @PathVariable("username") String username) {
        System.out.println("id：" + id + "，username；" + username);
        return "success";
    }


}

package com.lhc.controller;

import com.lhc.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/13 16:22
 * @ClassName:
 * @Description:
 */
@Controller
public class TestAjaxController {

    //1. 测试SpringMVC处理ajax请求
    @RequestMapping("/test/ajax")
    public void testAjax(Integer id, @RequestBody String requestBody, HttpServletResponse response) throws IOException {

        System.out.println("requestBody:" + requestBody);
        System.out.println("id:" + id);
        response.getWriter().write("hello,ajax");
    }

    //2. 通过@RequestBody注解 使用一个实体类来接收 Json格式的请求参数
    /*@RequestMapping("/test/RequestBody/json")
    public void testRequestBodyJson(@RequestBody User user, HttpServletResponse response) throws IOException {

        System.out.println("user:" + user);
        response.getWriter().write("hello,RequestBody");

    }*/
    //通过@RequestBody注解 使用一个Map集合来接收 Json格式的请求参数
    @RequestMapping("/test/RequestBody/json")
    public void testRequestBodyJson(@RequestBody Map<String, Object> map, HttpServletResponse response) throws IOException {

        System.out.println("map:" + map);
        response.getWriter().write("hello,RequestBody");

    }


    //3. 测试使用@ResponseBody响应JSON格式的ajax请求
    /* 响应一个User对象
    @RequestMapping("/test/ResponseBody/json")
    @ResponseBody
    public User testResponseBodyJson(){
        User user = new User(1001, "admin1", "123456", 23, "男");
        return user;
    }
    */
    /* 响应一个map集合
    @RequestMapping("/test/ResponseBody/json")
    @ResponseBody
    public Map<String,Object> testResponseBodyJson(){
        User user1 = new User(1001, "admin1", "123456", 23, "男");
        User user2 = new User(1002, "admin2", "123456", 23, "男");
        User user3 = new User(1003, "admin3", "123456", 23, "男");
        Map<String,Object> map = new HashMap<>();
        map.put("1001",user1);
        map.put("1002",user2);
        map.put("1003",user3);
        return map;
    }
    */
    //响应一个List集合
    @RequestMapping("/test/ResponseBody/json")
    @ResponseBody
    public List<User> testResponseBodyJson() {
        User user1 = new User(1001, "admin1", "123456", 23, "男");
        User user2 = new User(1002, "admin2", "123456", 23, "男");
        User user3 = new User(1003, "admin3", "123456", 23, "男");
        List<User> list = Arrays.asList(user1, user2, user3);
        return list;
    }


    //4. 使用 @ResponseBody注解响应浏览器普通请求

    @RequestMapping("/test/ResponseBody")
    @ResponseBody
    public User testResponseBody() {
        return new User(1001, "admin1", "123456", 23, "男");
    }

    @RequestMapping("/testRequestBody")
    @ResponseBody
    public String testRequestBody(@RequestBody String requestBody){
        System.out.println("requestBody:"+requestBody);
        return "success";
    }
}

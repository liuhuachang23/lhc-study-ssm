package com.lhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/10 20:14
 * @ClassName:
 * @Description:
 * 查询所有的用户信息 --> /user --> get
 * 根据id查询用户信息 --> /user/1 --> get
 * 添加用户信息 --> /user --> post
 * 修改用户信息 --> /user --> put
 * 删除用户信息 --> /user/1 --> delete
 */
@Controller
public class TestRestController {

    @GetMapping("/user")
    public String getAllUser() {
        System.out.println("查询所有的用户信息 --> /user --> get");
        return "success";
    }

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable("id") Integer id) {
        System.out.println("根据id查询用户信息 --> /user/" + id + " --> get");
        return "success";
    }

    @PostMapping("/user")
    public String insertUser() {
        System.out.println("添加用户信息 --> /user --> post");
        return "success";
    }

    @PutMapping("/user")
    public String updateUser() {
        System.out.println("修改用户信息 --> /user --> put");
        return "success";
    }

    
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        System.out.println("删除用户信息 --> /user/" + id + " --> delete");
        return "success";
    }

}

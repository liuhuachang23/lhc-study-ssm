package com.lhc.spring.controller;

import com.lhc.spring.service.UserService;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 15:18
 * @ClassName:
 * @Description:
 */
public class UserController {

    // UserService 属性
    private UserService userService;

    //set方法
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //业务方法
    public void saveUser(){
        userService.saveUser();
    }

}

package com.lhc.spring.controller;

import com.lhc.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 14:51
 * @ClassName:
 * @Description:
 */
@Controller()
public class UserController {

    @Autowired(required = false)
    private UserService userService;

    public void saveUser(){
        userService.saveUser();
    }

}

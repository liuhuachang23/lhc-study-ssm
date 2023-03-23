package com.lhc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/9 17:14
 * @ClassName:
 * @Description:
 */
@Controller
public class TestParamController {



    @RequestMapping("/test/mav")
    public ModelAndView testModelAndView(){

        /**
         * ModelAndView有Model和View的功能
         * Model：主要用于向请求域共享数据
         * View：主要用于设置视图，实现页面跳转
         */
        ModelAndView mav = new ModelAndView();

        //1）向请求域共享数据
        mav.addObject("testScope", "hello,ModelAndView");
        //2）设置视图，实现页面跳转
        mav.setViewName("success");
        return mav;
    }


}

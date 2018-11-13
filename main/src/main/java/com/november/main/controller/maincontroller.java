package com.november.main.controller;

import com.november.demo2.service.BillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class maincontroller {
    @RequestMapping(value = "/home.html")
    public String home(){ return "home"; }

    @RequestMapping(value = "/login")
    public String login(){

        return "redirect:/home.html";
    }


    @RequestMapping(value = "/user.html")
    public String userPage(){
        return "user";
    }

    @RequestMapping(value = "/welcome.html")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping(value = "/test1.html")
    public String test1(){
        return "test1";
    }


}

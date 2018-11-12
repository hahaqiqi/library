package com.november.main.controller;

import com.november.demo2.service.BillService;
import com.november.main.service.Server;
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

    @RequestMapping(value = "/login.html")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/tologin.html")
    public String tologin(){
        return "redirets:login2.html";
    }

    @RequestMapping(value = "/user.html")
    public String userPage(){
        return "user";
    }

    @RequestMapping(value = "/welcome.html")
    public String welcome(){
        return "welcome";
    }


}

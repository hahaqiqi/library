package com.november.main.controller;

import com.november.admin.model.Admin;
import com.november.common.JsonData;
import com.november.common.RequestHolder;
import com.november.demo2.service.BillService;
import com.november.main.param.LoginParam;
import com.november.main.service.LoginService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class MainController {

    @Resource
    private LoginService loginService;

    @RequestMapping(value = "/home.html")
    public String home(){ return "home"; }

    @RequestMapping(value = "/login.html")
    public String login(){
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/login.json")
    public JsonData login(LoginParam param, HttpServletRequest request,HttpSession session){
        Admin admin = loginService.login(param);
        session.setAttribute("admin",admin);
        com.november.model.Admin copyAdmin = new com.november.model.Admin();
        BeanUtils.copyProperties(admin,copyAdmin);
        RequestHolder.add(copyAdmin);
        return JsonData.success();
    }

    @RequestMapping(value = "/logout.html")
    public String logout(){
        return "logout";
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

    @ResponseBody
    @RequestMapping(value = "/error.html")
    public JsonData error(){
        return JsonData.fail("无权限访问");
    }

    @ResponseBody
    @RequestMapping(value = "/unauth.json")
    public JsonData unauth(){
        return JsonData.fail("无权限访问");
    }
}

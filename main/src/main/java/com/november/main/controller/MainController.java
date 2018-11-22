package com.november.main.controller;

import com.november.admin.model.Admin;
import com.november.common.JsonData;
import com.november.common.RequestHolder;
import com.november.demo2.service.BillService;
import com.november.main.param.LoginParam;
import com.november.main.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class MainController {

    @Resource
    private LoginService loginService;

    @RequestMapping(value = "/home.html")
    public String home(){ return "home"; }

    @ResponseBody
    @RequestMapping(value = "/login.json")
    public JsonData login(LoginParam param, HttpServletRequest request){
        Admin admin = loginService.login(param);
        com.november.model.Admin copyAdmin = new com.november.model.Admin();
        BeanUtils.copyProperties(admin,copyAdmin);
        RequestHolder.add(copyAdmin);
        RequestHolder.add(request);
        return JsonData.success();
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

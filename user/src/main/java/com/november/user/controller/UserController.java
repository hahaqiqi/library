package com.november.user.controller;

import com.november.common.JsonData;
import com.november.user.model.User;
import com.november.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/user")
public class UserController {
    @Resource(name="userService")
    private UserService userService;

    @RequestMapping(value = "/user.html")
    public String toUsers(){
        return "users";
    }

    @ResponseBody
    @RequestMapping(value="/x.json")
    public JsonData save(User record){
        record.setId(1);
        record.setUserName("li");
        record.setIdCard("430624199701040035");
        record.setOperator("li");
        record.setStatus(1);
        record.setUserBalance(1000.0);
        record.setUserPhone("17680466440");
        record.setUserEmail("2778034124@qq.com");
        record.setUserScore(100);
        Date date=new Date();
        record.setUserBirthday(date);
        record.setOperateTime(date);
        record.setRemark(null);
        Integer i=userService.insert(record);
        System.out.println(i);
        return JsonData.success();
    }
    @ResponseBody
    @RequestMapping(value="/save.json")
    public JsonData saveByParam(User record){

        Integer i=userService.insertSelective(record);
        System.out.println(i);
        return JsonData.success();
    }
    @ResponseBody
    @RequestMapping(value="/list.json",method = RequestMethod.GET)
    public JsonData select(HttpServletRequest request){
        int count=userService.userCount();//总行数
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<User> users= userService.select(page,limit);
        return JsonData.pageSuccess(users,count,limit);
    }

    @ResponseBody
    @RequestMapping(value="/update.json")
    public JsonData update(User record){
        record.setId(1);
        record.setUserName("admin");
        Integer i=userService.updateByPrimaryKey(record);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value="/delete.json")
    public JsonData delete(Integer id){
        return JsonData.success();
    }


    @RequestMapping(value="/login.html")
    public String login(){
        return "save";
    }



}

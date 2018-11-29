package com.november.user.controller;

import com.november.common.JsonData;
import com.november.exception.ParamException;
import com.november.user.model.User;
import com.november.user.param.UserParam;
import com.november.user.service.UserService;
import com.november.util.Email;
import com.november.util.IsEmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="/user")
public class UserController {
    @Resource(name="userService")
    private UserService userService;

    @RequestMapping(value = "/user.html")//前往分页查询
    public String toUsers(){
        return "users";
    }

    @RequestMapping(value = "/tosave.html")//去添加页面
    public String toSave(){ return "save";}

   /* @ResponseBody
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
    }*/
    //保存
    @ResponseBody
    @RequestMapping(value="/save.json")
    public JsonData saveByParam(UserParam record){
        Integer i=userService.insertSelective(record);
        return JsonData.success();
    }
    //分页
    @ResponseBody
    @RequestMapping(value="/list.json",method = RequestMethod.GET)
    public JsonData select(HttpServletRequest request){
        int count=userService.userCount();//总行数
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<User> users= userService.select(page,limit);
        return JsonData.pageSuccess(users,count,limit);
    }
    //修改
    @ResponseBody
    @RequestMapping(value="/update.json",method = RequestMethod.POST)
    public JsonData update(UserParam record){
        Integer i=userService.updateByPrimaryKeySelective(record);
        return JsonData.success();
    }

    //根据邮箱查用户
    @ResponseBody
    @RequestMapping(value="/selectUserByEmail.json")
    public JsonData SelectUserByEmail(String email){
        User user=userService.selectUserByEmail(email);
        System.out.print(user.toString());
        return JsonData.success(user);
    }

    @ResponseBody
    @RequestMapping(value="/yzEmail.json",method = RequestMethod.POST)
    public JsonData yzEmail( String userEmail){
        if(!IsEmail.isEmail(userEmail) || userEmail==""){
            throw new ParamException("邮箱格式不正确或者为空");
        }
           String yzm= Email.GetCode(userEmail);
           System.out.print(yzm);
        return JsonData.success(yzm);
    }


}

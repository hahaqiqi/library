package com.november.user.controller;

import com.november.common.JsonData;
import com.november.user.model.UserType;
import com.november.user.param.UserTypeParam;
import com.november.user.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="/userType")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @RequestMapping(value = "/userType.html")//前往查询页面
    public String toUserType(){
        return "userType";
    }

    @ResponseBody
    @RequestMapping(value = "save.json")//保存
    public JsonData saveUserType(UserTypeParam param){
        Integer i=userTypeService.insertSelective(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value="list.json",method = RequestMethod.GET)//查询方法
    public JsonData selectUserType(HttpServletRequest request){
        List<UserType> list=userTypeService.selectUsertypeByScore();
        return JsonData.success(list);
    }
    @ResponseBody
    @RequestMapping(value="update.json",method = RequestMethod.POST)//修改
    public JsonData updateUserType(UserTypeParam param){
        int i=userTypeService.updateByPrimaryKeySelective(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping(value="/selectUsertypeByScore.json")//根据积分查用户类型
    public JsonData selectUsertypeByScore(Integer score){
        UserType userType=userTypeService.selectUsertypeByScore(score);
        System.out.print(userType);
        return JsonData.success(userType);
    }
    @ResponseBody
    @RequestMapping(value="/delete.json")//删除
    public JsonData delete(Integer id){
        int i=userTypeService.deleteByPrimaryKey(id);
        return JsonData.success();
    }
}

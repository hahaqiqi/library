package com.november.user.controller;

import com.november.common.JsonData;
import com.november.user.model.UserType;
import com.november.user.param.UserTypeParam;
import com.november.user.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/userType")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    public JsonData saveUserType(UserTypeParam userType){

        userTypeService.insertSelective(userType);
        return JsonData.success();
    }
    @ResponseBody
    @RequestMapping(value="/selectUsertypeByScore.json")
    public JsonData selectUsertypeByScore(Integer score){
        score=100;
        UserType userType=userTypeService.selectUsertypeByScore(score);
        System.out.print(userType);
        return JsonData.success();
    }
}

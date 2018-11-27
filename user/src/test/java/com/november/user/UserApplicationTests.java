package com.november.user;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RunWith(SpringRunner.class)
@SpringBootTest
@Controller
@RequestMapping(value="/test")
public class UserApplicationTests {
    @RequestMapping(value="/totest.html")
    public String test(){
        return "test";
    }

    @ResponseBody
    @RequestMapping(value="/yzPhone.json")
    public String yzPhone(String userPhone, HttpSession session){
        userPhone="17680466440";
        System.out.println(userPhone);
        String code=GetMassageCode.getCode(userPhone);
        session.setAttribute("yz",code);
        return code;
    }

}

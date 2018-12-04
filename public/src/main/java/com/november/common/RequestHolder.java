package com.november.common;

import com.november.model.Admin;

import javax.servlet.http.HttpServletRequest;

/**
 * @author skrT
 * @create 2018/11/3 9:49
 */
public class RequestHolder {

    private static Admin admin = null;

    public static Admin getCurrentAdmin(){
        if(admin == null){
            Admin ad = new Admin();
            ad.setAdminCode("admin");
            return ad;
        }
        return admin;
    }

    public static void add(Admin adm){
        admin = adm;
    }
}

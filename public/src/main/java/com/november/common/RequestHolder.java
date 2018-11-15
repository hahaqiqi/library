package com.november.common;

import com.november.model.Admin;

import javax.servlet.http.HttpServletRequest;

/**
 * @author skrT
 * @create 2018/11/3 9:49
 */
//  管理员模块完成后才用
public class RequestHolder<T> {

    private static final ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(Admin admin){
        adminHolder.set(admin);
    }

    public static void add(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static Admin getCurrentAdmin(){
        return adminHolder.get();
    }

    public static HttpServletRequest getCurrentRequest(){
        return requestHolder.get();
    }

}

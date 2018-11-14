package com.november.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author skrT
 * @create 2018/11/3 9:49
 */
//  用户模块完成后才用
public class RequestHolder<T> {


    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();


    public static void add(HttpServletRequest request){
        requestHolder.set(request);
    }


    public static HttpServletRequest getCurrentRequest(){
        return requestHolder.get();
    }

}

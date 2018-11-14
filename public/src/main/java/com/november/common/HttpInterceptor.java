package com.november.common;

import com.november.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author skrT
 * @create 2018/10/30 11:27
 */

//  拦截器
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String STAT_TIME = "requestStat";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long stat = System.currentTimeMillis();
        request.setAttribute(STAT_TIME, stat);
        String url = request.getRequestURI();
        Map parameterMap = request.getParameterMap();
        String jsonStr = JsonMapper.obj2String(parameterMap);
        System.out.println(jsonStr);
        jsonStr = jsonStr.replace('[',' ');
        jsonStr = jsonStr.replace(']',' ');
        log.info("interceptor on url:{" + url + "},params:{" + JsonMapper.obj2String(parameterMap) + "}");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long stat = (long) request.getAttribute(STAT_TIME);
        long end = System.currentTimeMillis();
        log.info("执行时间为:" + (end - stat));
    }
}

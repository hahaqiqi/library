package com.november.main.commons;

import com.november.exception.LibraryException;
import com.november.exception.ParamException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class CustomRolesAuthorizationFilter extends RolesAuthorizationFilter {
    @Override
    public boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) {
        Subject subject = getSubject(req, resp);
        String[] rolesArray = (String[]) mappedValue;
        //如果没有角色限制，直接放行
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        for (int i = 0; i < rolesArray.length; i++) {
            //若当前用户是rolesArray中的任何一个，则有权限访问
            if (subject.hasRole(rolesArray[i])) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        //处理跨域问题，跨域的请求首先会发一个options类型的请求
        if (servletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        boolean isAccess = isAccessAllowed(request, response, mappedValue);
        if (isAccess) {
            return true;
        }
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            throw new ParamException("您还未登录，请先登录");
        } else {
            ((HttpServletResponse) response).sendRedirect("/unauth.json");
        }
        return false;
    }
}

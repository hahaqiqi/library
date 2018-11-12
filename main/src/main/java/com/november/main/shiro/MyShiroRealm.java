package com.november.main.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyShiroRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限认证方法：MyShiroRealm.doGetAuthenticationInfo()");
//      User user = (User)SecurityUtils.getSubject().getPrincipal();
        Set<String> roles = new HashSet<>();
        //可根据user查询数据库得到该用户的权限,添加到roles中
//        if("admin".equals(user.getUserCode())){
//            roles.add("admin");
//        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles); //根据用户ID查询角色（role），放入到Authorization里。
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //User user= userService.login(upToken.getUsername(),String.valueOf(upToken.getPassword()));
        //return new SimpleAuthenticationInfo(user,user.getUserPassword(),getName());
        return new SimpleAuthenticationInfo(null,"pwd",getName());
    }
}

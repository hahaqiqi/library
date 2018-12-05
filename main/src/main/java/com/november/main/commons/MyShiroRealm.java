package com.november.main.commons;

import com.november.acl.model.Acl;
import com.november.acl.model.Role;
import com.november.acl.service.AclService;
import com.november.acl.service.RoleAclServiceImpl;
import com.november.acl.service.RoleAdminService;
import com.november.acl.service.RoleService;
import com.november.admin.model.Admin;
import com.november.admin.service.AdminService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/24 13:53
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private AdminService adminService;

    @Resource
    private RoleAdminService roleAdminService;

    @Resource
    private RoleService roleService;

    @Resource
    private RoleAclServiceImpl roleAclService;

    @Resource
    private AclService aclService;

    /**
     * 授权用户权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名
        String name= (String) principals.getPrimaryPrincipal();
        //查询用户名称
        Admin admin = adminService.getAdminByAdminCode(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 获得当前管理员的所有角色id
        List<Integer> roleIds = roleAdminService.getRoleIdListByAdminId(admin.getId());
        //  用id集合获得角色
        List<Role> roles = roleService.getByIds(roleIds);
        if(CollectionUtils.isNotEmpty(roles)){
            for (Role role : roles) {
                //添加角色
                simpleAuthorizationInfo.addRole(role.getRoleName());
                // 根据角色id获得拥有的权限
                List<Integer> aclIds = roleAclService.getAclIdsByRoleId(role.getId());
                //  用权限id获得权限集合
                List<Acl> acls = aclService.getByIds(aclIds);
                if(CollectionUtils.isNotEmpty(acls)){
                    for (Acl acl : acls) {
                        //添加权限
                        simpleAuthorizationInfo.addStringPermission(acl.getUrl());
                    }
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = token.getPrincipal().toString();
        String password = new String((char[]) token.getCredentials());
        Admin admin = adminService.getAdminByAdminCode(name);
        if (admin == null || ! password.equals(admin.getAdminPwd())) {
            throw new UnknownAccountException("账户或密码不正确");
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, admin.getAdminPwd().toString(), getName());
            return simpleAuthenticationInfo;
        }
    }

}

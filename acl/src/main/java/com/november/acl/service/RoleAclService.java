package com.november.acl.service;

import java.security.acl.Acl;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/25 13:49
 */
public interface RoleAclService {

    List<Integer> getAclIdsByRoleId(int roleId);

}

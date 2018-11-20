package com.november.acl.service;

import com.november.acl.model.Acl;
import com.november.acl.model.Role;
import com.november.acl.param.RoleParam;

import java.util.List;

/**
 * @author skrT
 * @create 2018/11/15 9:11
 */
public interface RoleService {

    List<Role> getAll();

    void save(RoleParam param);

    void update(RoleParam param);

    void delete(int id);

}

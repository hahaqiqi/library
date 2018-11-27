package com.november.acl.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.november.acl.dao.RoleAclMapper;
import com.november.common.RequestHolder;
import com.november.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RoleAclServiceImpl implements RoleAclService {

    @Resource
    private RoleAclMapper roleAclMapper;

    @Override
    public List<Integer> getAclIdsByRoleId(int roleId) {
        return roleAclMapper.getAclIdByRoleId(roleId);
    }

}

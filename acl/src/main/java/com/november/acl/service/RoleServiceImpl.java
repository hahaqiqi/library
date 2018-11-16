package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.november.acl.dao.RoleMapper;
import com.november.acl.model.Role;
import com.november.acl.param.RoleParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getRoleName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        Role role = Role.builder().roleName(param.getRoleName())
                .remark(param.getRemark()).build();
        role.setOperator("admin");
        role.setOperateTime(new Date());
        roleMapper.insertSelective(role);
    }

    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getRoleName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        Role before = roleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        Role after = Role.builder().id(param.getId()).roleName(param.getRoleName())
                .remark(param.getRemark()).build();
        after.setOperator("admin");     //  TODO
        after.setOperateTime(new Date());
        roleMapper.updateByPrimaryKeySelective(after);
    }

    public List<Role> getAll() {
        return roleMapper.getAll();
    }

    private boolean checkExist(String roleName, Integer id) {
        return roleMapper.countByRoleName(roleName, id) > 0;
    }

    /*public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }*/
}

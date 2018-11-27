package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.november.model.CacheType;
import com.november.acl.dao.RedisDao;
import com.november.acl.dao.RoleAclMapper;
import com.november.acl.dao.RoleAdminMapper;
import com.november.acl.dao.RoleMapper;
import com.november.acl.model.Role;
import com.november.acl.param.RoleParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import com.november.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleAdminMapper roleAdminMapper;

    @Resource
    private RoleAclMapper roleAclMapper;

    @Resource(name = "aclRedisDao")
    private RedisDao redisDao;

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
        redisDao.setExKey(CacheType.NEEDUPDATE_PREFIX+"role","true",10,TimeUnit.MINUTES);
    }

    @Transactional
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
        redisDao.setExKey(CacheType.NEEDUPDATE_PREFIX+"role","true",10,TimeUnit.MINUTES);
    }

    @Override
    @Transactional
    public void delete(int id) {
        BeanValidator.check(id);
        List<Integer> adminIdsByRoleId = roleAdminMapper.getAdminIdsByRoleId(id);
        if(CollectionUtils.isNotEmpty(adminIdsByRoleId)){
            throw new ParamException("该角色下还有管理员");
        }
        roleMapper.deleteByPrimaryKey(id);
        roleAclMapper.deleteByRoleId(id);
        redisDao.setExKey(CacheType.NEEDUPDATE_PREFIX+"role","true",10,TimeUnit.MINUTES);
    }

    @Override
    public List<Role> getByIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return Lists.newArrayList();
        }
        List<Role> roles = roleMapper.getByIdList(ids);
        return roles;
    }

    public List<Role> getAll() {
        //  获取是否需要更新的值
        String flag = redisDao.getValue(CacheType.NEEDUPDATE_PREFIX+"role");
        //  判断是否不为空
        if(StringUtils.isNotBlank(flag)){
            //  判断是否是true
            if(Boolean.valueOf(flag)){
                //  重新从数据库中取出数据
                List<Role> all = roleMapper.getAll();
                //  设置不需要马上更新
                redisDao.setExKey(CacheType.NEEDUPDATE_PREFIX+"role","false",10,TimeUnit.MINUTES);
                //  返回值
                return all;
            }
        }
        //  从redis中取值
        String value = redisDao.getValue(CacheType.LIST_PREFIX + "role");
        //  判断是否有值
        if(StringUtils.isBlank(value)){
            //  从数据库中取值
            List<Role> all = roleMapper.getAll();
            //  判断数据库中取的值是否为空
            if(CollectionUtils.isEmpty(all)){
                //  是则返回新集合
                return Lists.newArrayList();
            }
            //  转化为字符串
            String sValue = JsonMapper.obj2String(all);
            //  存值至10分钟
            redisDao.setExKey(CacheType.LIST_PREFIX+"role",sValue,10,TimeUnit.MINUTES);
            //  返回数据库取的值
            return all;
        }
        //  转化从redis中取的值
        List<Role> roles = JsonMapper.string2Obj(value, new TypeReference<List<Role>>() {
        });
        //  返回值
        return roles;
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

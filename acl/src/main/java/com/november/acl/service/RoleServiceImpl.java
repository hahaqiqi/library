package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.november.acl.dao.RedisDao;
import com.november.acl.dao.RoleAclMapper;
import com.november.acl.dao.RoleAdminMapper;
import com.november.acl.dao.RoleMapper;
import com.november.acl.model.Role;
import com.november.acl.param.RoleParam;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.log.Param.LogParam;
import com.november.log.commons.LogTypeInt;
import com.november.log.service.LogService;
import com.november.model.CacheType;
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

    @Resource
    private LogService logService;

    @Resource(name = "aclRedisDao")
    private RedisDao redisDao;

    public void save(RoleParam param) {
        //  检查是否满足条件
        BeanValidator.check(param);
        //  查看是否存在相同名称
        if (checkExist(param.getRoleName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        //  创建角色
        Role role = Role.builder().roleName(param.getRoleName())
                .remark(param.getRemark()).build();
        //  设置操作的管理员
        if(RequestHolder.getCurrentAdmin() != null){
            role.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }else{
            role.setOperator("admin");
        }
        //  设置为当前时间
        role.setOperateTime(new Date());
        //  插入数据库
        roleMapper.insertSelective(role);
        //  删除缓存的键
        redisDao.delKey(CacheType.LIST_PREFIX+"role");
        //  添加入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ROLE_TYPE).targetId(role.getId())
                .remark("添加角色").newValue(JsonMapper.obj2String(role)).build());
    }

    @Transactional
    public void update(RoleParam param) {
        //  检查是否满足条件
        BeanValidator.check(param);
        //  检查是否存在相同名称
        if (checkExist(param.getRoleName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        //  获得更新前的role
        Role before = roleMapper.selectByPrimaryKey(param.getId());
        //  检查是否为Null
        Preconditions.checkNotNull(before, "待更新的角色不存在");
        //  创建更新后的role
        Role after = Role.builder().id(param.getId()).roleName(param.getRoleName())
                .remark(param.getRemark()).build();
        //  设置操作的管理员
        if(RequestHolder.getCurrentAdmin() != null){
            after.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }else{
            after.setOperator("admin");
        }
        //  设置为当前时间
        after.setOperateTime(new Date());
        //  更新到数据库
        roleMapper.updateByPrimaryKeySelective(after);
        //  删除缓存的键
        redisDao.delKey(CacheType.LIST_PREFIX+"role");
        //  添加入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ROLE_TYPE).targetId(param.getId())
                .remark("修改角色").oldValue(JsonMapper.obj2String(before)).newValue(JsonMapper.obj2String(after)).build());
    }

    @Override
    @Transactional
    public void delete(int id) {
        //  检查是否满足条件
        BeanValidator.check(id);
        //  根据当前角色id获得已分配的管理员
        List<Integer> adminIdsByRoleId = roleAdminMapper.getAdminIdsByRoleId(id);
        //  判断是否不为空
        if(CollectionUtils.isNotEmpty(adminIdsByRoleId)){
            //  如果不为空抛出异常说明
            throw new ParamException("该角色下还有管理员");
        }
        //  用id查出该角色
        Role role = roleMapper.selectByPrimaryKey(id);
        //  检查是否为空
        Preconditions.checkNotNull(role, "待删除的角色不存在");
        //  删除该角色
        roleMapper.deleteByPrimaryKey(id);
        //  根据角色id获得已分配的权限
        List<Integer> aclIdByRoleId = roleAclMapper.getAclIdByRoleId(id);
        //  删除分配的权限
        roleAclMapper.deleteByRoleId(id);
        //  删除缓存的键
        redisDao.delKey(CacheType.LIST_PREFIX+"role");
        //  添加入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ROLE_TYPE).targetId(id)
                .remark("删除角色").oldValue(JsonMapper.obj2String(role)).build());
        if(CollectionUtils.isNotEmpty(aclIdByRoleId)){
            //  添加入日志
            logService.saveLog(LogParam.builder().type(LogTypeInt.ROLEACL_TYPE).targetId(id)
                    .remark("删除角色分配的权限").oldValue(JsonMapper.obj2String(aclIdByRoleId)).build());
        }
    }

    @Override
    public List<Role> getByIds(List<Integer> ids) {
        //  判断是否为空
        if(CollectionUtils.isEmpty(ids)){
            return Lists.newArrayList();
        }
        List<Role> roles = roleMapper.getByIdList(ids);
        return roles;
    }

    public List<Role> getAll() {
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

}

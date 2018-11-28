package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.november.acl.dao.AclMapper;
import com.november.acl.dao.RedisDao;
import com.november.acl.dao.RoleAclMapper;
import com.november.acl.dto.AclDto;
import com.november.acl.model.Acl;
import com.november.acl.model.OperType;
import com.november.acl.param.AclParam;
import com.november.common.ApplicationContextHelper;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.log.Param.LogParam;
import com.november.log.commons.LogTypeInt;
import com.november.log.service.LogService;
import com.november.model.CacheType;
import com.november.util.BeanValidator;
import com.november.util.JsonMapper;
import com.november.util.PageQuery;
import com.november.util.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("aclService")
public class AclServiceImpl implements AclService {

    @Resource
    private AclMapper aclMapper;

    @Resource
    private RoleAclMapper roleAclMapper;

    @Resource(name = "aclRedisDao")
    private RedisDao redisDao;

    @Resource
    private ShiroService shiroService;

    @Resource
    private LogService logService;

    public boolean checkExist(int parentId, String aclName, Integer id) {
        return aclMapper.countByNameAndParentId(parentId, aclName, id) > 0;
    }

    @Transactional
    public void changeAcl(String idStr, int rid) {
        //  判断传入的id字符串是否是空
        if (StringUtils.isBlank(idStr)) {
            //  查出要记录的数据
            List<Integer> aclIdByRoleId = roleAclMapper.getAclIdByRoleId(rid);
            //  删除rid对应拥有的所有权限点
            roleAclMapper.deleteByRoleId(rid);
            //  插入日志
            logService.saveLog(LogParam.builder().type(LogTypeInt.ROLEACL_TYPE).remark("删除角色分配的权限")
                    .oldValue(JsonMapper.obj2String(aclIdByRoleId)).targetId(rid).build());
            //  终止
            return;
        }
        //  所有id
        String[] ids = idStr.split(",");
        //  父id
        List<Integer> pids = Lists.newArrayList();
        //  子ids
        List<String> zids = Lists.newArrayList();
        //  添加入对应的集合
        for (int i = 0; i < ids.length; i++) {
            //  取出
            if (ids[i].contains("g") || ids[i].contains("f")) {
                if (ids[i].contains("g")) {
                    ids[i] = ids[i].replace("g", "");
                } else {
                    ids[i] = ids[i].replace("f", "");
                }
                pids.add(Integer.valueOf(ids[i]));
            }
            if (ids[i].contains("z")) {
                ids[i] = ids[i].replace("z", "");
                zids.add(ids[i]);
            }
        }
        //  查出要记录的数据
        List<Integer> aclIdByRoleId = roleAclMapper.getAclIdByRoleId(rid);
        //  进行权限点赋予前删除当前拥有权限
        roleAclMapper.deleteByRoleId(rid);
        //  插入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ROLEACL_TYPE).remark("删除角色分配的权限")
                .oldValue(JsonMapper.obj2String(aclIdByRoleId)).targetId(rid).build());
        //  创建权限点集合
        List<Acl> aclsByOperType = Lists.newArrayList();
        //  循环所有子节点id
        for (String zid : zids) {
            //  分割
            String[] strs = zid.split("-");
            //  获得父id
            Integer parId = Integer.parseInt(strs[0]);
            //  根据父id和当前操作得到所有对应的权限点
            aclsByOperType.addAll(aclMapper.getAclsByOperType(Lists.newArrayList(Integer.parseInt(strs[1])), parId));
        }
        //  将权限点集合转化为权限点id集合
        List<Integer> aclIds = aclsByOperType.stream().map(acl -> acl.getId()).collect(Collectors.toList());
        //  判断当前session中的管理员是否为空
        if (RequestHolder.getCurrentAdmin() == null) {
            //  默认admin
            roleAclMapper.batchInsert(aclIds, rid, "admin");
        } else {
            //  有值则当前
            roleAclMapper.batchInsert(aclIds, rid, RequestHolder.getCurrentAdmin().getAdminCode());
        }
        //  插入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ROLEACL_TYPE).remark("分配角色新的权限")
                .targetId(rid).build());
        //  shiro进行权限赋予更新
        if (ApplicationContextHelper.propBean("shiroFilter", ShiroFilterFactoryBean.class) != null) {
            shiroService.updatePermission((ShiroFilterFactoryBean) ApplicationContextHelper.propBean("shiroFilter", ShiroFilterFactoryBean.class));
        }
    }

    @Override
    public List<Acl> getByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return aclMapper.getByIdList(ids);
    }

    @Override
    public List<AclDto> getAll(int rid) {
        //  获得所有目录节点
        List<Acl> acls = aclMapper.getRootAll();
        //  判断是否为空
        if (CollectionUtils.isEmpty(acls)) {
            //  返回新集合
            return Lists.newArrayList();
        }
        //  权限树集合
        List<AclDto> aclDtoList = Lists.newArrayList();
        //  根节点
        List<AclDto> rootAclDto = Lists.newArrayList();
        //  获得所有操作类型
        List<OperType> operTypeList = aclMapper.getOperType();
        //  循环所有目录节点
        for (Acl acl : acls) {
            //  把权限点包装成权限树
            AclDto dto = AclDto.adapt(acl);
            //  权限树集合添加权限树
            aclDtoList.add(dto);
            //  判断当前权限点父id是否是空
            if (acl.getParentId() == null) {
                //  添加入根节点中
                rootAclDto.add(dto);
            }
        }
        //  权限树集合中移除根节点
        aclDtoList.removeAll(rootAclDto);
        //  循环根节点
        for (int i = 0; i < rootAclDto.size(); i++) {
            //  设置根节点中每个节点显示的值
            rootAclDto.get(i).setName(rootAclDto.get(i).getAclName());
            //  设置根节点中每个节点真正的值
            rootAclDto.get(i).setValue("g" + rootAclDto.get(i).getId());
            //  给每个根节点补充子节点
            rootAclDto.get(i).setList(sortTree(rootAclDto.get(i).getId(), aclDtoList, operTypeList, rid, 1));
        }
        //  设置是否默认选中
        setCheckedVal(rootAclDto);
        //  返回根节点
        return rootAclDto;
    }

    public List<AclDto> sortTree(int parentId, List<AclDto> aclDtoList, List<OperType> operTypeList, int rid, Integer level) {
        //  本层level
        int i = level;
        //  要返回的总权限点集合结果
        List<AclDto> dtoList = Lists.newArrayList();
        //  判断传入进来的权限点集合是否不为空
        if (CollectionUtils.isNotEmpty(aclDtoList)) {
            //  获取到权限点集合的父类
            List<Integer> pids = aclDtoList.stream().map(aclDto -> aclDto.getParentId()).collect(Collectors.toList());
            //  判断是否包含传入父id
            if (pids.contains(parentId)) {
                //  循环权限点集合
                for (AclDto dto : aclDtoList) {
                    //  如果权限点id等于传入父id
                    if (dto.getParentId() == parentId) {
                        //  添加入返回结果集合中
                        dtoList.add(dto);
                    }
                }
                //  权限点集合中移除返回结果
                aclDtoList.removeAll(dtoList);
                //  循环返回结果集合
                for (AclDto dto : dtoList) {
                    //  设置权限点显示的值
                    dto.setName(dto.getAclName());
                    //  设置权限点真正的值
                    dto.setValue("f" + dto.getId());
                    //  层级+1
                    level += 1;
                    //  继续处理剩下的权限点
                    dto.setList(sortTree(dto.getId(), aclDtoList, operTypeList, rid, level));
                }
            }
        }
        //  添加操作节点
        dtoList.addAll(addOperType(operTypeList, rid, i, parentId));
        //  返回结果
        return dtoList;
    }

    //  添加操作类型节点
    public List<AclDto> addOperType(List<OperType> operTypeList, int rid, int level, int parentId) {
        //  最后返回的集合
        List<AclDto> dtoList = Lists.newArrayList();
        //  循环操作类型
        for (OperType operType : operTypeList) {
            //  根据父id获得操作集合
            List<Acl> aclOperList = aclMapper.getAclsByOperType(Lists.newArrayList(operType.getId()), parentId);
            //  判断是否为空
            if(CollectionUtils.isEmpty(aclOperList)){
                //  是空的话跳过
                continue;
            }
            //  创建一个权限节点
            AclDto dto = new AclDto();
            //  设置节点显示的值
            dto.setName(operType.getName());
            //  设置节点真正的值
            dto.setValue("z" + parentId + "-" + operType.getId());
            //  根据角色id获得所有拥有的权限点id集合
            List<Integer> aclIdByRoleId = roleAclMapper.getAclIdByRoleId(rid);
            //  判断权限点id集合是否不为空
            if (CollectionUtils.isNotEmpty(aclIdByRoleId)) {
                //  用权限点id集合获取权限点集合
                List<Acl> byIdList = aclMapper.getByIdList(aclIdByRoleId);
                //  循环权限点集合
                for (Acl acl : byIdList) {
                    //  如果是这个的子节点
                    if (parentId == acl.getParentId()) {
                        //  如果操作类型一致
                        if (acl.getOperTypeId() == operType.getId()) {
                            //  设置为默认选中
                            dto.setChecked(true);
                        }
                    }
                }
            }
            //  把权限点添加入权限点集合中
            dtoList.add(dto);
        }
        //  返回权限点集合
        return dtoList;
    }

    //  设置是否默认选中
    public boolean setCheckedVal(List<AclDto> aclDtos) {
        //  全局默认值
        boolean flag = false;
        //  判空
        if (CollectionUtils.isNotEmpty(aclDtos)) {
            //  循环权限节点集合
            for (AclDto aclDto : aclDtos) {
                //  假如不默认选中
                boolean b = false;
                //  如果当前节点选中
                if (aclDto.isChecked()) {
                    b = true;
                }
                //  判断是否还有子集合
                if (CollectionUtils.isNotEmpty(aclDto.getList())) {
                    //  如果还有子集合继续递归
                    b = setCheckedVal(aclDto.getList());
                }
                //  如果是true
                if (b) {
                    //  设置当前为true
                    aclDto.setChecked(true);
                    //  全局为true
                    flag = true;
                }
            }
        }
        //  返回值
        return flag;
    }
}

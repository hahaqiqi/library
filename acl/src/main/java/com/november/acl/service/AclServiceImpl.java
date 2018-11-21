package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.november.acl.dao.AclMapper;
import com.november.acl.dao.RoleAclMapper;
import com.november.acl.dto.AclDto;
import com.november.acl.model.Acl;
import com.november.acl.param.AclParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import com.november.util.PageQuery;
import com.november.util.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        Acl acl = Acl.builder().aclName(param.getName()).parentId(param.getParentId()).url(param.getUrl())
                .status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setOperator("admin");       //  TODO
        acl.setOperateTime(new Date());
        aclMapper.insertSelective(acl);
//        sysLogService.saveAclLog(null, acl);      //  TODO
    }

    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        Acl before = aclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");
        Acl after = Acl.builder().id(param.getId()).aclName(param.getName()).parentId(param.getParentId()).url(param.getUrl())
                .status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setOperator("admin");        //   TODO
        after.setOperateTime(new Date());
        aclMapper.updateByPrimaryKeySelective(after);
//        sysLogService.saveAclLog(before, after);      //  TODO
    }

    public boolean checkExist(int parentId, String aclName, Integer id) {
        return aclMapper.countByNameAndParentId(parentId, aclName, id) > 0;
    }


    public PageResult<Acl> getPageByAclModuleId(int parentId, PageQuery page) {
        BeanValidator.check(page);
        int count = aclMapper.countByParentId(parentId);
        if (count > 0) {
            List<Acl> aclList = aclMapper.getPageByParentId(parentId, page);
            return PageResult.<Acl>builder().data(aclList).total(count).pageNo(page.getPageNo())
                    .pageSize(page.getPageSize()).build();
        }
        return PageResult.<Acl>builder().pageNo(page.getPageNo()).pageSize(page.getPageSize()).build();
    }

    @Override
    public List<AclDto> getAll(int rid) {
        //  先得到所有权限点
        List<Acl> all = aclMapper.getAll();
        //  处理成树
        List<AclDto> aclTree = getAclTree(all,rid);
        return aclTree;
    }

    //  进行排序得到树
    private List<AclDto> getAclTree(List<Acl> acls,int rid) {
        //  判空
        if (CollectionUtils.isEmpty(acls)) {
            return Lists.newArrayList();
        }
        //  获得根权限
        List<AclDto> rootAclDtoList = Lists.newArrayList();
        //  创建所有权限树
        List<AclDto> aclDtoList = Lists.newArrayList();
        //  转化
        for (Acl acl : acls) {
            aclDtoList.add(AclDto.adapt(acl));
        }
        //  得到角色对应的所有权限id
        List<Integer> aclIdByRoleId = roleAclMapper.getAclIdByRoleId(rid);
        //  储存用完的Dto
        Set<AclDto> removeAcls = Sets.newHashSet();
        for (AclDto dto : aclDtoList) {
            //  判断是否是根节点
            if (null == dto.getParentId() || 0 == dto.getParentId()) {
                //  设置页面显示的名称
                dto.setName(dto.getAclName());
                //  设置页面的值
                dto.setValue(dto.getId().toString());
                //  加入根节点中
                rootAclDtoList.add(AclDto.adapt(dto));
                //  从总权限集合中删除
                removeAcls.add(dto);
            }
        }
        //  删除所有处理过的权限
        aclDtoList.removeAll(removeAcls);
        //  排序
        rootAclDtoList.sort((x, y) -> x.getSeq() - y.getSeq());
        for (AclDto dto : rootAclDtoList) {
            //  是否拥有该权限
            if(aclIdByRoleId.contains(dto.getId())){
                dto.setChecked(true);
                aclIdByRoleId.remove(dto.getId());
                //  如果父节点有则子节点也有
                dto.setList(sortAclDtoList(dto.getId(),aclDtoList,true,aclIdByRoleId));
            }else{
                dto.setList(sortAclDtoList(dto.getId(),aclDtoList,false,aclIdByRoleId));
            }
        }
        return rootAclDtoList;
    }

    //  循环处理所有子集合
    private List<AclDto> sortAclDtoList(int parentId, List<AclDto> aclDtoList,boolean isAllChecked,List<Integer> aclIdsByRid) {
        //  判空
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        //  该parentId子权限树集合
        List<AclDto> childAclDto = Lists.newArrayList();
        //  需要删除的权限树集合
        Set<AclDto> removeAclDto = Sets.newHashSet();
        for (AclDto dto : aclDtoList) {
            //  是否是这个父亲的儿子
            if (parentId == dto.getParentId()) {
                if(isAllChecked){
                    dto.setChecked(true);
                    aclIdsByRid.remove(dto.getId());
                }else{
                    if(aclIdsByRid.contains(dto.getId())){
                        dto.setChecked(true);
                        aclIdsByRid.remove(dto.getId());
                    }
                }
                dto.setName(dto.getAclName());
                dto.setValue(dto.getId().toString());
                childAclDto.add(dto);
                removeAclDto.add(dto);
            }
        }
        //  删除处理过的权限树
        aclDtoList.removeAll(removeAclDto);
        if (CollectionUtils.isNotEmpty(aclDtoList)) {
            for (AclDto dto : childAclDto) {
                if(aclIdsByRid.contains(dto.getId())){
                    dto.setChecked(true);
                    aclIdsByRid.remove(dto.getId());
                    dto.setList(sortAclDtoList(dto.getId(),aclDtoList,true,aclIdsByRid));
                }else{
                    dto.setList(sortAclDtoList(dto.getId(),aclDtoList,false,aclIdsByRid));
                }
            }
        }
        //  排序
        childAclDto.sort((x, y) -> x.getSeq() - y.getSeq());
        return childAclDto;
    }

    @Transactional
    public void changeAcl(String idStr,int rid){
        if(StringUtils.isBlank(idStr)){
            roleAclMapper.deleteByRoleId(rid);
            return;
        }
        String[] strs = StringUtils.split(idStr, ",");
        List<Integer> aclIds = Lists.newArrayList(Arrays.stream(strs).map(s -> Integer.valueOf(s)).collect(Collectors.toList()));
        //  获得rid的所有权限
        List<Integer> aclIdByRoleId = roleAclMapper.getAclIdByRoleId(rid);
        if(aclIds.size() == aclIdByRoleId.size()){
            if(aclIdByRoleId.containsAll(aclIds)){
                return;
            }
        }
        roleAclMapper.deleteByRoleId(rid);
        batchRoleAcl(aclIds,rid);
    }

    private void batchRoleAcl(List<Integer> ids,int rid){
        roleAclMapper.batchInsert(ids,rid,"admin");     //  TODO
    }

}

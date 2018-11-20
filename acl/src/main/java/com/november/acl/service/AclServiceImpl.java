package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.november.acl.dao.AclMapper;
import com.november.acl.model.Acl;
import com.november.acl.param.AclParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import com.november.util.PageQuery;
import com.november.util.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("aclService")
public class AclServiceImpl implements AclService {

    @Resource
    private AclMapper aclMapper;

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
    public List<Acl> getAll() {
        List<Acl> all = aclMapper.getAll();
        List<Acl> aclTree = getAclTree(all);
        return null;
    }

    //  进行排序得到树
    private List<Acl> getAclTree(List<Acl> acls) {
        //  判空
        if (CollectionUtils.isEmpty(acls)) {
            return Lists.newArrayList();
        }
        //  获得根权限
        List<Acl> rootAclList = Lists.newArrayList();
        Set<Acl> removeAcls = Sets.newHashSet();
        for (Acl acl : acls) {
            if (null == acl.getParentId() || 0 == acl.getParentId()) {
                rootAclList.add(acl);
                //  从总权限集合中删除
                removeAcls.add(acl);
            }
        }
        acls.removeAll(removeAcls);
        rootAclList.sort((x, y) -> x.getSeq() - y.getSeq());
        for (Acl acl : rootAclList) {
            acl.setList(sortAclList(acl.getId(), acls));
        }
        return rootAclList;
    }

    private List<Acl> sortAclList(int parentId, List<Acl> aclList) {
        if (CollectionUtils.isEmpty(aclList)) {
            return Lists.newArrayList();
        }
        List<Acl> childAcl = Lists.newArrayList();
        Set<Acl> removeAcls = Sets.newHashSet();
        for (Acl acl : aclList) {
            if (parentId == acl.getParentId()) {
                childAcl.add(acl);
                removeAcls.add(acl);
            }
        }
        aclList.removeAll(removeAcls);
        if (CollectionUtils.isNotEmpty(aclList)) {
            for (Acl acl : childAcl) {
                acl.setList(sortAclList(acl.getId(), aclList));
            }
        }
        childAcl.sort((x, y) -> x.getSeq() - y.getSeq());
        return childAcl;
    }
}

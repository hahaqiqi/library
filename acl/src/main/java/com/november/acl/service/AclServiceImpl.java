package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.november.acl.dao.AclMapper;
import com.november.acl.model.Acl;
import com.november.acl.param.AclParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import com.november.util.PageQuery;
import com.november.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("aclService")
public class AclServiceImpl implements AclService {

    @Resource
    private AclMapper aclMapper;

    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        Acl acl = Acl.builder().aclName(param.getName()).parentId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setOperator("admin");       //  TODO
        acl.setOperateTime(new Date());
        aclMapper.insertSelective(acl);
//        sysLogService.saveAclLog(null, acl);      //  TODO
    }

    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        Acl before = aclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");
        Acl after = Acl.builder().id(param.getId()).aclName(param.getName()).parentId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
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
}

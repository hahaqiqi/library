package com.november.acl.service;

import com.google.common.base.Preconditions;
import com.november.acl.dao.AclModuleMapper;
import com.november.acl.model.AclModule;
import com.november.acl.param.AclModuleParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import com.november.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("AclModuleService")
public class AclModuleServiceImpl implements AclModuleService {

    @Resource
    private AclModuleMapper aclModuleMapper;

    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getAclModuleName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        AclModule aclModule = AclModule.builder().aclModuleName(param.getAclModuleName()).parentId(param.getParentId()).seq(param.getSeq())
                .status(param.getStatus()).remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        aclModule.setOperator("admin");     //  TODO : 待登录
        aclModule.setOperateTime(new Date());
        aclModuleMapper.insertSelective(aclModule);
//        sysLogService.saveAclModuleLog(null, aclModule);
    }

    @Transactional
    public void update(AclModuleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getParentId(), param.getAclModuleName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        AclModule before = aclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");
        AclModule after = AclModule.builder().id(param.getId()).aclModuleName(param.getAclModuleName()).parentId(param.getParentId()).seq(param.getSeq())
                .status(param.getStatus()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator("admin"); //  TODO
        after.setOperateTime(new Date());
        updateWithChild(before, after);
//        sysLogService.saveAclModuleLog(before, after);    //  TODO
    }

    private void updateWithChild(AclModule before, AclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<AclModule> aclModuleList = aclModuleMapper.getChildAclModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (AclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                aclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        aclModuleMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return aclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevel(Integer aclModuleId) {
        AclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }

   /* public void delete(int aclModuleId) {
        AclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在，无法删除");
        if(aclModuleMapper.countByParentId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有子模块，无法删除");
        }
        if (aclMapper.countByAclModuleId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有用户，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }*/

}

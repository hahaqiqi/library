package com.november.acl.dao;

import com.november.acl.model.AclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AclModuleMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(AclModule record);

    int insertSelective(AclModule record);

    AclModule selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(AclModule record);

    int updateByPrimaryKey(AclModule record);

    int countByNameAndParentId(@Param("parentId") Integer parentId,@Param("aclModuleName") String aclModuleName,@Param("aclModuleId") Integer aclModuleId);

    List<AclModule> getChildAclModuleListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("aclModuleList") List<AclModule> aclModuleList);

    List<AclModule> getAllAclModule();

    int countByParentId(@Param("aclModuleId") int aclModuleId);
}
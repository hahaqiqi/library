package com.november.acl.dao;


import com.november.acl.model.RoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleAclMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int deleteByRoleId(@Param("rid") int rid);

    int insert(RoleAcl record);

    int insertSelective(RoleAcl record);

    RoleAcl selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(RoleAcl record);

    int updateByPrimaryKey(RoleAcl record);

    List<Integer> getAclIdByRoleId(@Param("rid") int rid);

    void batchInsert(@Param("ids") List<Integer> ids,@Param("rid") int rid,@Param("operator") String operator);
}
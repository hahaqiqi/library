package com.november.acl.dao;


import com.november.acl.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> getAll();

    int countByAclName(@Param("aclName") String aclName, @Param("id") Integer id);

    List<Role> getByIdList(@Param("idList") List<Integer> idList);
}
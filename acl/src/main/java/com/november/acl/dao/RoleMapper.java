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

    int countByRoleName(@Param("roleName") String roleName, @Param("id") Integer id);

    List<Role> getByIdList(@Param("idList") List<Integer> idList);
}
package com.november.acl.dao;

import org.apache.ibatis.annotations.Param;
import com.november.acl.model.RoleAdmin;

import java.util.List;

public interface RoleAdminMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int deleteByRoleId(@Param("rid") int rid);

    int deleteByAdminId(@Param("adminId") int adminId);

    int insert(RoleAdmin record);

    int insertSelective(RoleAdmin record);

    RoleAdmin selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(RoleAdmin record);

    int updateByPrimaryKey(RoleAdmin record);

    List<Integer> getAdminIdsByRoleId(@Param("rid") int rid);

    void batchInsert(@Param("ids") List<Integer> ids,@Param("rid") int rid,@Param("operator") String operator);

    int countByAdminId(@Param("adminId") int adminId);
}
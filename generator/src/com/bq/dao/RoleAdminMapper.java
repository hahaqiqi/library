package com.bq.dao;

import com.bq.model.RoleAdmin;

public interface RoleAdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleAdmin record);

    int insertSelective(RoleAdmin record);

    RoleAdmin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleAdmin record);

    int updateByPrimaryKey(RoleAdmin record);
}
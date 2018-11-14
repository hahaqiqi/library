package com.bq.dao;

import com.bq.model.AclModule;

public interface AclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AclModule record);

    int insertSelective(AclModule record);

    AclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AclModule record);

    int updateByPrimaryKey(AclModule record);
}
package com.november.acl.dao;

import com.november.acl.model.Acl;

public interface AclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Acl record);

    int insertSelective(Acl record);

    Acl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Acl record);

    int updateByPrimaryKey(Acl record);
}
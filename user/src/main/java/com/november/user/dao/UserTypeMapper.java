package com.november.user.dao;

import com.november.user.model.UserType;

public interface UserTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserType record);

    int insertSelective(UserType record);

    UserType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserType record);

    int updateByPrimaryKey(UserType record);
}
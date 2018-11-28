package com.november.user.service;

import com.november.user.model.UserType;
import com.november.user.param.UserTypeParam;

import java.util.List;

public interface UserTypeService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserType record);

    int insertSelective(UserTypeParam record);

    UserType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserTypeParam record);

    int updateByPrimaryKey(UserType record);

    UserType selectUsertypeByScore(Integer score);

    List<UserType> selectUsertypeByScore();
}

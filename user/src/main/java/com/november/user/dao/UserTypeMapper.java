package com.november.user.dao;

import com.november.user.model.UserType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserType record);

    int insertSelective(UserType record);

    UserType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserType record);

    int updateByPrimaryKey(UserType record);

    List<UserType> selectUsertypeByScore();


}
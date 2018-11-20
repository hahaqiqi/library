package com.november.user.dao;

import com.november.user.model.User;
import com.november.user.param.UserParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    List<User> select(@Param("page") int page, @Param("limit")int limit);

    int updateByPrimaryKeySelective(UserParam record);

    int updateByPrimaryKey(User record);

    int userCount();
}
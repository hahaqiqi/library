package com.november.user.dao;

import com.november.user.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int selectUserName(@Param("userName") String userName,@Param("id") Integer id);

    int selectIdCard(@Param("idCard")String idCard,@Param("id")Integer id);

    List<User> select(@Param("page") int page, @Param("limit")int limit);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int userCount();

    User selectUserByEmail(String email);

    int selectEmail(@Param("email")String email,@Param("id") Integer id);
}
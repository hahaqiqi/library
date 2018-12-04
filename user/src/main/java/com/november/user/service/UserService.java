package com.november.user.service;

import com.november.user.model.User;
import com.november.user.param.UserParam;

import java.util.List;

public interface UserService {
   public int deleteByPrimaryKey(Integer id);

    public int insert(User record);

    public int insertSelective(UserParam record);

    public User selectByPrimaryKey(Integer id);

    List<User> select( int page, int limit);

    public int updateByPrimaryKeySelective(UserParam record);

    public int updateByPrimaryKey(User record);

    public int userCount();

    public User selectUserByEmail(String userEmail);

    int selectEmail(String userEmail,Integer id);

    int updateScore(Integer id,double price);
}

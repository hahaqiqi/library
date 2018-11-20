package com.november.user.service;

import com.november.user.dao.UserMapper;
import com.november.user.model.User;
import com.november.user.param.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

   public int deleteByPrimaryKey(Integer id){
        return userMapper.deleteByPrimaryKey(id);
    }

    public int insert(User record){
        return userMapper.insert(record);
    }

    public int insertSelective(User record){
       return userMapper.insertSelective(record);
    }

    public  User selectByPrimaryKey(Integer id){
       return userMapper.selectByPrimaryKey(id);
    }

    public List<User> select( int page, int limit){
        return  userMapper.select((page-1)*limit,limit);
    }

    public  int updateByPrimaryKeySelective(UserParam record){
       return userMapper.updateByPrimaryKeySelective(record);
    }

    public  int updateByPrimaryKey(User record){
       return userMapper.updateByPrimaryKey(record);
    }

    public int userCount(){ return  userMapper.userCount(); }

}

package com.november.user.service;

import com.november.user.dao.UserTypeMapper;
import com.november.user.model.UserType;
import com.november.user.param.UserTypeParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userTypeService")
public class UserTypeServiceImpl implements UserTypeService {
    @Autowired
    private UserTypeMapper userTypeMapper;

    public  int deleteByPrimaryKey(Integer id){
        return userTypeMapper.deleteByPrimaryKey(id);
    }

    public int insert(UserType record){
        return userTypeMapper.insert(record);
    }

    public int insertSelective(UserTypeParam param){
        UserType userType=UserType.builder().build();
        return userTypeMapper.insertSelective(userType);
    }

    public UserType selectByPrimaryKey(Integer id){
        return userTypeMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(UserType record){
        return  userTypeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(UserType record){
        return userTypeMapper.updateByPrimaryKey(record);
    }

    public UserType selectUsertypeByScore(Integer score){
        List<UserType> list= userTypeMapper.selectUsertypeByScore();
        UserType userType=null;
        for(UserType li:list){
            if(score>=li.getMinScore()) {
                userType = li;
            }
        }

        return userType;
    }
}

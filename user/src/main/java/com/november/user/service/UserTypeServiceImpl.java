package com.november.user.service;

import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.user.dao.UserTypeMapper;
import com.november.user.model.UserType;
import com.november.user.param.UserTypeParam;
import com.november.util.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        BeanValidator.check(param);
        UserType userType=UserType.builder()
                .typeName(param.getTypeName())
                .minScore(param.getMinScore())
                .discount(param.getDiscount())
                .operateTime(new Date())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        if(userTypeMapper.selectTypeName(param.getTypeName(),null)>0){
            throw new ParamException("已经有相同的用户类型");
        }

        if(userTypeMapper.selectScore(param.getMinScore(),null)>0){
            throw new ParamException("该最小积分已被占用");
        }

        if(RequestHolder.getCurrentAdmin()!=null){
            userType.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        else {
            userType.setOperator("admin");
        }
        return userTypeMapper.insertSelective(userType);
    }

    public UserType selectByPrimaryKey(Integer id){
        return userTypeMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(UserTypeParam param){
        BeanValidator.check(param);
        UserType userType=UserType.builder()
                .id(param.getId())
                .typeName(param.getTypeName())
                .minScore(param.getMinScore())
                .discount(param.getDiscount())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        if(userTypeMapper.selectTypeName(param.getTypeName(),param.getId())>0){
            throw new ParamException("已经有相同的用户类型");
        }

        if(userTypeMapper.selectScore(param.getMinScore(),param.getId())>0){
            throw new ParamException("该最小积分已被占用");
        }
        return  userTypeMapper.updateByPrimaryKeySelective(userType);
    }

    public int updateByPrimaryKey(UserType record){
        return userTypeMapper.updateByPrimaryKey(record);
    }

    public UserType selectUsertypeByScore(Integer score){//用户积分得到用户类型
        List<UserType> list= userTypeMapper.selectUsertypeByScore();
        UserType userType=null;
        for(UserType li:list){//积分降序遍历
            if(score>=li.getMinScore()) {//若用户积分大于该类型最小积分，则为该类型
                userType = li;
                break;
            }
        }
        return userType;
    }

    public List<UserType> selectUsertypeByScore(){
        return userTypeMapper.selectUsertypeByScore();
    }
}

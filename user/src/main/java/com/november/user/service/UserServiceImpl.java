package com.november.user.service;

import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.user.dao.UserMapper;
import com.november.user.model.User;
import com.november.user.param.UserParam;
import com.november.util.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public int insertSelective(UserParam param){
        BeanValidator.check(param);
        User user=User.builder().userBirthday(param.getUserBirthday())
                .userName(param.getUserName())
                .userPhone(param.getUserPhone())
                .userEmail(param.getUserEmail())
                .userScore(param.getUserScore())
                .userBalance(param.getUserBalance())
                .idCard(param.getIdCard())
                .remark(param.getRemark())
                .build();

        //查询用户是否同名
        if(userMapper.selectUserName(param.getUserName(),null)>0){
            throw new ParamException("已经有相同的用户名");
        }
        //查询身份证是否相同
        if(userMapper.selectIdCard(param.getIdCard(),null)>0){
            throw new ParamException("已经有相同的身份证号码");
        }

        if(RequestHolder.getCurrentAdmin()!=null){
            user.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        else {
            user.setOperator("admin");
        }
        user.setOperateTime(new Date());
        user.setStatus(1);


        return userMapper.insertSelective(user);
    }

    public  User selectByPrimaryKey(Integer id){
       return userMapper.selectByPrimaryKey(id);
    }

    public List<User> select( int page, int limit){
        return  userMapper.select((page-1)*limit,limit);
    }

    public  int updateByPrimaryKeySelective(UserParam param){
        BeanValidator.check(param);
        User user= User.builder().id(param.getId())
                .userName(param.getUserName())
                .userEmail(param.getUserEmail())
                .userPhone(param.getUserPhone())
                .userScore(param.getUserScore())
                .userBalance(param.getUserBalance())
                .idCard(param.getIdCard())
                .userBirthday(param.getUserBirthday())
                .remark(param.getRemark())
                .status(param.getStatus())
                .build();

        if(userMapper.selectUserName(param.getUserName(),param.getId())>0){
            throw new ParamException("已经有相同的用户名");
        }

        if(userMapper.selectIdCard(param.getIdCard(),param.getId())>0){
            throw new ParamException("已经有相同的身份证号码");
        }

        if(userMapper.selectEmail(param.getUserEmail())>0){
            throw new ParamException("该邮箱已注册");
        }
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public  int updateByPrimaryKey(User record){
       return userMapper.updateByPrimaryKey(record);
    }

    public int userCount(){ return  userMapper.userCount(); }

    public User selectUserByEmail(String userEmail){
        BeanValidator.check(userEmail);
       return  userMapper.selectUserByEmail(userEmail);
    }

}

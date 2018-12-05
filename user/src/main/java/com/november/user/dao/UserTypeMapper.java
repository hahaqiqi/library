package com.november.user.dao;

import com.november.user.model.UserType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserType record);

    int insertSelective(UserType record);

    int selectTypeName(@Param("TypeName") String TypeName, @Param("id") Integer id);//查类型

    UserType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserType record);

    int updateByPrimaryKey(UserType record);

    List<UserType> selectUsertypeByScore();//查询所有

    int selectScore(@Param("score") Integer score, @Param("id") Integer id);//查积分

}
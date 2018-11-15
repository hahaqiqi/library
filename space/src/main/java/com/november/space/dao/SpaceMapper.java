package com.november.space.dao;

import com.november.space.model.Space;
import org.apache.ibatis.annotations.Param;

public interface SpaceMapper {

    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Space record);

    int insertSelective(Space record);

    Space selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Space record);

    int updateByPrimaryKey(Space record);
}
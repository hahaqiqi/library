package com.november.space.dao;


import com.november.space.model.Space;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLClientInfoException;
import java.util.List;

public interface SpaceMapper {

    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Space record);

    int insertSelective(Space record);

    Space selectByPrimaryKey(@Param("id") Integer id);

    int selectByparentid(@Param("parentid") Integer parentid, @Param("parentName") String parentName);

    List<Space> selectList(@Param("parentId") Integer parentId);

    int updateByPrimaryKeySelective(Space record);

    int updateByPrimaryKey(Space record);

}
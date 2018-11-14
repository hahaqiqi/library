package com.bq.dao;

import com.bq.model.Space;

public interface SpaceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Space record);

    int insertSelective(Space record);

    Space selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Space record);

    int updateByPrimaryKey(Space record);
}
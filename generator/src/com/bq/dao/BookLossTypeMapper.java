package com.bq.dao;

import com.bq.model.BookLossType;

public interface BookLossTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookLossType record);

    int insertSelective(BookLossType record);

    BookLossType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookLossType record);

    int updateByPrimaryKey(BookLossType record);
}
package com.bq.dao;

import com.bq.model.BookLeaseType;

public interface BookLeaseTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookLeaseType record);

    int insertSelective(BookLeaseType record);

    BookLeaseType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookLeaseType record);

    int updateByPrimaryKey(BookLeaseType record);
}
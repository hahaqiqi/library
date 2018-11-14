package com.bq.dao;

import com.bq.model.BookLease;

public interface BookLeaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookLease record);

    int insertSelective(BookLease record);

    BookLease selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookLease record);

    int updateByPrimaryKey(BookLease record);
}
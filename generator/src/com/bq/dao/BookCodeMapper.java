package com.bq.dao;

import com.bq.model.BookCode;

public interface BookCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookCode record);

    int insertSelective(BookCode record);

    BookCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookCode record);

    int updateByPrimaryKey(BookCode record);
}
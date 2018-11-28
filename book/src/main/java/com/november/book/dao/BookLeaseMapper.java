package com.november.book.dao;

import com.november.book.model.BookLease;
import org.apache.ibatis.annotations.Param;

public interface BookLeaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookLease record);

    int insertSelective(BookLease record);

    BookLease selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookLease record);

    int updateByPrimaryKey(BookLease record);

    int selectBookLeaseCountByBookId(@Param("bookId") Integer bookId);
}
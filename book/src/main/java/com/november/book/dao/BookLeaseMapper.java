package com.november.book.dao;

import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookLeaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookLease record);

    int insertSelective(BookLease record);

    BookLease selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookLease record);

    int updateByPrimaryKey(BookLease record);

    List<BookLease> pageListBookLease(@Param("page") int page,@Param("limit") int limit);

    int selectBookLeaseCountByBookId(@Param("bookId") Integer bookId);

    BookLease selectBookLeaseByBookidOne(Integer bookId);

    List<BookLease> getAll();

    List<BookLease> selectByUserIding(@Param("userId") Integer userId);

    List<BookLease> selectByParam(BookLeaseParam record);

    int getCount(BookLeaseParam record);

}
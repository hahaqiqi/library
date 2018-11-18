package com.november.book.dao;

import com.november.book.model.BookLossType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookLossTypeMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(BookLossType record);

    int insertSelective(BookLossType record);

    BookLossType selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(BookLossType record);

    int updateByPrimaryKey(BookLossType record);

    int checkBookLossTypeName(@Param("bookLossBookTypeName") String bookLossBookTypeName,@Param("id") Integer id);

    List<BookLossType> getBookLossType(@Param("id")Integer id);
}
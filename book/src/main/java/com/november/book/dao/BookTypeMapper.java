package com.november.book.dao;

import com.november.book.model.BookType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookTypeMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(BookType record);

    int insertSelective(BookType record);

    BookType selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(BookType record);

    int updateByPrimaryKey(BookType record);

    int checkBookTypeName(@Param("bookTypeName")String bookTypeName,@Param("id") Integer id);

    List<BookType> selectAll();

    List<BookType> pageSelect(@Param("page") int page,@Param("limit")int limit);

    int bacthDelectBookType(List<Integer> list);

    int bookTypeCount();
}
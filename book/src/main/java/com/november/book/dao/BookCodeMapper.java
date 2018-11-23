package com.november.book.dao;

import com.november.book.model.BookCode;
import com.november.book.model.BookCode;
import com.november.book.param.BookCodeParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookCode record);

    int insertSelective(BookCode record);

    BookCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookCode record);

    int updateByPrimaryKey(BookCode record);

    int count();

    List<BookCode> getAll();

    List<BookCode> pageSelect(@Param("page") int page, @Param("limit")int limit);

    int isRepeat(BookCodeParam bookCodeParam);

    int minIsEnabled(BookCodeParam bookCodeParam);

    int maxIsEnabled(BookCodeParam bookCodeParam);
}
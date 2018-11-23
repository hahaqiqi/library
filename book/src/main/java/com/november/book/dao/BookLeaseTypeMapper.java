package com.november.book.dao;

import com.november.book.model.BookLeaseType;
import com.november.book.param.BookLeaseTypeParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookLeaseTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookLeaseType record);

    int insertSelective(BookLeaseType record);

    BookLeaseType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookLeaseType record);

    int updateByPrimaryKey(BookLeaseTypeParam record);

    int count();

    List<BookLeaseType> pageList(@Param("page") Integer page, @Param("limit")Integer limit);

    int selectByTypeName(BookLeaseTypeParam param);
}
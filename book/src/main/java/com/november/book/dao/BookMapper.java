package com.november.book.dao;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Book record);

    int insertSelective(Book record);

    Book selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);

    int batchInsert(List<Book> listBook);

    int bacthDelete(List<Integer> list);

    int count();

    List<Book> list();

    List<Book> pageList(@Param("page") int page, @Param("limit")int limit);

    List<Book> filtrateSelect(BookParam bookParam);

    List<Book> filtrateSelectAllId(BookParam bookParam);

    int changeBookStatus(@Param("id") Integer id,@Param("statusId") Integer statusId);

    int bacthUpdateAll(BookParam param);

    int bacthUpdateWhere(BookParam param);

    List<Book> getBookByCode(List<String> list);
}
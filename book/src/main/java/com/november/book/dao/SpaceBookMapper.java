package com.november.book.dao;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpaceBookMapper {

    List<Book> selectSpaceBook(BookParam bookParam);//查询空间图书

    int selectSpaceBookList(List<Book> listBy);//判断空间添加的书籍组是否存在

    int Booklimit(List<Integer> bookall);//查询空间图书分页

    int BookSpaceremove(@Param("bookSpaceId") Integer bookSpaceId,@Param("bookId") Integer bookId);//移除某一空间的图书

    int BookSpaceAdd(@Param("bookpid")Integer bookpid,@Param("bookCode")String bookCode,@Param("status")Integer status);//添加书籍到空间

    int BookSpaceAddList(List<Book> listBook);//添加一组书籍到空间
}

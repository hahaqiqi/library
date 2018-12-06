package com.november.book.service;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SpaceBookService {

    List<Book> selectSpaceBook(BookParam bookParam);

    int Booklimit(List<Integer> bookall);

    int BookSpaceremove(Integer bookSpaceId,Integer bookId);

    int BookSpaceAdd(Integer bookpid,String bookCode,Integer status);

    int BookSpaceAddList(List<Book> listBook);//添加一组书籍到空间

}

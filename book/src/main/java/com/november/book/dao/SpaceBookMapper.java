package com.november.book.dao;

import com.november.book.model.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpaceBookMapper {

    List<Book> selectSpaceBook(List<Integer> list);

}

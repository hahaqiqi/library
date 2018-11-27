package com.november.book.service;

import com.november.book.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SpaceBookService {

    List<Book> selectSpaceBook(List<Integer> bookspaceid);

}

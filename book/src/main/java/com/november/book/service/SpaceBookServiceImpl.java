package com.november.book.service;

import com.november.book.dao.SpaceBookMapper;
import com.november.book.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

@Service("spacebookService")
public class SpaceBookServiceImpl implements SpaceBookService{
    @Resource
    private SpaceBookMapper spacebook;

    //获取书籍
    public List<Book> selectSpaceBook(List<Integer> bookspaceid){
        return spacebook.selectSpaceBook(bookspaceid);
    }
}

package com.november.book.service;

import com.november.book.dao.SpaceBook;
import com.november.book.model.Book;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;

@Service("spacebookService")
public class SpaceBookServiceImpl implements SpaceBookService{
    @Resource
    private SpaceBook spacebook;

    //获取书籍
    public List<Book> selectSpaceBook(Integer bookspaceid){
        return spacebook.selectSpaceBook(bookspaceid);
    }
}

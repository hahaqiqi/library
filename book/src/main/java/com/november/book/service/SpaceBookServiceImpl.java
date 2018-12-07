package com.november.book.service;

import com.november.book.dao.SpaceBookMapper;
import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.exception.ParamException;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

@Service("spacebookService")
public class SpaceBookServiceImpl implements SpaceBookService {
    @Resource
    private SpaceBookMapper spacebook;

    //获取书籍
    public List<Book> selectSpaceBook(BookParam bookParam) {
        return spacebook.selectSpaceBook(bookParam);
    }

    public int Booklimit(List<Integer> bookall) {
        return spacebook.Booklimit(bookall);
    }

    public int BookSpaceremove(Integer bookSpaceId, Integer bookId) {
        return spacebook.BookSpaceremove(bookSpaceId, bookId);
    }

    public int BookSpaceAdd(Integer bookpid, String bookCode, Integer status) {
        if (spacebook.BookSpaceAdd(bookpid, bookCode, status) <= 0) {
            throw new ParamException("没有此书籍信息");
        }
        return 1;
    }

    public int BookSpaceAddList(Integer status, Integer bookSpase, List<Book> listBook) {
        /*if(selectSpaceBookListBy(listBook)>listBook.size()){
            throw new ParamException("这组图书中的某本图书重复");
        }else if(selectSpaceBookListBy(listBook)<listBook.size()){
            throw new ParamException("这组图书中的某本图书不存在");
        }*/
        return spacebook.BookSpaceAddList(status, bookSpase, listBook);
    }
    /*
        判断
     */
    /*public int selectSpaceBookListBy(List<Book> listBy){
        return spacebook.selectSpaceBookList(listBy);
    }*/
}

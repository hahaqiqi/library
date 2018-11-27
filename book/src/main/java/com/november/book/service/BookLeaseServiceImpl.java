package com.november.book.service;/*
 *
 **/

import com.november.book.dao.BookLeaseMapper;
import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bookLeaseService")
public class BookLeaseServiceImpl implements BookLeaseService {
    @Resource
    private BookLeaseMapper bookLeaseMapper;

    @Override
    public int saveBookLease(BookLeaseParam param) {
        return 0;
    }

    @Override
    public int updateBookLease(BookLeaseParam param) {
        return 0;
    }

    @Override
    public int deleteBookLease(Integer id) {
        return 0;
    }

    @Override
    public int batchDeleteBookLease(List<Integer> list) {
        return 0;
    }

    @Override
    public int bookLeaseCount() {
        return 0;
    }

    @Override
    public List<BookLease> listBookLease() {
        return null;
    }

    @Override
    public List<BookLease> pageListBookLease(int page, int limit) {
        return null;
    }

    @Override
    public BookLease byIdBookLease(Integer id) {
        return null;
    }

    @Override
    public List<BookLease> selectBookLeaseByBookId(Integer id) {
        return null;
    }

    @Override
    public int selectBookLeaseCountByBookId(Integer id) {
        return bookLeaseMapper.selectBookLeaseCountByBookId(id);
    }
}

package com.november.book.service;/*
 *
 **/

import com.november.book.dao.BookLeaseMapper;
import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;
import com.november.book.util.SerialNumberUtil;
import com.november.common.RequestHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("bookLeaseService")
public class BookLeaseServiceImpl implements BookLeaseService {
    @Resource
    private BookLeaseMapper bookLeaseMapper;

    @Override
    public int saveBookLease(BookLeaseParam param) {
        BookLease bookLease=BookLease.builder()
                .bookPrice(param.getBookPrice())
                .discount(param.getDiscount())
                .bookId(param.getBookId())
                .userId(param.getUserId())
                .remark(param.getRemark())
                .status(1)
                .serialNumber(SerialNumberUtil.getCerialNumber()).build();
        if(RequestHolder.getCurrentAdmin()!=null){
            bookLease.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }else{
            bookLease.setOperator("admin");
        }
        bookLeaseMapper.insertSelective(bookLease);
        return bookLease.getId();
    }

    @Override
    public int updateBookLease(BookLeaseParam param) {
        BookLease bookLease=BookLease.builder()
                .status(param.getStatus())
                .id(param.getId())
                .remark(param.getRemark()).build();
        bookLease.setFinalOperator("admin");
        bookLease.setFinalOperateTime(new Date());
        return bookLeaseMapper.updateByPrimaryKeySelective(bookLease);
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

    @Override
    public BookLease getBookLeaseOne(Integer bookId) {
        return bookLeaseMapper.selectBookLeaseByBookidOne(bookId);
    }

    @Override
    public List<BookLease> getAll() {
        return bookLeaseMapper.getAll();
    }
}

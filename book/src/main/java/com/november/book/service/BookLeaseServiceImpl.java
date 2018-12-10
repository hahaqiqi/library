package com.november.book.service;/*
 *
 **/

import com.november.book.dao.BookLeaseMapper;
import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;
import com.november.book.util.SerialNumberUtil;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("bookLeaseService")
public class BookLeaseServiceImpl implements BookLeaseService {
    @Resource
    private BookLeaseMapper bookLeaseMapper;

    @Override
    public int saveBookLease(BookLeaseParam param) {
        BookLease bookLease = BookLease.builder()
                .bookPrice(param.getBookPrice())
                .discount(param.getDiscount())
                .bookId(param.getBookId())
                .userId(param.getUserId())
                .remark(param.getRemark())
                .status(1)
                .price(param.getPrice())
                .serialNumber(SerialNumberUtil.getCerialNumber()).build();
        if (RequestHolder.getCurrentAdmin() != null) {
            bookLease.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        bookLeaseMapper.insertSelective(bookLease);
        return bookLease.getId();
    }

    @Override
    public int updateBookLease(BookLeaseParam param) {
        BeanValidator.check(param);
        if (Double.isNaN(param.getPrice())) {
            throw new ParamException("数据类型错误");
        }
        BookLease bookLease = BookLease.builder()
                .status(param.getStatus())
                .id(param.getId())
                .price(param.getPrice())
                .remark(param.getRemark())
                .build();
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
        return bookLeaseMapper.pageListBookLease((page - 1) * limit, limit);
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

    @Override
    public List<Integer> selectByUserId(Integer userId) {
        List<BookLease> bookLeases = bookLeaseMapper.selectByUserIding(userId);
        List<Integer> bookIds = new ArrayList<>();
        for (BookLease bookLease : bookLeases) {
            bookIds.add(bookLease.getBookId());
        }
        return bookIds;
    }

    public List<BookLease> selectByParam(BookLeaseParam param) {//带条件的分页展示
        param.setPage((param.getPage() - 1) * param.getLimit());
        return bookLeaseMapper.selectByParam(param);
    }

    public int getCount(BookLeaseParam param) {//得到行数
        return bookLeaseMapper.getCount(param);
    }//得到满足条件的行数
}

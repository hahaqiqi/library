package com.november.book.service;/*
 *
 **/

import com.google.common.base.Preconditions;
import com.november.book.dao.BookCodeMapper;
import com.november.book.model.BookCode;
import com.november.book.param.BookCodeParam;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("bookCodeService")
public class BookCodeServiceImpl implements BookCodeService {
    @Resource
    private BookCodeMapper bookCodeMapper;

    @Override
    public int saveBookCode(BookCodeParam param) {
        BeanValidator.check(param);//检查类
        if (bookCodeMapper.isRepeat(param) > 0) {
            throw new ParamException("已经存在该范围");
        }
        BookCode bookCode = BookCode.builder()
                .bookPrice(param.getBookPrice())
                .bookPriceMax(param.getBookPriceMax())
                .bookPriceMin(param.getBookPriceMin())
                .remark(param.getRemark()).build();
        if (RequestHolder.getCurrentAdmin() != null) {
            bookCode.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        return bookCodeMapper.insertSelective(bookCode);
    }

    @Override
    public int updateBookCode(BookCodeParam param) {
        BeanValidator.check(param);
        //如果是1 则不允许修改范围 将PriceMin值设为0
        if (param.getId() == 1) {
            param.setBookPriceMin(0);
        } else {
            BookCode before = bookCodeMapper.selectByPrimaryKey(param.getId());
            Preconditions.checkNotNull(before, "修改的内容不存在");
            if (bookCodeMapper.isRepeat(param) > 0) {
                throw new ParamException("已经存在该范围");
            }
        }
        BookCode after = BookCode.builder()
                .bookPrice(param.getBookPrice())
                .bookPriceMax(param.getBookPriceMax())
                .bookPriceMin(param.getBookPriceMin())
                .remark(param.getRemark())
                .id(param.getId())
                .build();
        if (RequestHolder.getCurrentAdmin() != null) {
            after.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        return bookCodeMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public int deleteBookCode(Integer id) {
        //如果是1 则不允许删除
        if (id == 1) {
            throw new ParamException("该项不允许删除");
        }
        BookCode before = bookCodeMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "删除的内容不存在");
        return bookCodeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int bookCodeCount() {
        return bookCodeMapper.count();
    }

    @Override
    public List<BookCode> listBookCode() {
        return bookCodeMapper.getAll();
    }

    @Override
    public List<BookCode> pageListBookCode(int page, int limit) {
        return bookCodeMapper.pageSelect((page - 1) * limit, limit);
    }

    @Override
    public BookCode byIdBookCode(Integer id) {
        return bookCodeMapper.selectByPrimaryKey(id);
    }

    @Override
    public BookCode byPriceBookCode(Double price) {
        List<BookCode> listBookCode = bookCodeMapper.getAll();
        BookCode bookCode = null;
        for (int i = listBookCode.size() - 1; i >= 0; i--) {
            if (price >= listBookCode.get(i).getBookPriceMin()) {
                bookCode = listBookCode.get(i);
                break;
            }
        }
        return bookCode;
    }

}

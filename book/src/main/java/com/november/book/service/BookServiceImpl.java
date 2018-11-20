package com.november.book.service;/*
 *
 **/

import com.google.common.base.Preconditions;
import com.november.book.dao.BookMapper;
import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.util.BookCodeUtil;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("bookService")
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;

    @Override
    public int saveBook(BookParam param) {
        BeanValidator.check(param);
        List<Book> listBook=new ArrayList<>(param.getCount());
        for(int i=0;i<param.getCount();i++){
            Book book=Book.builder().bookChcoType(param.getBookChcoType()).bookCode(param.getBookCode())
                    .bookLeaseId(param.getBookLeaseId()).bookLeaseType(param.getBookLeaseType())
                    .bookLossId(param.getBookLossId()).bookName(param.getBookName())
                    .bookSpaceId(param.getBookSpaceId()).bookTypeId(param.getBookTypeId())
                    .authorName(param.getAuthorName()).pressName(param.getPressName())
                    .price(param.getPrice()).remark(param.getRemark()).build();
            book.setOperator("admin");
            book.setOperateTime(new Date());
            book.setBookCode(BookCodeUtil.getBookCode());
            listBook.add(book);
        }
        return bookMapper.batchInsert(listBook);
    }

    @Override
    public int updateBook(BookParam param) {
        BeanValidator.check(param);
        Book before=bookMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "修改的内容不存在");
        Book after=Book.builder().bookChcoType(param.getBookChcoType()).bookCode(param.getBookCode())
                .bookLeaseId(param.getBookLeaseId()).bookLeaseType(param.getBookLeaseType())
                .bookLossId(param.getBookLossId()).bookName(param.getBookName())
                .bookSpaceId(param.getBookSpaceId()).bookTypeId(param.getBookTypeId())
                .authorName(param.getAuthorName()).pressName(param.getPressName())
                .price(param.getPrice()).remark(param.getRemark()).id(param.getId()).build();
        after.setOperator("admin");
        after.setOperateTime(new Date());
        return bookMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public int deleteBook(Integer id) {
        Book before=bookMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "删除的内容不存在");
        return bookMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDeleteBook(List<Integer> list) {
        return bookMapper.bacthDelete(list);
    }

    @Override
    public int bookCount() {
        return bookMapper.count();
    }

    @Override
    public List<Book> listBook() {
        return bookMapper.list();
    }

    @Override
    public List<Book> pageListBook(int page, int limit) {
        return bookMapper.pageList((page-1)*limit,limit);
    }

    @Override
    public Book byIdBook(Integer id) {
        return bookMapper.selectByPrimaryKey(id);
    }
}

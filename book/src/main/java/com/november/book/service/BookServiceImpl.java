package com.november.book.service;/*
 *
 **/

import com.google.common.base.Preconditions;
import com.november.book.dao.BookMapper;
import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.util.BookCodeUtil;
import com.november.common.RequestHolder;
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
        BeanValidator.check(param);//数据基本验证
        List<Book> listBook=new ArrayList<>(param.getCount());//批量增加的容器
        for(int i=0;i<param.getCount();i++){    //存放数据
            Book book=Book.builder().bookChcoType(param.getBookChcoType()).bookCode(param.getBookCode())
                    .bookLeaseId(param.getBookLeaseId()).bookLeaseType(param.getBookLeaseType())
                    .bookLossId(param.getBookLossId()).bookName(param.getBookName())
                    .bookSpaceId(param.getBookSpaceId()).bookTypeId(param.getBookTypeId())
                    .authorName(param.getAuthorName()).pressName(param.getPressName())
                    .price(param.getPrice()).remark(param.getRemark()).build();
            if(RequestHolder.getCurrentAdmin()!=null){
                    book.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
            }else{
                book.setOperator("admin");
            }

            book.setOperateTime(new Date());
            book.setBookCode(BookCodeUtil.getBookCode());
            listBook.add(book);
        }
        return bookMapper.batchInsert(listBook);
    }

    @Override
    public int updateBook(BookParam param) {
        param.setCount(1);
        BeanValidator.check(param);
        Book before=bookMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "修改的内容不存在");
        Book after=Book.builder().bookChcoType(param.getBookChcoType()).bookCode(param.getBookCode())
                .bookLeaseId(param.getBookLeaseId()).bookLeaseType(param.getBookLeaseType())
                .bookLossId(param.getBookLossId()).bookName(param.getBookName())
                .bookSpaceId(param.getBookSpaceId()).bookTypeId(param.getBookTypeId())
                .authorName(param.getAuthorName()).pressName(param.getPressName())
                .price(param.getPrice()).remark(param.getRemark()).id(param.getId()).build();
        if(RequestHolder.getCurrentAdmin()!=null){
            after.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }else{
            after.setOperator("admin");
        }
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
    public List<Book> pageListBook(int page, int limit, BookParam bookParam) {
        if(bookParam==null){    //无条件查询所有
            return bookMapper.pageList((page-1)*limit,limit);
        }else{
            return bookMapper.filtrateSelect(bookParam);
        }
    }

    @Override
    public Book byIdBook(Integer id) {
        return bookMapper.selectByPrimaryKey(id);
    }

    @Override
    public int changeBookStatus(Integer id, Integer statusId) {
        return bookMapper.changeBookStatus(id,statusId);
    }
}

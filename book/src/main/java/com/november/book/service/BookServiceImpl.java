package com.november.book.service;/*
 *
 **/

import com.google.common.base.Preconditions;
import com.november.book.dao.BookLeaseMapper;
import com.november.book.dao.BookMapper;
import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.util.BookCodeUtil;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
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
    @Resource
    private BookLeaseMapper bookLeaseMapper;

    @Override
    public int saveBook(BookParam param) {
        BeanValidator.check(param);//数据基本验证
        List<Book> listBook = new ArrayList<>(param.getCount());//批量增加的容器
        for (int i = 0; i < param.getCount(); i++) {    //存放数据
            Book book = Book.builder().bookChcoType(param.getBookChcoType()).bookCode(param.getBookCode())
                    .bookLeaseId(param.getBookLeaseId()).bookLeaseType(param.getBookLeaseType())
                    .bookLossId(param.getBookLossId()).bookName(param.getBookName())
                    .bookSpaceId(param.getBookSpaceId()).bookTypeId(param.getBookTypeId())
                    .authorName(param.getAuthorName()).pressName(param.getPressName())
                    .price(param.getPrice()).remark(param.getRemark()).build();
            if (RequestHolder.getCurrentAdmin() != null) {
                book.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
            } else {
                book.setOperator("admin");
            }
            //默认未上架
            book.setStatus(0);
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
        Book before = bookMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "修改的内容不存在");
        Book after = Book.builder().bookChcoType(param.getBookChcoType()).bookCode(param.getBookCode())
                .bookLeaseId(param.getBookLeaseId()).bookLeaseType(param.getBookLeaseType())
                .bookLossId(param.getBookLossId()).bookName(param.getBookName())
                .bookSpaceId(param.getBookSpaceId()).bookTypeId(param.getBookTypeId())
                .authorName(param.getAuthorName()).pressName(param.getPressName())
                .price(param.getPrice()).remark(param.getRemark()).id(param.getId()).build();
        if (RequestHolder.getCurrentAdmin() != null) {
            after.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        } else {
            after.setOperator("admin");
        }
        after.setOperateTime(new Date());
        return bookMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public int deleteBook(Integer id) {
        Book before = bookMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "删除的内容不存在");

/*        if (before.getBookLeaseId() != null && before.getBookLeaseId() != 0) {
            //如果书籍正在被借阅，不允许删除和更改状态
            throw new ParamException("书籍借阅中,不允许删除");
        }*/
        if (bookLeaseMapper.selectBookLeaseCountByBookId(id) > 0) {
            //有过订单不能从数据库删除
            //则将book status 改为 2(永久下架)
            return bookMapper.changeBookStatus(id, 2);
        }
        return bookMapper.deleteByPrimaryKey(id);//直接从数据库删除
    }

    @Override
    public int batchDeleteBook(List<Integer> list) {
        List<Integer> scList = new ArrayList<>();
        int count = 0;
        for (Integer i : list) {
            Book before = bookMapper.selectByPrimaryKey(i);
            if (before.getBookLeaseId() != null && before.getBookLeaseId() != 0) {
                //如果书籍正在被借阅，不允许删除和更改状态
                continue;
            }
            if (bookLeaseMapper.selectBookLeaseCountByBookId(i) > 0) {
                count = count + bookMapper.changeBookStatus(i, 2);
            } else {
                scList.add(i);
            }
        }
        count = count + bookMapper.bacthDelete(scList);
        return count;
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
        return bookMapper.pageList((page - 1) * limit, limit);
    }

    @Override
    public List<Book> pageListBook(Integer page, Integer limit, BookParam bookParam) {
        if (bookParam == null) {    //无条件查询所有
            if (page == null || limit == null) {
                return bookMapper.pageList(null, null);
            }
            return bookMapper.pageList((page - 1) * limit, limit);
        } else {
            if (page != null && limit != null) {
                bookParam.setPage((page - 1) * limit);
                bookParam.setLimit(limit);
            }else{
                bookParam.setPage(null);
                bookParam.setLimit(null);
            }
            return bookMapper.filtrateSelect(bookParam);
        }
    }

    @Override
    public Book byIdBook(Integer id) {
        return bookMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Book> byIdBooks(List<Integer> list) {
        return bookMapper.selectByBookIds(list);
    }

    @Override
    public int changeBookStatus(Integer id, Integer statusId) {
        return bookMapper.changeBookStatus(id, statusId);
    }

    @Override
    public List<Book> whereListBook(BookParam param) {
        if (param == null) {
            return null;
        } else {
            return bookMapper.filtrateSelectAllId(param);
        }
    }

    @Override
    public int batchUpdate(BookParam param) {
        if (param.getWhereList() == null) {
            return bookMapper.bacthUpdateAll(param);
        } else {
            return bookMapper.bacthUpdateWhere(param);
        }
    }

    @Override
    public List<Book> getBookByCode(List<String> list) {
        if (list.size() <= 0) {
            return null;
        }
        return bookMapper.getBookByCode(list);
    }

    @Override
    public String bookState(Integer id) {
        Book book = bookMapper.selectByPrimaryKey(id);
        if (book.getStatus() == 0) {
            return "未上架";
        }
        if (book.getBookLeaseId() != null) {
            return "被借阅";
        }
        return "可借阅";
    }

    @Override
    public int updateBookLeaseIdByBookId(Integer bookId, Integer leaseId) {
        return bookMapper.updateBookLeaseIdByBookId(bookId, leaseId);
    }
}

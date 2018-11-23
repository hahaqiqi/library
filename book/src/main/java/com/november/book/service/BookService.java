package com.november.book.service;

import com.november.book.model.Book;
import com.november.book.model.BookType;
import com.november.book.param.BookParam;
import com.november.book.param.BookTypeParam;

import java.util.List;

public interface BookService {
    /**
     * 新增
     * @param param
     * @return int
     */
    public int saveBook(BookParam param);

    /**
     * 根据id修改
     * @param param
     * @return int
     */
    public int updateBook(BookParam param);

    /**
     * 根据id删除一列
     * @param id
     * @return int
     */
    public int deleteBook(Integer id);

    /**
     * 批量删除
     * @param list
     * @return int
     */
    public int batchDeleteBook(List<Integer> list);

    /**
     * 得到总数
     * @return
     */
    public int bookCount();

    /**
     * 得到所有
     * @return List<Book>
     */
    public List<Book> listBook();

    /**
     * 普通分页
     * @return
     */
    public List<Book> pageListBook(int page, int limit);

    /**
     * 多条件查询分页
     * @return
     */
    public List<Book> pageListBook(int page, int limit,BookParam bookParam);

    /**
     * 根据id得到一个Book
     * @param id
     * @return
     */
    public Book byIdBook(Integer id);

    public int changeBookStatus(Integer id,Integer statusId);

}

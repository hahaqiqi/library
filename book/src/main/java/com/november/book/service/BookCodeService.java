package com.november.book.service;

import com.november.book.model.Book;
import com.november.book.model.BookCode;
import com.november.book.param.BookCodeParam;
import com.november.book.param.BookParam;

import java.util.List;

public interface BookCodeService {
    /**
     * 新增
     * @param param
     * @return int
     */
    public int saveBookCode(BookCodeParam param);

    /**
     * 根据id修改
     * @param param
     * @return int
     */
    public int updateBookCode(BookCodeParam param);

    /**
     * 根据id删除一列
     * @param id
     * @return int
     */
    public int deleteBookCode(Integer id);


    /**
     * 得到总数
     * @return
     */
    public int bookCodeCount();

    /**
     * 得到所有
     * @return List<Book>
     */
    public List<BookCode> listBookCode();

    /**
     * 分页
     * @return
     */
    public List<BookCode> pageListBookCode(int page, int limit);

    /**
     * 根据id得到一个BookCode
     * @param id
     * @return
     */
    public BookCode byIdBookCode(Integer id);

}

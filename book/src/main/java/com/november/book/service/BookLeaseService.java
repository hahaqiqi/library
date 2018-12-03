package com.november.book.service;

import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;

import java.util.List;

public interface BookLeaseService {
    /**
     * 新增
     * @param param
     * @return int
     */
    public int saveBookLease(BookLeaseParam param);

    /**
     * 根据id修改
     * @param param
     * @return int
     */
    public int updateBookLease(BookLeaseParam param);

    /**
     * 根据id删除一列
     * @param id
     * @return int
     */
    public int deleteBookLease(Integer id);

    /**
     * 批量删除
     * @param list
     * @return int
     */
    public int batchDeleteBookLease(List<Integer> list);

    /**
     * 得到总数
     * @return
     */
    public int bookLeaseCount();

    /**
     * 得到所有
     * @return List<BookLease>
     */
    public List<BookLease> listBookLease();

    /**
     * 分页
     * @return
     */
    public List<BookLease> pageListBookLease(int page, int limit);

    /**
     * 根据id得到一个
     * @param id
     * @return
     */
    public BookLease byIdBookLease(Integer id);

    /**
     * 根据bookid得到该书所有租借记录
     * @param id
     * @return
     */
    public List<BookLease> selectBookLeaseByBookId(Integer id);

    /**
     * 根据bookid得到该书被借阅的数量
     * @param id
     * @return
     */
    public int selectBookLeaseCountByBookId(Integer id);

    /**
     * 通过bookid得到为完成的订单
     * @param bookId
     * @return
     */
    public BookLease getBookLeaseOne(Integer bookId);

}

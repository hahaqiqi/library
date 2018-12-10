package com.november.book.service;

import com.november.book.model.BookLeaseType;
import com.november.book.param.BookLeaseTypeParam;

import java.util.List;

public interface BookLeaseTypeService {
    /**
     * 新增
     * @param param
     * @return int
     */
    public int saveBookLeaseType(BookLeaseTypeParam param);

    /**
     * 根据id修改
     * @param param
     * @return int
     */
    public int updateBookLeaseType(BookLeaseTypeParam param);

    /**
     * 根据id删除一列
     * @param id
     * @return int
     */
    public int deleteBookLeaseType(Integer id);


    /**
     * 得到总数
     * @return
     */
    public int bookLeaseTypeCount();

    /**
     * 得到所有
     * @return List<Book>
     */
    public List<BookLeaseType> listBookLeaseType();

    /**
     * 分页
     * @return
     */
    public List<BookLeaseType> pageListBookLeaseType(int page, int limit);

    /**
     * 根据id得到一个BookLeaseType
     * @param id
     * @return
     */
    public BookLeaseType byIdBookLeaseType(Integer id);
}

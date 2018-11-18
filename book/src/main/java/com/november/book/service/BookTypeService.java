package com.november.book.service;

import com.november.book.model.BookType;
import com.november.book.param.BookTypeParam;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookTypeService {
    /**
     * 新增
     * @param param
     * @return int
     */
    public int saveBookType(BookTypeParam param);

    /**
     * 根据id修改
     * @param param
     * @return int
     */
    public int updateBookType(BookTypeParam param);

    /**
     * 根据id删除一列
     * @param id
     * @return int
     */
    public int deleteBookType(Integer id);

    /**
     * 批量删除
     * @param list
     * @return int
     */
    public int batchDeleteBookType(List<Integer> list);

    /**
     * 得到总数
     * @return
     */
    public int bookTypeCount();

    /**
     * 得到所有
     * @return List<BookType>
     */
    public List<BookType> listBookType();

    /**
     * 分页
     * @return
     */
    public List<BookType> pageListBookType(int page,int limit);

    /**
     * 根据id得到一个BookType
     * @param id
     * @return
     */
    public BookType byIdBookType(Integer id);

}

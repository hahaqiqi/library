package com.november.book.service;

import com.november.book.model.BookLossType;
import com.november.book.param.BookLossTypeParam;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookLossTypeService {
    /**
     * 新增
     * @param param
     * @return
     */
    public int saveBookLossType(BookLossTypeParam param);

    /**
     * 修改
     * @param param
     * @return int
     */
    public int updateBookLossType(BookLossTypeParam param);

    /**
     * 删除
     * @param id
     * @return
     */
    public int deleteBookLossType(Integer id);

    /**
     * 得到所有
     * @return List<BookLossType>
     */
    public List<BookLossType> listBookLossType();

    /**
     * 根据主键id得到一列
     * @return BookLossType
     */
    public BookLossType selectByIdBookLossType(Integer id);
}

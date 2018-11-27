package com.november.book.service;/*
 *
 **/

import com.google.common.base.Preconditions;
import com.november.book.dao.BookLeaseTypeMapper;
import com.november.book.model.BookLeaseType;
import com.november.book.param.BookLeaseTypeParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("bookLeaseTypeService")
public class BookLeaseTypeServiceImpl implements BookLeaseTypeService {
    @Resource
    private BookLeaseTypeMapper bookLeaseTypeMapper;

    @Override
    public int saveBookLeaseType(BookLeaseTypeParam param) {
        BeanValidator.check(param);
        if(bookLeaseTypeMapper.selectByTypeName(param)>0){
            throw new ParamException("已经有相同的类型名");
        }
        BookLeaseType bookLeaseType=BookLeaseType.builder().typeName(param.getTypeName()).discount(param.getDiscount())
                                        .score(param.getScore()).remark(param.getRemark()).build();
        bookLeaseType.setOperator("admin");
        return bookLeaseTypeMapper.insertSelective(bookLeaseType);
    }

    @Override
    public int updateBookLeaseType(BookLeaseTypeParam param) {
        BeanValidator.check(param);
        BookLeaseType before=bookLeaseTypeMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"修改内容不存在");
        if(bookLeaseTypeMapper.selectByTypeName(param)>0){
            throw new ParamException("已经有相同的类型名");
        }
        BookLeaseType after=BookLeaseType.builder().typeName(param.getTypeName()).discount(param.getDiscount())
                .score(param.getScore()).remark(param.getRemark()).id(param.getId()).build();
        after.setOperator("admin");
        after.setOperateTime(new Date());
        return bookLeaseTypeMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public int deleteBookLeaseType(Integer id) {
        return bookLeaseTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int bookLeaseTypeCount() {
        return bookLeaseTypeMapper.count();
    }

    @Override
    public List<BookLeaseType> listBookLeaseType() {

        return bookLeaseTypeMapper.listAll();
    }

    @Override
    public List<BookLeaseType> pageListBookLeaseType(int page, int limit) {
        return bookLeaseTypeMapper.pageList((page-1)*limit,limit);
    }

    @Override
    public BookLeaseType byIdBookLeaseType(Integer id) {
        return bookLeaseTypeMapper.selectByPrimaryKey(id);
    }
}

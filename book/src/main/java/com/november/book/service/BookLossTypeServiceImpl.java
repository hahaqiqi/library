package com.november.book.service;

import com.google.common.base.Preconditions;
import com.november.book.dao.BookLossTypeMapper;
import com.november.book.model.BookLossType;
import com.november.book.param.BookLossTypeParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("bookLossTypeService")
public class BookLossTypeServiceImpl implements BookLossTypeService {
    @Resource
    private BookLossTypeMapper bookLossTypeMapper;
    @Override
    public int saveBookLossType(BookLossTypeParam param) {
        BeanValidator.check(param);
        if(checkBookLossTypeName(param.getTypeName(),param.getId())){
            throw new ParamException("已经有相同的损坏类型名称");
        }
        BookLossType bookLossType=BookLossType.builder().typeName(param.getTypeName())
                .score(param.getScore()).price(param.getPrice()).remark(param.getRemark()).build();
        bookLossType.setOperator("admin");
        return bookLossTypeMapper.insertSelective(bookLossType);
    }

    @Override
    public int updateBookLossType(BookLossTypeParam param) {
        BeanValidator.check(param);
        BookLossType before=bookLossTypeMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "修改的内容不存在");
        if(checkBookLossTypeName(param.getTypeName(),param.getId())){
            throw new ParamException("已经有相同的损坏类型名称");
        }
        BookLossType after=BookLossType.builder().typeName(param.getTypeName()).price(param.getPrice()).score(param.getScore())
                .remark(param.getRemark()).id(param.getId()).build();
        after.setOperator("admin");
        after.setOperateTime(new Date());
        return bookLossTypeMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public int deleteBookLossType(Integer id) {
        BookLossType before=bookLossTypeMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "删除的内容不存在");
        return bookLossTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BookLossType> listBookLossType() {
        return bookLossTypeMapper.getBookLossType(null);
    }

    @Override
    public BookLossType selectByIdBookLossType(Integer id) {
        return bookLossTypeMapper.getBookLossType(id).get(0);
    }

    //检查BookLossTypeName是否同名
    public boolean checkBookLossTypeName(String bookLossBookTypeName,Integer id){
        return bookLossTypeMapper.checkBookLossTypeName(bookLossBookTypeName,id)>0;
    }
}

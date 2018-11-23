package com.november.book.service;

import com.google.common.base.Preconditions;
import com.november.book.dao.BookTypeMapper;
import com.november.book.model.BookType;
import com.november.book.param.BookTypeParam;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service(value = "bookTypeService")
public class BookTypeServiceImpl implements BookTypeService {

    @Resource
    private BookTypeMapper bookTypeMapper;
    @Override
    public int saveBookType(BookTypeParam param) {
        //检查用户输入的字段
        BeanValidator.check(param);
        //检查BookTypeName值是否同名
        if(checkBookTypeName(param.getTypeName(),param.getId())){
            throw new ParamException("已经有相同的图书类型名");
        }
        //将BookTypeParam中的值放入BookType
        BookType bookType=BookType.builder()
                .typeName(param.getTypeName())
                .remark(param.getRemark()).build();
        //加入自动插入的值，比如当时的管理员
        bookType.setOperator("admin");
        //bookType.setOperator(当前登录管理员的用户名);
        //执行插入并返回
        return bookTypeMapper.insertSelective(bookType);
    }

    @Override
    public int updateBookType(BookTypeParam param) {
        BeanValidator.check(param);
        BookType before =bookTypeMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "修改的内容不存在");
        //检查BookTypeName值是否同名
        if(checkBookTypeName(param.getTypeName(),param.getId())){
            throw new ParamException("已经有相同的图书类型名");
        }
        BookType after =BookType.builder().typeName(param.getTypeName()).remark(param.getRemark()).id(param.getId()).build();
        after.setOperator("admin");
        after.setOperateTime(new Date());
        bookTypeMapper.updateByPrimaryKeySelective(after);
        return 0;
    }

    @Override
    public int deleteBookType(Integer id) {
        BookType before =bookTypeMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "删除的内容不存在");
        return bookTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDeleteBookType(List<Integer> list) {
        return  bookTypeMapper.bacthDelectBookType(list);
    }

    @Override
    public int bookTypeCount() {
        return bookTypeMapper.bookTypeCount();
    }

    @Override
    public List<BookType> listBookType() {
        return  bookTypeMapper.selectAll();
    }

    @Override
    public List<BookType> pageListBookType(int page, int limit) {
        return bookTypeMapper.pageSelect((page-1)*limit,limit);
    }

    @Override
    public BookType byIdBookType(Integer id) {
        return  bookTypeMapper.selectByPrimaryKey(id);
    }

    //检查BookTypeName是否同名
    public boolean checkBookTypeName(String bookTypeName,Integer id){
        return bookTypeMapper.checkBookTypeName(bookTypeName,id)>0;
    }
}

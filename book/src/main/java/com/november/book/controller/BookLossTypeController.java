package com.november.book.controller;

import com.november.book.model.BookLossType;
import com.november.book.param.BookLossTypeParam;
import com.november.book.param.BookTypeParam;
import com.november.book.service.BookLossTypeService;
import com.november.common.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/bookLossType")
public class BookLossTypeController {
    @Resource(name = "bookLossTypeService")
    private BookLossTypeService bookLossTypeService;

    @RequestMapping(value = "/save.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData saveBookType(BookLossTypeParam param){
        bookLossTypeService.saveBookLossType(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData updateBookType(BookLossTypeParam param){
        bookLossTypeService.updateBookLossType(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id){
        bookLossTypeService.deleteBookLossType(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(){
        List<BookLossType> listBookType = bookLossTypeService.listBookLossType();
        return JsonData.success(listBookType);
    }

    @RequestMapping(value = "/select.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookType(Integer id){
        BookLossType bookLossType=bookLossTypeService.selectByIdBookLossType(id);
        return JsonData.success(bookLossType);
    }
}

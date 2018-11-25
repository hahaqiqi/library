package com.november.book.controller;

import com.november.book.model.BookLeaseType;
import com.november.book.param.BookLeaseTypeParam;
import com.november.book.param.BookParam;
import com.november.book.service.BookLeaseTypeService;
import com.november.book.service.BookService;
import com.november.common.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/bookLeaseType")
public class BookLeaseTypeController {

    @Resource(name = "bookLeaseTypeService")
    private BookLeaseTypeService bookLeaseTypeService;
    @Resource(name = "bookService")
    private BookService bookService;

    @RequestMapping(value = "/bookLeaseType.html")
    public String toBookLeaseTpe(){
        return "bookLeaseType";
    }

    @RequestMapping(value = "/save.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookType(BookLeaseTypeParam param){
        bookLeaseTypeService.saveBookLeaseType(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookLeaseTypeParam param){
        bookLeaseTypeService.updateBookLeaseType(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData delete(Integer id){
        //先更改书籍表中关联了该id的值
        BookParam bookParam=new BookParam();
        bookParam.setBookLeaseType(0);
        bookService.batchUpdate(bookParam);
        //删除本表数据
        bookLeaseTypeService.deleteBookLeaseType(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/listAll.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listAllBookType(){
        List<BookLeaseType> listBookLeaseType=bookLeaseTypeService.listBookLeaseType();
        return JsonData.success(listBookLeaseType);
    }

    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request){
        int count=bookLeaseTypeService.bookLeaseTypeCount();
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<BookLeaseType> listBookLeaseType=bookLeaseTypeService.pageListBookLeaseType(page,limit);
        return JsonData.pageSuccess(listBookLeaseType,count,limit);
    }

    @RequestMapping(value = "/select.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData select(Integer id){
        //BookCode bookCode= bookCodeService.byIdBookCode(id);
        return JsonData.success(null);
    }




}

package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.BookService;
import com.november.common.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/book")
public class BookController {
    @Resource(name = "bookService")
    private BookService bookService;

    @RequestMapping(value = "/book.html")
    public String toBook(){
        return "book";
    }

    @RequestMapping(value = "/bookAdd.html")
    public String toBookAdd(){
        return "bookAdd";
    }

    @RequestMapping(value = "/save.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookType(BookParam param){
       bookService.saveBook(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookParam param){
        bookService.updateBook(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id){

        return JsonData.success();
    }

    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request){
        int count=bookService.bookCount();
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<Book> listBook=bookService.pageListBook(page,limit);
        return JsonData.pageSuccess(listBook,count,limit);
    }

    @RequestMapping(value = "/select.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookType(Integer id){

        return JsonData.success();
    }

    //批量删除
    @RequestMapping(value = "/batchDelete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData batchDeleteBookType(String batchStrId){

        return JsonData.success();
    }

}

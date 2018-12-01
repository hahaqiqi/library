package com.november.book.controller;

import com.november.book.model.BookCode;
import com.november.book.param.BookCodeParam;
import com.november.book.param.BookParam;
import com.november.book.service.BookCodeService;
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
@RequestMapping(value = "/bookCode")
public class BookCodeController {
    @Resource(name = "bookCodeService")
    private BookCodeService bookCodeService;

    @RequestMapping(value = "/bookCode.html")
    public String toBook(){
        return "bookCode";
    }

    @RequestMapping(value = "/save.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookType(BookCodeParam param){
       bookCodeService.saveBookCode(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookCodeParam param){
        bookCodeService.updateBookCode(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id){
        bookCodeService.deleteBookCode(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request){
        int count=bookCodeService.bookCodeCount();
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<BookCode> listBook=bookCodeService.pageListBookCode(page,limit);
        return JsonData.pageSuccess(listBook,count,limit);
    }

    @RequestMapping(value = "/select.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookCode(Integer id){
        BookCode bookCode= bookCodeService.byIdBookCode(id);
        return JsonData.success(bookCode);
    }

    @RequestMapping(value = "/selectByPrice.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookCodeByPrice(Double price){
        BookCode bookCode= bookCodeService.byPriceBookCode(price);
        return JsonData.success(bookCode);
    }


}

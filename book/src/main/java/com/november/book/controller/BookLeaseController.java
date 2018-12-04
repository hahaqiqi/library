package com.november.book.controller;

import com.november.book.model.BookLease;
import com.november.book.model.BookType;
import com.november.book.param.BookLeaseParam;
import com.november.book.param.BookTypeParam;
import com.november.book.service.BookLeaseService;
import com.november.book.service.BookTypeService;
import com.november.book.util.CalculatePrice;
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
@RequestMapping(value = "/bookLease")
public class BookLeaseController {
    @Resource(name = "bookLeaseService")
    private BookLeaseService bookLeaseService;

    private BookLeaseParam filtrateBookLeaseParam;

    @RequestMapping(value = "/bookLease.html")
    public String toBookLease(){
        return "bookLease";
    }

    @RequestMapping(value = "/save.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookLease(BookLeaseParam param){
        int newNumber= bookLeaseService.saveBookLease(param);
        return JsonData.success(newNumber);
    }

    @RequestMapping(value = "/update.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookLeaseParam param){
        bookLeaseService.updateBookLease(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id){
        //bookLeaseService.deleteBookLease(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request){
        int count=0;
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<BookLease> listBookLease = bookLeaseService.pageListBookLease(page,limit);
        return JsonData.pageSuccess(listBookLease,count,limit);
    }

    @RequestMapping(value = "/listAll.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookTypeAll(){

        return JsonData.success();
    }

    @RequestMapping(value = "/selectByBookId.json",method =RequestMethod.GET)//根据bookid返回所有
    @ResponseBody
    public JsonData selectBookLeaseByBookId(Integer id){

        return JsonData.success();
    }

    @RequestMapping(value = "/selectByBookIdOne.json",method =RequestMethod.GET)//根据bookid返回一条（正在租借中的）,并计算价格
    @ResponseBody
    public JsonData selectBookLeaseByBookIdAndGh(Integer bookId){
        BookLease bookLease= bookLeaseService.getBookLeaseOne(bookId);
        String[] arr= CalculatePrice.getPrice(bookLease.getBookPrice(),bookLease.getDiscount(),bookLease.getOperateTime());
        bookLease.setPrice(Double.parseDouble(arr[0]));
        return JsonData.success(bookLease,arr[1]);
    }

    @RequestMapping(value = "/selectByUserId.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookLeaseByUserId(Integer id){

        return JsonData.success();
    }

    //批量删除
    @RequestMapping(value = "/batchDelete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData batchDeleteBookType(String batchStrId){

        return JsonData.success();
    }

}

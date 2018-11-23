package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.BookService;
import com.november.book.util.IsEmpty;
import com.november.common.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        bookService.deleteBook(id);
        return JsonData.success();
    }


    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request, BookParam bookParam){
        HttpSession session = request.getSession();
        try {
            if(IsEmpty.isAllFieldNull(bookParam)){
                bookParam=(BookParam)session.getAttribute("bookParam");
            }else{
                session.setAttribute("bookParam",bookParam);
            }
        } catch (Exception e) {
            bookParam=null;
        }
        int count=bookService.bookCount();
        int page=0;
        int limit=10;
        try{
            page = Integer.parseInt(request.getParameter("page"));//第几页
            limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        }catch (Exception ex){

        }
        List<Book> listBook=bookService.pageListBook(page,limit,bookParam);
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
        List<Integer> list=new ArrayList<>();
        String params[] = batchStrId.split(",");//参数jie()
        for (int i = 0; i < params.length; i++) {
            list.add(Integer.valueOf(params[i]));
        }
        int count= bookService.batchDeleteBook(list);
        return JsonData.success(null,count+"");
    }

    @RequestMapping(value = "/changeBookStatus.json",method =RequestMethod.GET)//改变单本书籍的状态
    @ResponseBody
    public JsonData changeBookStatus(Integer id,Integer statusId){
        bookService.changeBookStatus(id,statusId);
        return JsonData.success();
    }

}

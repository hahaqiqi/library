package com.november.book.controller;

import com.november.book.model.BookType;
import com.november.book.param.BookTypeParam;
import com.november.book.service.BookTypeService;
import com.november.common.JsonData;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/bookType")
public class BookTypeController {
    @Resource(name = "bookTypeService")
    private BookTypeService bookTypeService;

    @RequestMapping(value = "/bookType.html")
    public String toBookType(){
        return "bookType";
    }

    @RequestMapping(value = "/save.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookType(BookTypeParam param){
        bookTypeService.saveBookType(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json",method =RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookTypeParam param){
        bookTypeService.updateBookType(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id){
        bookTypeService.deleteBookType(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request){
        int count=bookTypeService.bookTypeCount();
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<BookType> listBookType = bookTypeService.pageListBookType(page,limit);
        return JsonData.pageSuccess(listBookType,count,limit);
    }

    @RequestMapping(value = "/select.json",method =RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookType(Integer id){
        BookType selectBookType = bookTypeService.byIdBookType(id);
        return JsonData.success(selectBookType);
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
        int count= bookTypeService.batchDeleteBookType(list);
        return JsonData.success(null,count+"");
    }

}

package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.SpaceBookService;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/spacebook")
public class SpaceBookController {

    @Resource(name = "spacebookService")
    private SpaceBookService spacebookservice;

    @ResponseBody
    @RequestMapping("/selectBook.json")                         //"1,2,3,4,5"
    public JsonData selectBook(@RequestParam(value = "bookspaceid") String bookspaceid
        ,HttpServletRequest request){
        log.info("获取书籍信息",bookspaceid);
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<Integer> list = new ArrayList<>();
        String params[] = bookspaceid.split(",");//参数jie()
        for (int i = 0; i < params.length; i++) {
            list.add(Integer.valueOf(params[i]));
        }
        int count=spacebookservice.Booklimit(list);
        BookParam bookParam=new BookParam();
        bookParam.setWhereList(list);
        bookParam.setLimit(limit);
        bookParam.setPage((page-1)*limit);
        List<Book> listBook= spacebookservice.selectSpaceBook(bookParam);
        return JsonData.pageSuccess(listBook,count,limit);


    }

    @ResponseBody
    @RequestMapping("/spaceBookremove.json")
    public JsonData remove(@RequestParam(value = "bookId") Integer bookId){
        spacebookservice.BookSpaceremove(null,bookId);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/spaceBookAdd.json")
    public JsonData bookSpaceAdd(
            @RequestParam(value = "bookpid") Integer bookpid,
            @RequestParam(value = "bookCode") String bookCode,
            @RequestParam(value = "status") Integer status
                                 ){
        spacebookservice.BookSpaceAdd(bookpid,bookCode,status);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/spaceBookAddList.json")
    public JsonData bookSpaceAddList(List<Book> listBookCode){
        spacebookservice.BookSpaceAddList(listBookCode);
        return JsonData.success();
    }
}

package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.SpaceBookService;
import com.november.common.JsonData;
import com.november.exception.ParamException;
import com.november.model.BookExcel;
import com.november.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    public JsonData bookSpaceAddList(
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "bookspaceid") Integer bookpid,
            @RequestParam(value = "backImageFile", required = false) MultipartFile file){
        /*spacebookservice.BookSpaceAddList();*/
        List<BookExcel> bookExcels= ExcelUtil.loadBookExcel(file);
        //BookExcel转换成list
        List<Book> spaceBooks=new ArrayList<>(bookExcels.size()-1);
        for (int i = 1; i < bookExcels.size(); i++) {
            try {
                Book book = new Book();
                book.setBookCode(bookExcels.get(i).getBookCode());
                book.setBookSpaceId(bookpid);
                spaceBooks.add(book);
            } catch (NumberFormatException e) {
                throw new ParamException("错误，请检查Excel的第" + (i + 1) + "行数据");
            } catch (Exception e) {
                throw new ParamException("加载数据失败");
            }
        }
        spacebookservice.BookSpaceAddList(status,bookpid,spaceBooks);
        return JsonData.success();
    }
}

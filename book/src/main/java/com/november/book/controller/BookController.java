package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.BookService;
import com.november.book.util.IsEmpty;
import com.november.common.JsonData;
import com.november.util.Email;
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

    private BookParam filtrateBookParam=null; //筛选条件

    @RequestMapping(value = "/book.html")
    public String toBook() {
        return "book";
    }

    @RequestMapping(value = "/bookAdd.html")
    public String toBookAdd() {
        return "bookAdd";
    }

    @RequestMapping(value = "/bookBorrow.html")  //直接拦截借书的页面
    public String toBookBorrow() {
        return "bookBorrow";
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookType(BookParam param) {
        bookService.saveBook(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookParam param) {
        bookService.updateBook(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/batchUpdate.json", method = RequestMethod.POST)  //修改
    @ResponseBody
    public JsonData batchUpdateBookType(HttpServletRequest request,BookParam param) {
        try {
            if (IsEmpty.isAllFieldNull(param)) {
                return JsonData.fail("无更改");
            }
        } catch (Exception e) {
            return JsonData.fail("未知错误");
        }
        HttpSession session = request.getSession();
        BookParam bookParam=null;
        try {
            bookParam = (BookParam) session.getAttribute("bookParam");
            param.setWhereList(bookParam.getWhereList());
        }catch (Exception ex){

        }
        bookService.batchUpdate(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id) {
        bookService.deleteBook(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/reFiltrate.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData reFiltrate() {
        filtrateBookParam=null;
        return JsonData.success();
    }

    @RequestMapping(value = "/setFiltrate.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData setFiltrate(HttpServletRequest request, BookParam bookParam) {
        try {
            if (!IsEmpty.isAllFieldNull(bookParam)) {
                List<Book> list= bookService.whereListBook(bookParam);
                List<Integer> listInt=new ArrayList<>();
                for(Book book:list){
                    listInt.add(book.getId());
                }
                bookParam.setWhereList(listInt);
                filtrateBookParam=bookParam;
            }
        } catch (Exception e) {

        }
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json", method = RequestMethod.GET)//查 包括高级查询
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request) {
        int count;
        if(filtrateBookParam==null){
            count=bookService.bookCount();
        }else {
            count = filtrateBookParam.getWhereList().size();
        }
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<Book> listBook = bookService.pageListBook(page, limit, filtrateBookParam);
        return JsonData.pageSuccess(listBook, count, limit);
    }

    @RequestMapping(value = "/select.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookType(Integer id) {

        return JsonData.success();
    }


    @RequestMapping(value = "/batchDelete.json", method = RequestMethod.GET)//批量删除
    @ResponseBody
    public JsonData batchDeleteBookType(String batchStrId) {
        List<Integer> list = new ArrayList<>();
        String params[] = batchStrId.split(",");//参数jie()
        for (int i = 0; i < params.length; i++) {
            list.add(Integer.valueOf(params[i]));
        }
        int count = bookService.batchDeleteBook(list);
        return JsonData.success(null, count + "");
    }

    @RequestMapping(value = "/changeBookStatus.json", method = RequestMethod.GET)//改变单本书籍的状态
    @ResponseBody
    public JsonData changeBookStatus(Integer id, Integer statusId) {
        bookService.changeBookStatus(id, statusId);
        return JsonData.success();
    }

    @RequestMapping(value = "/searchBook.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData searchBook(String searchVal) {
        String params[] = searchVal.split(",");
        List<String> listStr=new ArrayList<>();
        for(String str:params){
            listStr.add(str);
        }
        List<Book> list=bookService.getBookByCode(listStr);
        return JsonData.success(list);
    }

    @RequestMapping(value = "/bookState.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData bookState(Integer id) {//根据书籍id判断 未上架,可借阅,被借阅

        return JsonData.success();
    }

    @RequestMapping(value = "/getCode.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData getCode(String email) {
        String code= Email.GetCode(email);
        return JsonData.success(code);
    }

}

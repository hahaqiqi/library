package com.november.book.controller;

import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;
import com.november.book.service.BookLeaseService;
import com.november.book.util.CalculatePrice;
import com.november.common.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/bookLease")
public class BookLeaseController {
    @Resource(name = "bookLeaseService")
    private BookLeaseService bookLeaseService;

    private BookLeaseParam filtrateBookLeaseParam;

    @RequestMapping(value = "/bookLease.html")
    public String toBookLease() {
        return "bookLease";
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookLease(BookLeaseParam param) {
        int newNumber = bookLeaseService.saveBookLease(param);
        return JsonData.success(newNumber);
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookLeaseParam param) {
        bookLeaseService.updateBookLease(param);
        //return "redirect:/book/updateLeaseId.json?bookId="+param.getBookId();
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id) {
        //bookLeaseService.deleteBookLease(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData listBookType(BookLeaseParam param) {//分页条件查询bookLease
        int count=bookLeaseService.getCount(param);
        List<BookLease> listBookLease = bookLeaseService.selectByParam(param);
        return JsonData.pageSuccess(listBookLease, count, param.getLimit());
    }

    @RequestMapping(value = "/listAll.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData listBookTypeAll() {
        return JsonData.success();
    }

    @RequestMapping(value = "/selectByBookId.json", method = RequestMethod.GET)//根据bookid返回所有
    @ResponseBody
    public JsonData selectBookLeaseByBookId(Integer id) {

        return JsonData.success();
    }

    @RequestMapping(value = "/selectByBookIdOne.json", method = RequestMethod.GET)//根据bookid返回一条（正在租借中的）,并计算价格
    @ResponseBody
    public JsonData selectBookLeaseByBookIdAndGh(Integer bookId) {
        BookLease bookLease = bookLeaseService.getBookLeaseOne(bookId);
        String[] arr = CalculatePrice.getPrice(bookLease.getBookPrice(), bookLease.getDiscount(), bookLease.getOperateTime());
        if (bookLease.getPrice() > 0) {
            arr[1] = arr[1] + " + " + bookLease.getPrice() + "(上次租借未支付金额)";
        }
        bookLease.setPrice(Double.parseDouble(arr[0]) + bookLease.getPrice());
        return JsonData.success(bookLease, arr[1]);
    }

    @RequestMapping(value = "/selectByBookIdOne2.json", method = RequestMethod.GET)//根据bookid返回租借人userid
    @ResponseBody
    public JsonData selectBookLeaseByBookId2(Integer bookId) {
        BookLease bookLease = bookLeaseService.getBookLeaseOne(bookId);
        return JsonData.success(bookLease.getUserId());
    }

    @RequestMapping(value = "/selectAllByUserId.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookLeaseByUserId(Integer id) {
        return JsonData.success();
    }

    //批量删除
    @RequestMapping(value = "/batchDelete.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData batchDeleteBookType(String batchStrId) {

        return JsonData.success();
    }

    //根据用户id得到该用户正在租借的书籍的list id
    @RequestMapping(value = "/selectByUserId.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData selectByUserId(Integer userId) {
        List<Integer> list = bookLeaseService.selectByUserId(userId);
        return JsonData.success(list);
    }


}

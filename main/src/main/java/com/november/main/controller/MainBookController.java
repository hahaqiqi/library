package com.november.main.controller;

import com.november.book.model.BookLease;
import com.november.book.param.BookLeaseParam;
import com.november.book.service.BookLeaseService;
import com.november.book.service.BookService;
import com.november.book.util.CalculatePrice;
import com.november.common.JsonData;
import com.november.exception.ParamException;
import com.november.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/mainBook")
public class MainBookController {

    @Resource(name = "bookLeaseService")
    private BookLeaseService bookLeaseService;
    @Resource(name = "bookService")
    private BookService bookService;
    @Resource(name = "userService")
    private UserService userService;

    //借书操作
    @RequestMapping(value = "/borrowBook.json", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonData borrowBook(BookLeaseParam param) {
        int leaseId = bookLeaseService.saveBookLease(param); //新增订单表数据并返回新增的id
        bookService.updateBookLeaseIdByBookId(param.getBookId(), leaseId);//修改借的书的租借id
        return JsonData.success();
    }

    //还书操作
    @RequestMapping(value = "/returnBook.json", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonData returnBook(BookLeaseParam param) {
        //完善订单表数据
        bookLeaseService.updateBookLease(param);
        //修改书籍租借id为null
        bookService.updateBookLeaseIdByBookId(param.getBookId(), null);
        //为用户加积分
        userService.updateScore(param.getUserId(), param.getPrice());
        return JsonData.success();
    }

    //续租操作
    @RequestMapping(value = "/reletBook.json", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonData reletBook(Integer bookId) {
        //根据bookid得到该订单
        BookLease bookLease = bookLeaseService.getBookLeaseOne(bookId);
        if (bookLease.getPrice() > 0) {
            throw new ParamException("已经续租过，不可继续续租");
        }
        //得到目前为止的收费arr[0]
        String[] arr = CalculatePrice.getPrice(bookLease.getBookPrice(), bookLease.getDiscount(), bookLease.getOperateTime());
        bookLease.setPrice(Double.parseDouble(arr[0]));
        //设置状态为租借完成
        bookLease.setStatus(0);
        //执行修改
        BookLeaseParam bookLeaseParam = new BookLeaseParam();
        bookLeaseParam.setId(bookLease.getId());
        bookLeaseParam.setBookId(bookLease.getBookId());
        bookLeaseParam.setUserId(bookLease.getUserId());
        bookLeaseParam.setBookPrice(bookLease.getBookPrice());
        bookLeaseParam.setDiscount(bookLease.getDiscount());
        bookLeaseParam.setStatus(bookLease.getStatus());
        bookLeaseParam.setPrice(bookLease.getPrice());
        bookLeaseParam.setRemark(bookLease.getRemark());
        bookLeaseService.updateBookLease(bookLeaseParam);
        //创建新的订单
        int leaseId = bookLeaseService.saveBookLease(bookLeaseParam); //新增订单表数据并返回新增的id
        bookService.updateBookLeaseIdByBookId(bookLeaseParam.getBookId(), leaseId);//修改借的书的租借id
        return JsonData.success();
    }

    //损坏遗失操作
    @RequestMapping(value = "/loseBook.json", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonData loseBook(Integer bookId, Double compensatePrice) {
        //根据bookid得到该订单
        BookLease bookLease = bookLeaseService.getBookLeaseOne(bookId);
        if (compensatePrice < bookLease.getPrice()) {
            throw new ParamException("书籍续租过，赔付金额必须大于上次租借产生的费用："+bookLease.getPrice());//表示续租过，交付金额必须大于上次租借产生的费用
        }
        //设置状态为遗失、损坏
        bookLease.setStatus(2);
        //设置收费
        bookLease.setPrice(compensatePrice);
        //删除书籍（实际为修改状态）
        bookService.deleteBook(bookId);
        //创建对象并执行修改
        BookLeaseParam bookLeaseParam = new BookLeaseParam();
        bookLeaseParam.setId(bookLease.getId());
        bookLeaseParam.setStatus(bookLease.getStatus());
        bookLeaseParam.setPrice(bookLease.getPrice());
        bookLeaseService.updateBookLease(bookLeaseParam);
        return JsonData.success();
    }
}

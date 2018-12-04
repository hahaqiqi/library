package com.november.main.commons;

import com.november.book.model.Book;
import com.november.book.model.BookLease;
import com.november.book.service.BookLeaseService;
import com.november.book.service.BookService;
import com.november.user.model.User;
import com.november.user.service.UserService;
import com.november.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class DayBack {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookLeaseService bookLeaseService;

    @Scheduled(cron = "0 40 16 ? * *")
    public void dayEmail(){
        List<BookLease> list=bookLeaseService.getAll();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        String info="";
        for(BookLease b:list){
            int day= TimeUtil.getDifferenceDay(b.getOperateTime(),new Date());
            User user=userService.selectByPrimaryKey(b.getUserId());//查邮箱
            Book book=bookService.byIdBook(b.getBookId());//调用byIdBook查书名
            String eday= sdf.format(b.getOperateTime());
            if(day>=27 && day<=30 && b.getStatus()==1){
                info="尊敬的用户，您于"+ eday+"日,在本图书馆租借的《"+ book.getBookName()+
                        "》图书,已租借"+day+"天,请在30天之内归还图书,若逾期未还将以租借价格的两倍/天收取费用(最低0.2元/天)";
            }else if(day>30 && b.getStatus()==1){
                info="尊敬的用户，您于"+ eday+"日,在本图书馆租借的《"+ book.getBookName()+
                        "》图书,租借了"+day+"天,超过限定时间30天，请尽快归还";
            }else{
                continue;
            }
            com.november.util.Email.remindUser(user.getUserEmail(),info);
        }
    }
}

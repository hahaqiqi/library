package com.november.main.controller;

import com.november.book.model.BookLease;
import com.november.book.service.BookLeaseService;
import com.november.common.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class Email {
    @Autowired
    private BookLeaseService bookLeaseService;

  public JsonData ervydayEmail(){
      List<BookLease> list=null;
      for(BookLease b:list){
        b.getOperateTime();
      }
      return JsonData.success();
  }
}

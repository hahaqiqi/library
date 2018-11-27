package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.service.SpaceBookService;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/Spacebook")
public class SpaceBookController {

    @Resource(name = "spacebookService")
    private SpaceBookService spacebookservice;

    @ResponseBody
    @RequestMapping("/selectBook.json")                         //"1,2,3,4,5"
    public JsonData delete(@RequestParam(value = "bookspaceid") String bookspaceid){
        log.info("获取书籍信息",bookspaceid);
        List<Integer> list = new ArrayList<>();
        String params[] = bookspaceid.split(",");//参数jie()
        for (int i = 0; i < params.length; i++) {
            list.add(Integer.valueOf(params[i]));
        }
        List<Book> listBook= spacebookservice.selectSpaceBook(list);
        return JsonData.success();
    }
}

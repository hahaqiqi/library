package com.november.book.controller;

import com.november.book.service.SpaceBookService;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping(value = "/Spacebook")
public class SpaceBookController {

    @Resource(name = "spacebookService")
    private SpaceBookService spacebookservice;

    @ResponseBody
    @RequestMapping("/selectBook.json")
    public JsonData delete(@RequestParam(value = "bookspaceid") Integer bookspaceid){
        log.info("获取书籍信息",bookspaceid);
        spacebookservice.selectSpaceBook(bookspaceid);
        return JsonData.success();
    }
}

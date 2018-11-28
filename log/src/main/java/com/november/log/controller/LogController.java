package com.november.log.controller;

import com.november.common.JsonData;
import com.november.log.model.LogWithBLOBs;
import com.november.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/28 8:54
 */
@Slf4j
@Controller
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @RequestMapping("/log.html")
    public String log(){
        return "log";
    }

    @ResponseBody
    @RequestMapping("/selectDate.json")
    public JsonData date(){
        return JsonData.success(logService.getDateList());
    }

    @ResponseBody
    @RequestMapping("/page.json")
    public JsonData dataPage(HttpServletRequest request,@RequestParam("date") String date){
        int count = logService.getCountByOperTime(date);
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<LogWithBLOBs> pageList = logService.getAllByOperTime(date, page, limit);
        return JsonData.pageSuccess(pageList,count,limit);
    }

}

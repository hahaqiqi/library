package com.november.admin.controller;

import com.google.common.collect.Lists;
import com.november.admin.dto.AdminDto;
import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;
import com.november.admin.service.AdminService;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/20 21:21
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @RequestMapping("/admin.html")
    public ModelAndView page(){
        return new ModelAndView("admin");
    }

    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData saveAclModule(AdminParam param) {
        //  后台日志输出
        log.info("开始进行权限点添加,param:{}", param);
        adminService.save(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/delete.json")
    public JsonData delete(int adminId){
        adminService.delete(adminId);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/update.json")
    public JsonData updateAclModule(AdminParam param) {
        //  后台日志输出
        log.info("开始进行权限点修改,param:{}", param);
        adminService.update(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/list.json")
    public JsonData list(){
        List<Admin> all = adminService.getAll();
        if(CollectionUtils.isEmpty(all)){
            all = Lists.newArrayList();
        }
        return JsonData.success(all);
    }

    @ResponseBody
    @RequestMapping("/page.json")
    public JsonData dataPage(HttpServletRequest request){
        int count = adminService.countAll();
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<AdminDto> pageList = adminService.getPageList(page,limit);
        return JsonData.pageSuccess(pageList,count,limit);
    }

}

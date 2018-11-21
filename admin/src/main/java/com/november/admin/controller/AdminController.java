package com.november.admin.controller;

import com.google.common.collect.Lists;
import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;
import com.november.admin.service.AdminService;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData saveAclModule(AdminParam param) {
        //  后台日志输出
        log.info("开始进行权限点添加,param:{}", param);
        adminService.save(param);
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

}

package com.november.acl.controller;

import com.november.acl.param.AclParam;
import com.november.acl.service.AclService;
import com.november.common.JsonData;
import com.november.util.PageQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

//  TODO
@Slf4j
@Controller
@RequestMapping("/acl")
public class AclController {

    @Resource(name = "aclService")
    private AclService aclService;

    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData saveAclModule(AclParam param) {
        //  后台日志输出
        log.info("开始进行权限点添加,param:{}", param);
        aclService.save(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/update.json")
    public JsonData updateAclModule(AclParam param) {
        //  后台日志输出
        log.info("开始进行权限点修改,param:{}", param);
        aclService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("parentId") Integer parentId, PageQuery page) {
        log.info("开始进行分页查询,parentId:{},param:{}", parentId, page);
        return JsonData.success(aclService.getPageByAclModuleId(parentId, page));
    }

    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId) {
        return JsonData.success();
    }

}

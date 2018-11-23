package com.november.acl.controller;

import com.google.common.collect.Lists;
import com.november.acl.dto.AclDto;
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
import java.util.List;

//  TODO
@Slf4j
@Controller
@RequestMapping("/acl")
public class AclController {

    @Resource(name = "aclService")
    private AclService aclService;

    /*@ResponseBody
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
    }*/

    @ResponseBody
    @RequestMapping("/tree.json")
    public JsonData aclTree(int rid){
        //  后台日志输出
        log.info("开始获取权限点集合,rid:{}", rid);
        List<AclDto> aclTree = aclService.getAll(rid);
        return JsonData.success(aclTree);
    }

    @ResponseBody
    @RequestMapping("/changeAcl.json")
    public JsonData changeRoleAcl(String idStr,int rid){
        aclService.changeAcl(idStr,rid);
        return JsonData.success();
    }
}

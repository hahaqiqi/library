package com.november.acl.controller;

import com.november.acl.param.AclParam;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//  TODO
@Controller
@RequestMapping("/acl")
@Slf4j
public class AclController {

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclParam param) {
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclParam param) {
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId) {
        return JsonData.success();
    }

    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId) {
        return JsonData.success();
    }
}

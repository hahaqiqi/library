package com.november.acl.controller;

import com.november.acl.param.AclModuleParam;
import com.november.acl.service.AclModuleService;
import com.november.acl.service.AclModuleServiceImpl;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//  TODO
@Slf4j
@Controller
@RequestMapping("/aclModule")
public class AclModuleController {

    @Autowired
    private AclModuleService aclModuleService;

    @RequestMapping("/acl.html")
    public ModelAndView page() {
        return new ModelAndView("acl");
    }

    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData saveAclModule(AclModuleParam param) {
        aclModuleService.save(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/update.json")
    public JsonData updateAclModule(AclModuleParam param) {

        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/tree.json")
    public JsonData tree() {
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/delete.json")
    public JsonData delete(@RequestParam("id") int id) {
        return JsonData.success();
    }
}


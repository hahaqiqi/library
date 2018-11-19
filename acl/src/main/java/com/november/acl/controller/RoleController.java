package com.november.acl.controller;

import com.google.common.collect.Lists;
import com.november.acl.model.Role;
import com.november.acl.model.TestTree;
import com.november.acl.param.RoleParam;
import com.november.acl.service.RoleService;
import com.november.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//  TODO
@Slf4j
@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource(name = "roleService")
    private RoleService roleService;

    @RequestMapping("role.html")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    // 测试模板
    @RequestMapping("templatesTest.html")
    public ModelAndView test2(){
        return new ModelAndView("templatesTest");
    }

    //  测试页面
    @RequestMapping("test.html")
    public ModelAndView test1(){
        return new ModelAndView("test");
    }

    @ResponseBody
    @RequestMapping("treeTest.html")
    public ModelAndView testTree(){
        return new ModelAndView("treeTest");
    }

    @ResponseBody
    @RequestMapping("tree.json")
    public JsonData getTree(){
        List<TestTree> list = Lists.newArrayList(
                new TestTree("用户管理","yhgl", true),
                new TestTree("用户组管理","yhzgl", true, Lists.newArrayList(
                        new TestTree("角色管理","yhzgl-jsgl", true,Lists.newArrayList(
                                new TestTree("添加角色","yhzgl-jsgl-tjjs", true),
                                new TestTree("角色列表","yhzgl-jsgl-jslb", false)
                        )),
                        new TestTree("管理员管理","glygl", false,Lists.newArrayList(
                                new TestTree("添加管理员","glygl-tjgly", false),
                                new TestTree("管理员列表","glygl-glylb", false),
                                new TestTree("管理员管理","glygl-glylb", false)
                        ))
                ))
        );

        return JsonData.success(list,"获取成功");
    }

    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData saveRole(RoleParam param) {
        log.info("角色开始添加了,param:{}",param);
        roleService.save(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/update.json")
    public JsonData updateRole(RoleParam param) {
        log.info("角色开始修改了,param:{}",param);
        roleService.update(param);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/delete.json")
    public JsonData delete(int id){
        log.info("角色开始删除了,id:{}",id);
        roleService.delete(id);
        return JsonData.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        List<Role> roleList = roleService.getAll();
        return JsonData.success(roleList);
    }

    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success();
    }

    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        return JsonData.success();
    }

    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        return JsonData.success();
    }

    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId) {
        return JsonData.success();
    }
}

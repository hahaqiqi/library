package com.november.acl.controller;

import com.november.acl.dto.AdminDto;
import com.november.acl.model.Role;
import com.november.acl.param.RoleParam;
import com.november.acl.service.RoleAdminService;
import com.november.acl.service.RoleService;
import com.november.common.JsonData;
import com.november.model.Admin;
import com.november.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

//  TODO
@Slf4j
@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource(name = "roleService")
    private RoleService roleService;

    @Resource
    private RoleAdminService roleAdminService;

    @RequestMapping("role.html")
    public ModelAndView page() {
        return new ModelAndView("role");
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

    @ResponseBody
    @RequestMapping("/changeAdmin.json")
    public JsonData changeAdmin(String idStr,int rid) {
        roleAdminService.changeAdmin(idStr,rid);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/packAdminList.json")
    public JsonData packAdminList(String str,int rid){
        List<Admin> list = JsonMapper.string2Obj(str, new TypeReference<List<Admin>>() {
        });
        List<AdminDto> dtoList = roleAdminService.packAdminList(list, rid);
        return JsonData.success(dtoList);
    }

}

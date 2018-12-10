package com.november.acl.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.november.acl.dao.RoleAdminMapper;
import com.november.acl.dto.AdminDto;
import com.november.admin.dao.AdminMapper;
import com.november.admin.service.AdminService;
import com.november.common.RequestHolder;
import com.november.log.Param.LogParam;
import com.november.log.commons.LogTypeInt;
import com.november.log.service.LogService;
import com.november.model.Admin;
import com.november.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("roleAdminService")
public class RoleAdminServiceImpl implements RoleAdminService {

    @Resource
    private RoleAdminMapper roleAdminMapper;

    //@Resource
    //private LogService logService;

    @Resource
    private AdminMapper adminMapper;

    public void changeAdmin(String idStr, int rid) {
        //  获得已分配的管理员的id
        List<Integer> idList = roleAdminMapper.getAdminIdsByRoleId(rid);
        //  删除分配
        roleAdminMapper.deleteByRoleId(rid);
        //  记录日志
        //logService.saveLog(LogParam.builder().targetId(rid).remark("删除角色已分配的管理员")
        //       .type(LogTypeInt.ROLEADMIN_TYPE).oldValue(JsonMapper.obj2String(idList)).build());
        //  判断id字符串是否不为空
        if (StringUtils.isNotBlank(idStr)) {
            //  分割id字符串
            String[] strs = StringUtils.split(idStr, ",");
            //  转化字符串数组为集合
            List<Integer> ids = Lists.newArrayList(Arrays.asList(strs).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList()));
            //  分配角色给管理员
            batchRoleAdmin(ids, rid);
        }
    }

    private void batchRoleAdmin(List<Integer> ids, int rid) {
        //  判断当前是否登陆了管理员
        if (RequestHolder.getCurrentAdmin() != null) {
            //  当前管理员
            roleAdminMapper.batchInsert(ids, rid, RequestHolder.getCurrentAdmin().getAdminCode());
        } else {
            //  默认admin
            roleAdminMapper.batchInsert(ids, rid, "admin");
        }
        //  记录日志
        //logService.saveLog(LogParam.builder().targetId(rid).remark("添加管理员新分配的角色")
        //       .type(LogTypeInt.ROLEADMIN_TYPE).newValue(JsonMapper.obj2String(ids)).build());
    }

    @Override
    public List<AdminDto> packAdminList(List<Admin> list, int rid) {
        //  创建要返回的
        List<AdminDto> adminDtoList = Lists.newArrayList();
        //  查出来拥有该角色的管理员
        List<Integer> adminIdsByRoleId = roleAdminMapper.getAdminIdsByRoleId(rid);
        //  循环传进来管理员集合
        for (Admin admin : list) {
            //  转换
            AdminDto dto = AdminDto.adapt(admin);
            //  判断该管理员是否在其中
            if (adminIdsByRoleId.contains(dto.getId())) {
                //  默认选中
                dto.setSelected("selected");
            }
            //  添加入结果中
            adminDtoList.add(dto);
        }
        //  return
        return adminDtoList;
    }

    @Override
    public List<Integer> getRoleIdListByAdminId(int adminId) {
        return roleAdminMapper.getRoleIdsByAdminId(adminId);
    }

}

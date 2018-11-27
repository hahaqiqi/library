package com.november.acl.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.november.acl.dao.RoleAdminMapper;
import com.november.acl.dto.AdminDto;
import com.november.admin.dao.AdminMapper;
import com.november.model.Admin;
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

    public void changeAdmin(String idStr,int rid){
        if(StringUtils.isBlank(idStr)){
            roleAdminMapper.deleteByRoleId(rid);
            return;
        }
        String[] strs = StringUtils.split(idStr,",");
        //  转化
        List<Integer> ids = Lists.newArrayList(Arrays.asList(strs).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList()));
        List<Integer> adminIdsByRoleId = roleAdminMapper.getAdminIdsByRoleId(rid);
        if(ids.size() == adminIdsByRoleId.size()){
            if(adminIdsByRoleId.containsAll(ids)){
                return ;
            }
        }
        roleAdminMapper.deleteByRoleId(rid);
        batchRoleAdmin(ids,rid);
    }

    private void batchRoleAdmin(List<Integer> ids,int rid){
        roleAdminMapper.batchInsert(ids,rid,"admin");   //  TODO
    }

    @Override
    public List<AdminDto> packAdminList(List<Admin> list, int rid) {
        List<AdminDto> adminDtoList = Lists.newArrayList();
        List<Integer> adminIdsByRoleId = roleAdminMapper.getAdminIdsByRoleId(rid);
        for (Admin admin : list) {
            AdminDto dto = AdminDto.adapt(admin);
            if(adminIdsByRoleId.contains(dto.getId())){
                dto.setSelected("selected");
            }
            adminDtoList.add(dto);
        }
        return adminDtoList;
    }

    @Override
    public List<Integer> getRoleIdListByAdminId(int adminId) {
        return roleAdminMapper.getRoleIdsByAdminId(adminId);
    }

}

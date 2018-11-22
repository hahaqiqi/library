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

    @Resource
    private AdminMapper adminMapper;

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
        clearDisRoleAdmin(adminIdsByRoleId);
        for (Admin admin : list) {
            AdminDto dto = AdminDto.adapt(admin);
            if(adminIdsByRoleId.contains(dto.getId())){
                dto.setSelected("selected");
            }
            adminDtoList.add(dto);
        }
        return adminDtoList;
    }

    private void clearDisRoleAdmin(List<Integer> ids){
        if(CollectionUtils.isNotEmpty(ids)){
            for (Integer id : ids) {
                if(adminMapper.selectByPrimaryKey(id) == null){
                    roleAdminMapper.deleteByAdminId(id);
                }
            }
        }
    }

    /*public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);
    }

    @Transactional
    private void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }
    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }*/
}

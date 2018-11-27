package com.november.acl.service;

import com.november.acl.dto.AdminDto;
import com.november.model.Admin;

import java.util.List;

/**
 * @author skrT
 * @create 2018/11/21 8:59
 */

public interface RoleAdminService {

    void changeAdmin(String idStr,int rid);

    List<AdminDto> packAdminList(List<Admin> list,int rid);

    List<Integer> getRoleIdListByAdminId(int adminId);
}

package com.november.admin.service;

import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;

import java.util.List;

/**
 * @author skrT
 * @create 2018/11/20 21:19
 */
public interface AdminService {

    void save(AdminParam param);

    void update(AdminParam param);

    List<Admin> getAll();

}

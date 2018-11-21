package com.november.admin.service;

import com.november.admin.dao.AdminMapper;
import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/20 21:19
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public void save(AdminParam param) {

    }

    @Override
    public void update(AdminParam param) {

    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.getAll();
    }
}

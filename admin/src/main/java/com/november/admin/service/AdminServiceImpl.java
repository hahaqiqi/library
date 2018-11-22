package com.november.admin.service;

import com.google.common.collect.Lists;
import com.november.admin.dao.AdminMapper;
import com.november.admin.dto.AdminDto;
import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Override
    public List<AdminDto> getPageList(int page, int limit) {
        //  TODO
        page = (page - 1) * limit;
        List<Admin> all = adminMapper.getPageList(page,limit);
        List<AdminDto> adminDtoList = Lists.newArrayList();
        for (Admin admin : all) {
            AdminDto dto = AdminDto.adapt(admin);
            Date birthday = dto.getBirthday();
            int age = new Date().getYear() - birthday.getYear();
            dto.setAge(age);
            adminDtoList.add(dto);
        }
        return adminDtoList;
    }

    @Override
    public int countAll() {
        return adminMapper.countAll();
    }

    @Override
    public void delete(int adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }
}

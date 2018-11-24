package com.november.admin.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.november.admin.dao.AdminMapper;
import com.november.admin.dto.AdminDto;
import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.util.BeanValidator;
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
        BeanValidator.check(param);
        if (checkIdCard(param.getId(),param.getIdCard())) {
            throw new ParamException("身份证号已经存在");
        }
        Admin admin = Admin.builder().adminCode(param.getAdminCode()).adminPwd(param.getAdminPwd())
                .adminName(param.getAdminName()).birthday(param.getBirthday())
                .idCard(param.getIdCard()).remark(param.getRemark()).build();
        if(null == RequestHolder.getCurrentAdmin()){
            admin.setOperator("admin");
        }else{
            admin.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        admin.setOperateTime(new Date());
        adminMapper.insertSelective(admin);
    }

    @Override
    public void update(AdminParam param) {
        BeanValidator.check(param);
        if (checkIdCard(param.getId(),param.getIdCard())) {
            throw new ParamException("身份证号已经存在");
        }
        Admin before = adminMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的管理员不存在");
        Admin after = Admin.builder().id(param.getId()).adminCode(param.getAdminCode()).adminPwd(param.getAdminPwd())
                .adminName(param.getAdminName()).birthday(param.getBirthday())
                .idCard(param.getIdCard()).remark(param.getRemark()).build();
        if(null == RequestHolder.getCurrentAdmin()){
            after.setOperator("admin");
        }else{
            after.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }
        after.setOperateTime(new Date());
        adminMapper.updateByPrimaryKeySelective(after);
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

    private boolean checkIdCard(Integer id,String idCard){
        return adminMapper.countByIdCard(id,idCard) > 0;
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

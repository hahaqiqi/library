package com.november.admin.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.november.admin.dao.AdminMapper;
import com.november.admin.dto.AdminDto;
import com.november.admin.model.Admin;
import com.november.admin.param.AdminParam;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.log.Param.LogParam;
import com.november.log.commons.LogTypeInt;
import com.november.log.model.LogType;
import com.november.log.service.LogService;
import com.november.util.BeanValidator;
import com.november.util.JsonMapper;
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

    @Resource
    private LogService logService;

    @Override
    public void save(AdminParam param) {
        //  验证参数
        BeanValidator.check(param);
        //  检查身份证
        if (checkIdCard(param.getId(),param.getIdCard())) {
            throw new ParamException("身份证号已经存在");
        }
        //  对管理员进行初始化
        Admin admin = Admin.builder().adminCode(param.getAdminCode()).adminPwd(param.getAdminPwd())
                .adminName(param.getAdminName()).birthday(param.getBirthday())
                .idCard(param.getIdCard()).remark(param.getRemark()).build();
        //  添加操作人信息
        admin.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        //  添加操作时间
        admin.setOperateTime(new Date());
        //  插入数据库
        adminMapper.insertSelective(admin);
        //  插入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ADMIN_TYPE).targetId(admin.getId())
                .newValue(JsonMapper.obj2String(admin)).remark("添加管理员").build());
    }

    @Override
    public void update(AdminParam param) {
        //  验证参数
        BeanValidator.check(param);
        //  检查身份证
        if (checkIdCard(param.getId(),param.getIdCard())) {
            throw new ParamException("身份证号已经存在");
        }
        //  获得更新前的管理员
        Admin before = adminMapper.selectByPrimaryKey(param.getId());
        //  检查是否为空
        Preconditions.checkNotNull(before, "待更新的管理员不存在");
        //  初始化改变后的管理员
        Admin after = Admin.builder().id(param.getId()).adminCode(param.getAdminCode()).adminPwd(param.getAdminPwd())
                .adminName(param.getAdminName()).birthday(param.getBirthday())
                .idCard(param.getIdCard()).remark(param.getRemark()).build();
        //  添加操作管理员
        after.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        //  添加操作时间
        after.setOperateTime(new Date());
        //  更新入数据库
        adminMapper.updateByPrimaryKeySelective(after);
        //  插入日志
        logService.saveLog(LogParam.builder().type(LogTypeInt.ADMIN_TYPE).targetId(param.getId())
                .oldValue(JsonMapper.obj2String(before)).newValue(JsonMapper.obj2String(after)).remark("修改管理员").build());
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.getAll();
    }

    @Override
    public List<AdminDto> getPageList(int page, int limit) {
        //  获得当前页
        page = (page - 1) * limit;
        //  获得分页后的管理员
        List<Admin> all = adminMapper.getPageList(page,limit);
        //  初始化要返回的管理员集合
        List<AdminDto> adminDtoList = Lists.newArrayList();
        //  循环分页的管理员
        for (Admin admin : all) {
            //  转化
            AdminDto dto = AdminDto.adapt(admin);
            //  获得生日
            Date birthday = dto.getBirthday();
            //  计算年龄
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

    @Override
    public Admin getAdminByAdminCode(String adminCode) {
        return adminMapper.getAdminByAdminCode(adminCode);
    }
}

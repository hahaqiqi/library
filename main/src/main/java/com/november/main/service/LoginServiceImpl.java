package com.november.main.service;

import com.november.admin.dao.AdminMapper;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.main.param.LoginParam;
import com.november.admin.model.Admin;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author skrT
 * @create 2018/11/15 19:40
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Admin login(LoginParam param) {
        BeanValidator.check(param);
        Admin admin = adminMapper.getAdminByAdminCode(param.getAdminCode());
        if(null == admin){
            throw new ParamException("不存在该管理员!");
        }
        if(!(param.getAdminPwd().equals(admin.getAdminPwd()))){
            throw new ParamException("登录名或密码不正确!");
        }
        return admin;
    }

}

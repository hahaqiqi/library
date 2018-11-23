package com.november.main.service;

import com.november.main.param.LoginParam;
import com.november.admin.model.Admin;

/**
 * @author skrT
 * @create 2018/11/15 19:40
 */
public interface LoginService {

    Admin login(LoginParam param);

}

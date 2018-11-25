package com.november.acl.service;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.Map;

/**
 * @author skrT
 * @create 2018/11/25 16:30
 */
public interface ShiroService {

    Map<String, String> loadFilterChainDefinitions();

    void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean);
}

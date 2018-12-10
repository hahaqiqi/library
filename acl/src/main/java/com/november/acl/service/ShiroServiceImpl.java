package com.november.acl.service;

import com.november.acl.dao.AclMapper;
import com.november.acl.dao.RoleAclMapper;
import com.november.acl.dao.RoleMapper;
import com.november.acl.model.Acl;
import com.november.acl.model.Role;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("shiroService")
public class ShiroServiceImpl implements ShiroService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AclMapper aclMapper;

    @Resource
    private RoleAclMapper roleAclMapper;

    /**
     * 初始化权限
     */
    @Override
    public Map<String, String> loadFilterChainDefinitions() {
        List<Acl> acls = aclMapper.getAll();
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(acls)) {
//            String uris;
//            String[] uriArr;
            for (Acl acl : acls) {
                List<Integer> roleIds = roleAclMapper.getRoleIdByAclId(acl.getId());
                if (CollectionUtils.isEmpty(roleIds)) {
                    continue;
                }
                List<Role> roles = roleMapper.getByIdList(roleIds);
                if (CollectionUtils.isEmpty(roles)) {
                    continue;
                }
                List<String> rolesName = roles.stream().map(r -> r.getRoleName()).collect(Collectors.toList());
                StringBuffer sbf = new StringBuffer();
                for (int i = 0; i < rolesName.size(); i++) {
                    sbf.append(rolesName.get(i));
                    if (i != (rolesName.size() - 1)) {
                        sbf.append(',');
                    }
                }
                filterChainDefinitionMap.put(acl.getUrl(), "authc,roles[" + sbf.toString() + "]");
            }
        }
        filterChainDefinitionMap.put("/logout.html", "logout");
        // 设置登录的请求
        filterChainDefinitionMap.put("/login.json", "anon");
        // 设置静态资源可访问
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/extends/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/layui/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/spop-0.1.3/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }

    /**
     * 在对角色进行增删改操作时，需要调用此方法进行动态刷新
     *
     * @param shiroFilterFactoryBean
     */
    @Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
        }
    }
}

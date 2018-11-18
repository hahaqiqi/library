package com.november.acl.service;

import com.november.acl.model.Acl;
import com.november.acl.param.AclParam;
import com.november.util.PageQuery;
import com.november.util.PageResult;

/**
 * @author skrT
 * @create 2018/11/15 8:29
 */
public interface AclService {

    void save(AclParam param);

    void update(AclParam param);

    PageResult<Acl> getPageByAclModuleId(int parentId, PageQuery page);

}
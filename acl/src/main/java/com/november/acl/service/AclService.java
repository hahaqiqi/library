package com.november.acl.service;

import com.november.acl.dto.AclDto;
import com.november.acl.model.Acl;
import com.november.acl.param.AclParam;
import com.november.util.PageQuery;
import com.november.util.PageResult;

import java.util.List;

/**
 * @author skrT
 * @create 2018/11/15 8:29
 */
public interface AclService {

    /*void save(AclParam param);

    void update(AclParam param);*/

//    PageResult<Acl> getPageByAclModuleId(int parentId, PageQuery page);

    //  获得所有权限点
    List<AclDto> getAll(int rid);

    //  改变权限点赋予
    void changeAcl(String idStr,int rid);

}
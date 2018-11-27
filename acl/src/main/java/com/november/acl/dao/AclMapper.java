package com.november.acl.dao;

import com.november.acl.model.Acl;
import com.november.acl.model.OperType;
import com.november.util.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AclMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Acl record);

    int insertSelective(Acl record);

    Acl selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Acl record);

    int updateByPrimaryKey(Acl record);

    int countByParentId(@Param("parentId") int parentId);

    List<Acl> getPageByParentId(@Param("parentId") int parentId,@Param("page") PageQuery page);

    int countByNameAndParentId(@Param("parentId") int parentId, @Param("aclName") String aclName, @Param("id") Integer id);

    List<Acl> getAll();

    List<Acl> getByIdList(@Param("idList") List<Integer> idList);

    List<Acl> getByUrl(@Param("url") String url);

    List<OperType> getOperType();

    List<Acl> getRootAll();

    List<Acl> getAclsByOperType(@Param("operTypeId") List<Integer> operTypeId,@Param("parentId") int parentId);
}
package com.november.admin.dao;

import com.november.admin.model.Admin;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    List<Admin> getAll();

    int countAll();

    List<Admin> getPageList(@Param("page") int page,@Param("limit") int limit);

    Admin getAdminByAdminCode(@Param("adminCode") String adminCode);

    int countByIdCard(@Param("id") Integer id,@Param("idCard") String idCard);
}
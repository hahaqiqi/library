package com.november.admin.dto;

import com.november.admin.model.Admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.security.acl.Acl;

/**
 * @author skrT
 * @create 2018/11/21 15:44
 */
@Getter
@Setter
@ToString
public class AdminDto extends Admin {

    private int age;

    private String roleName;

    // 转化Acl为AclDto
    public static AdminDto adapt(Admin admin){
        AdminDto dto = new AdminDto();
        BeanUtils.copyProperties(admin,dto);
        return dto;
    }
}

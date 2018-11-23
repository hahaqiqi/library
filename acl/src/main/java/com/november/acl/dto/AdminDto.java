package com.november.acl.dto;

import com.november.acl.model.Acl;
import com.november.model.Admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author skrT
 * @create 2018/11/21 9:45
 */
@Getter
@Setter
@ToString
public class AdminDto extends Admin {

    private String selected;

    private String disabled;

    // 转化Admin为AdminDto
    public static AdminDto adapt(Admin admin){
        AdminDto dto = new AdminDto();
        BeanUtils.copyProperties(admin,dto);
        return dto;
    }
}

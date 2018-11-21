package com.november.acl.dto;

import com.google.common.collect.Lists;
import com.november.acl.model.Acl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author skrT
 * @create 2018/11/19 15:56
 */
@Getter
@Setter
@ToString
public class AclDto extends Acl {

    //  页面显示的名字
    private String name;

    //  页面显示的值
    private String value;

    // 是否要默认选中
    private boolean checked = false;

    // 下面的所有权限点
    private List<AclDto> list = Lists.newArrayList();

    // 转化Acl为AclDto
    public static AclDto adapt(Acl acl){
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl,dto);
        return dto;
    }

}

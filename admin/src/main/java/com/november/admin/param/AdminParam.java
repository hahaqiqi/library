package com.november.admin.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author skrT
 * @create 2018/11/21 14:40
 */
@Getter
@Setter
@ToString
public class AdminParam {

    private Integer id;

    @NotBlank(message = "管理员账号不能为空")
    @Length(min = 5,max = 20,message = "管理员账号长度要在5-20位之间")
    private String adminCode;

    @NotBlank(message = "管理员密码不能为空")
    @Length(min = 8,max = 20,message = "管理员密码长度要在8-20位之间")
    private String adminPwd;

    @NotBlank(message = "管理员真实姓名不能为空")
    @Length(min = 2,max = 20,message = "管理员真实姓名长度要在8-20位之间")
    private String adminName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "生日不能为空")
    private Date birthday;

    @NotBlank(message = "管理员身份证号不能为空")
    @Length(min = 18,max= 18,message = "管理员身份证号长度应为18")
    private String idCard;

    @Length(max = 200,message = "管理员备注长度不能超过200")
    private String remark;

}

package com.november.user.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserParam implements Serializable{
    private Integer id;

    @NotBlank(message = "用户名称不能为空")
    @Length(max = 40,message = "用户名称长度不能超过40")
    private String userName;

    private String userPhone;

    @NotBlank(message="邮箱不能为空")
    @Email(message="邮箱格式不正确")
    private String userEmail;


    private Integer userScore=100;

    private Double userBalance;

    @NotBlank(message = "身份证不能为空")
    @Length(min=18,max =18,message = "身份证长度为18")
    private String idCard;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "生日不能为空")
    private Date userBirthday;

    private Integer status;

    private String operator;

    private Date operateTime;

    private String remark;
}

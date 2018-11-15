package com.november.main.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author skrT
 * @create 2018/11/15 19:41
 */
@Getter
@Setter
@ToString
public class LoginParam {

    @NotBlank(message = "用户名不能为空")
    private String adminCode;

    @NotBlank(message = "用户密码不能为空")
    private String adminPwd;

}

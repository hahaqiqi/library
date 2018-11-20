package com.november.user.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserParam {

    private String userName;

    private String userPhone;

    private String userEmail;

    private Integer userScore;

    private Double userBalance;

    private String idCard;

    private Date userBirthday;

    private Integer status;

    private String operator;

    private String remark;
}

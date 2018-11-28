package com.november.user.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString
public class UserTypeParam implements Serializable{
    private Integer id;

    private String typeName;

    private Integer minScore;

    private Double discount;

    private Integer status;

    private String operator;

    private Date operateTime;

    private String remark;
}

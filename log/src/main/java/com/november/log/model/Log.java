package com.november.log.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
public class Log {
    private Integer id;

    private Integer type;

    private Integer targetid;

    private String operator;

    private Date operateTime;

    private String remark;

    private String logTypeName;
}
package com.november.book.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BookLeaseParam {
    private Integer id;

    private Integer bookId;

    private Integer userId;

    private Double bookPrice;

    private Integer status;

    private String operator;

    private Date operateTime;

    private Double price;

    private String finalOperator;

    private Date finalOperateTime;

    private String remark;

}
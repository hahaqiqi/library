package com.november.book.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class BookLeaseParam {
    private Integer id;

    private Integer bookId;

    private String serialNumber;

    private Integer userId;

    private Double bookPrice;

    private Double discount;

    private Integer status;

    private String operator;

    private Date operateTime;

    @NotNull(message = "收费不能为空")
    private Double price;

    private String finalOperator;

    private Date finalOperateTime;

    private String remark;

    private Integer page;

    private Integer limit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date starTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
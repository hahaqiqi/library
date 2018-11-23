package com.november.book.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book implements Serializable {
    private Integer id;

    private String bookName;

    private String bookCode;

    private String authorName;

    private Integer status;

    private Double price;

    private String pressName;

    private Integer bookTypeId;

    private Integer bookLeaseId;

    private Integer bookLossId;

    private Integer bookLeaseType;

    private Integer bookChcoType;

    private Integer bookSpaceId;

    private String operator;

    private Date operateTime;

    private String remark;


}
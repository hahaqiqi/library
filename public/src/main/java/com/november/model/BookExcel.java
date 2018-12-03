package com.november.model;/*
 *
 **/

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookExcel {
    private String bookName;

    private String bookCode;

    private String authorName;

    private String price;

    private String pressName;

    private String bookTypeId;

    private String bookChcoType;

    private String remark;
}

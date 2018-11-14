package com.bq.model;

import java.util.Date;

public class Book {
    private Integer id;

    private String bookName;

    private String bookCode;

    private String authorName;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode == null ? null : bookCode.trim();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? null : authorName.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName == null ? null : pressName.trim();
    }

    public Integer getBookTypeId() {
        return bookTypeId;
    }

    public void setBookTypeId(Integer bookTypeId) {
        this.bookTypeId = bookTypeId;
    }

    public Integer getBookLeaseId() {
        return bookLeaseId;
    }

    public void setBookLeaseId(Integer bookLeaseId) {
        this.bookLeaseId = bookLeaseId;
    }

    public Integer getBookLossId() {
        return bookLossId;
    }

    public void setBookLossId(Integer bookLossId) {
        this.bookLossId = bookLossId;
    }

    public Integer getBookLeaseType() {
        return bookLeaseType;
    }

    public void setBookLeaseType(Integer bookLeaseType) {
        this.bookLeaseType = bookLeaseType;
    }

    public Integer getBookChcoType() {
        return bookChcoType;
    }

    public void setBookChcoType(Integer bookChcoType) {
        this.bookChcoType = bookChcoType;
    }

    public Integer getBookSpaceId() {
        return bookSpaceId;
    }

    public void setBookSpaceId(Integer bookSpaceId) {
        this.bookSpaceId = bookSpaceId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
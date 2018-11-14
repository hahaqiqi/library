package com.bq.model;

import java.util.Date;

public class BookCode {
    private Integer id;

    private Integer bookPriceMin;

    private Integer bookPriceMax;

    private Double bookPrice;

    private String operator;

    private Date operateTime;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookPriceMin() {
        return bookPriceMin;
    }

    public void setBookPriceMin(Integer bookPriceMin) {
        this.bookPriceMin = bookPriceMin;
    }

    public Integer getBookPriceMax() {
        return bookPriceMax;
    }

    public void setBookPriceMax(Integer bookPriceMax) {
        this.bookPriceMax = bookPriceMax;
    }

    public Double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Double bookPrice) {
        this.bookPrice = bookPrice;
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
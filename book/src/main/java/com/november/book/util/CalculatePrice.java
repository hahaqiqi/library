package com.november.book.util;/*
 *
 **/

import com.november.util.TimeUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalculatePrice {

    public static String[] getPrice(double leasePrice, double leaseDiscount, Date leaseData) {
        DecimalFormat df = new DecimalFormat("#.00");
        String[] arr = new String[2];
        Date date2 = new Date();
        int day = TimeUtil.getDifferenceDay(leaseData, date2);
        double excess = 0.2;
        double price;
        String formulaMode = "";
        if (day > 30) {
            if (leasePrice > excess / 2) {
                excess = leasePrice * 2;
            }
            double normalPrice = leasePrice * 30 * leaseDiscount;  //30天之内的收费
            double excessPrice = (day - 30) * excess;    //超出时间的费用
            price = normalPrice + excessPrice;
            formulaMode = leasePrice + "(元/天) * " + 30 + "(天) * " + leaseDiscount + " + " + excess + "(元/超出天数) * " + (day - 30) + "(超出天数)";
        } else {
            if (day <= 0) {
                day = 1;
            }
            price = leasePrice * day * leaseDiscount;
            formulaMode = leasePrice + "(元/天) * " + day + "(天) * " + leaseDiscount;
        }
        arr[0] = df.format(price);
        arr[1] = formulaMode;
        return arr;
    }


}

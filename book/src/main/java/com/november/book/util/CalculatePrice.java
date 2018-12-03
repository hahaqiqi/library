package com.november.book.util;/*
 *
 **/

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculatePrice {
    public static String[] getPrice(double leasePrice, double leaseDiscount, Date leaseData) {
        String[] arr = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date2 = new Date();
        long startDay = Long.parseLong(sdf.format(leaseData));
        long endDay = Long.parseLong(sdf.format(date2));
        Long day = endDay - startDay;
        double price = leasePrice * day * leaseDiscount;
        arr[0] = (double) Math.round(price * 100) / 100 + "";
        arr[1] = leasePrice + "(元/天) * " + day + "(天) * " + leaseDiscount;
        return arr;
    }
}

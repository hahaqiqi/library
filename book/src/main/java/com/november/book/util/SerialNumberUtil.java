package com.november.book.util;/*
 *
 **/

import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialNumberUtil {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static String getCerialNumber(){
        StringBuffer cerialNumber=new StringBuffer("SYY-");
        Date data=new Date();
        cerialNumber.append(dateFormat.format(data));
        return cerialNumber.toString();
    }
}

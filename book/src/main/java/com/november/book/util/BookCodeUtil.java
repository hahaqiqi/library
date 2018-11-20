package com.november.book.util;/*
 *
 **/

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BookCodeUtil {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");
    static Random random=new Random();
    public static String getBookCode(){
        StringBuffer bookCode=new StringBuffer("SYY-");
        Date data=new Date();
        bookCode.append(dateFormat.format(data));
        bookCode.append(random.nextInt(100000)+100000);
        return bookCode.toString();
    }
}

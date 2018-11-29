package com.november.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTimr() {
        return "2018";
    }

    public static String dateFormatToStr(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    public static Date dateFormat(Date date) {
        TimeZone timeZoneSH = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        outputFormat.setTimeZone(timeZoneSH);
        String time = outputFormat.format(date);
        System.out.println(time);
        try {
            return outputFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int compareDate(String str1,String str2){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sd.parse(str1);
            Date date2 = sd.parse(str2);
            if(date1.getTime()>date2.getTime()){
                return 1;
            }else if(date1.getTime()<date2.getTime()){
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

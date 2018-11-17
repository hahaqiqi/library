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

}

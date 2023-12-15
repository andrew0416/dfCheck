package com.example.dfcheck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // 현재 날짜를 기반으로 이번주 목요일 날짜를 반환
    public static Date getStartOfThisWeekThursday(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 현재 요일을 구함 (일요일: 1, 월요일: 2, ..., 토요일: 7)
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // (현재 요일 + 4) % 7 을 통해 현재 날짜의 목요일로 이동
        if(currentDayOfWeek > 5){
            calendar.add(Calendar.DATE, -(currentDayOfWeek-5));
        }
        else if (currentDayOfWeek < 5){
            calendar.add(Calendar.DATE, -(currentDayOfWeek+2));
        }
        else{
            if (Calendar.HOUR_OF_DAY < 6){
                calendar.add(Calendar.DATE, -(currentDayOfWeek+2));
            }
        }


        // 목요일의 6시로 설정
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    // Date를 원하는 형식의 문자열로 변환
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(date);
    }

    // 예시: "yyyy-MM-dd HH:mm:ss"
    public static String getCurrentDateTimeString(String pattern) {
        return formatDate(new Date(), pattern);
    }

    // 특정 날짜를 예시의 패턴으로 문자열로 변환
    public static String formatDateAsString(Date date, String pattern) {
        return formatDate(date, pattern);
    }

    // 특정 날짜를 원하는 패턴으로 문자열로 변환
    public static String formatDateAsString(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm");
    }

    public static String formatForAPI(Date date) {
        return formatDate(date, "yyyyMMdd")+"T"+formatDate(date, "HHmm");
    }
}

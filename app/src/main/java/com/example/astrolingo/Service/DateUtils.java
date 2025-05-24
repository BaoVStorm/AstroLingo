package com.example.astrolingo.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    // Lấy ngày hiện tại
    public static String getCurrentDate() {
        Date today = new Date();
        return formatter.format(today);
    }

    // Cộng/trừ năm, tháng, ngày vào ngày hiện tại
    public static String calculateDate(int years, int months, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years);
        calendar.add(Calendar.MONTH, months);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return formatter.format(calendar.getTime());
    }

    public static String formatDate(String isoDateStr) {
        try {
            // 1. Định dạng đầu vào (ISO 8601)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // 2. Định dạng đầu ra (dd-MM-yyyy)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = inputFormat.parse(isoDateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // hoặc trả về chuỗi mặc định
        }
    }
}
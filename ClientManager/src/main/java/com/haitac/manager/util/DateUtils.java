package com.haitac.manager.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Tiện ích ngày tháng và sinh mã key ngẫu nhiên
 */
public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String format(long timestamp) {
        if (timestamp <= 0) return "Vĩnh viễn";
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(timestamp));
    }

    public static long parse(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || "Vĩnh viễn".equalsIgnoreCase(dateStr.trim())) {
            return -1;
        }
        try {
            return new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateStr.trim()).getTime();
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr.trim()).getTime();
            } catch (Exception ex) {
                return -1;
            }
        }
    }

    public static long addDays(long fromTime, int days) {
        if (fromTime <= 0) fromTime = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fromTime);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTimeInMillis();
    }

    public static long addMonths(long fromTime, int months) {
        if (fromTime <= 0) fromTime = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fromTime);
        cal.add(Calendar.MONTH, months);
        return cal.getTimeInMillis();
    }

    /**
     * Sinh mã Key ngẫu nhiên định dạng: PREFIX-XXXX-YYYY-ZZZZ
     */
    public static String generateKey(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = "HTTH";
        }
        prefix = prefix.trim().toUpperCase();
        return prefix + "-" + randomBlock(4) + "-" + randomBlock(4) + "-" + randomBlock(4);
    }

    private static String randomBlock(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}

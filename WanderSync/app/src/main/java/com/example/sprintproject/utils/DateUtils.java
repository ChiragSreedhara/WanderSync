package com.example.sprintproject.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// helper class to manipulate date related data
public class DateUtils {

    private DateUtils() {
    }
    private static Locale locale = Locale.getDefault();
    private static DateFormat dfMed = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
    private static DateFormat dfShort = DateFormat.getDateInstance(DateFormat.SHORT, locale);

    // create Date object at 00:00 from year, month, day
    public static Date createDate(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(year, month, day, 0, 0, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    // convert number of days to number of milliseconds
    public static long daysToMillis(int days) {
        return (long) days * (24 * 60 * 60 * 1000);
    }

    // convert number of milliseconds to number of days
    public static int millisToDays(long ms) {
        return (int) (ms / (24 * 60 * 60 * 1000));
    }

    // convert timestamp in milliseconds to Date
    public static Date millisToDate(long ms) {
        return new Date(ms);
    }

    // convert timestamp to date string for display
    public static String formatShort(long ms) {
        return dfShort.format(ms);
    }
    public static String formatMedium(long ms) {
        return dfMed.format(ms);
    }

    // convert date to date string for display
    public static String formatShort(Date date) {
        return dfShort.format(date);
    }
    public static String formatMedium(Date date) {
        return dfMed.format(date);
    }

    // convert date string back to date
    public static Date parseShort(String dateStringShort) throws ParseException {
        return dfShort.parse(dateStringShort);
    }
    public static Date parseMedium(String dateStringMedium) throws ParseException {
        return dfShort.parse(dateStringMedium);
    }

    // get locale and update date string formatter
    public static void updateDateFormatter() {
        locale = Locale.getDefault();
        dfMed = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        dfShort = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    }
}

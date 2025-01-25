package com.example.sprintproject.model;

import java.text.DateFormat;
import java.util.Locale;

// model to store start timestamp, end timestamp, duration.
// date and/or time can be derived from timestamps.
// for Dining Reservation, use start date only
// for Trip, Accommodation, use both dates
// for Destination, use both dates and duration
public class DateAndTimeModel implements IDateAndTimeModel {
    private long startDate;
    private long endDate;
    private long duration;   // # of milliseconds

    private static Locale locale = Locale.getDefault();
    private static DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

    public DateAndTimeModel() {
        startDate = 0;
        endDate = 0;
        duration = -1;
    }

    // convert timestamp to date string for display
    public static String formatTimestamp(long ms) {
        return df.format(ms);
    }

    // get locale and update date string formatter
    public static void updateDateFormatter() {
        locale = Locale.getDefault();
        df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
    }

    // getters
    @Override
    public long getStartDate() {
        return startDate;
    }

    @Override
    public long getEndDate() {
        return endDate;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    // setters
    @Override
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }
}

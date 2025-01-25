package com.example.sprintproject.model;

public interface IDateAndTimeModel {
    //getters
    long getStartDate();
    long getEndDate();
    long getDuration();

    // setters
    void setStartDate(long startDate);
    void setEndDate(long endDate);
    void setDuration(long duration);
}

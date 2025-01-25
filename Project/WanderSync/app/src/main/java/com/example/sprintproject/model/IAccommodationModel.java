package com.example.sprintproject.model;

import java.util.Date;

public interface IAccommodationModel {
    // getters
    Date getCheckInDate();
    Date getCheckOutDate();
    String getLocation();
    int getNumRooms();
    String getRoomType();
    boolean getExpired();

    // setters
    void setCheckInDate(Date checkInDate);
    void setCheckOutDate(Date checkOutDate);
    void setLocation(String location);
    void setNumRooms(int numRooms);
    void setRoomType(String roomType);
    void toggleExpired();
}

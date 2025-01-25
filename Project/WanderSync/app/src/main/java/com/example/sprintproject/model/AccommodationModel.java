package com.example.sprintproject.model;

import com.example.sprintproject.utils.DateUtils;

import java.util.Date;
import java.util.Objects;

public class AccommodationModel implements IAccommodationModel {
    private IDateAndTimeModel checkInOutDate;
    private String location;
    private int numRooms;
    private String roomType;
    private boolean expired = false;

    public AccommodationModel() {
        this.checkInOutDate = new DateAndTimeModel();
        this.location = "";
        this.numRooms = 0;
        this.roomType = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccommodationModel a = (AccommodationModel) o;
        return getCheckInDate().equals(a.getCheckInDate())
                && getCheckOutDate().equals(a.getCheckOutDate())
                && location.equals(a.getLocation())
                && numRooms == a.getNumRooms()
                && roomType.equals(a.getRoomType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCheckInDate(), getCheckOutDate(), location, numRooms, roomType);
    }

    @Override
    public Date getCheckInDate() {
        return DateUtils.millisToDate(checkInOutDate.getStartDate());
    }

    @Override
    public Date getCheckOutDate() {
        return DateUtils.millisToDate(checkInOutDate.getEndDate());
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public int getNumRooms() {
        return numRooms;
    }

    @Override
    public String getRoomType() {
        return roomType;
    }

    @Override
    public boolean getExpired() {
        return expired;
    }

    @Override
    public void setCheckInDate(Date checkInDate) {
        this.checkInOutDate.setStartDate(checkInDate.getTime());
    }

    @Override
    public void setCheckOutDate(Date checkOutDate) {
        this.checkInOutDate.setEndDate(checkOutDate.getTime());
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    @Override
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public void toggleExpired() {
        this.expired = !this.expired;
    }
}

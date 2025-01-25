package com.example.sprintproject.utils;

import com.example.sprintproject.model.IAccommodationModel;

import java.util.Comparator;

public class AccommodationCheckInDateComparator implements Comparator<IAccommodationModel> {
    @Override
    public int compare(IAccommodationModel m1, IAccommodationModel m2) {
        return m1.getCheckInDate().compareTo(m2.getCheckInDate());
    }
}

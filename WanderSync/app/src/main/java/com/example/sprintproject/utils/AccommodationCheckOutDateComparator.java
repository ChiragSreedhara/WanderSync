package com.example.sprintproject.utils;

import com.example.sprintproject.model.IAccommodationModel;

import java.util.Comparator;

public class AccommodationCheckOutDateComparator  implements Comparator<IAccommodationModel> {
    @Override
    public int compare(IAccommodationModel m1, IAccommodationModel m2) {
        return m1.getCheckOutDate().compareTo(m2.getCheckOutDate());
    }
}

package com.example.sprintproject.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.AccommodationModel;
import com.example.sprintproject.model.IAccommodationModel;
import com.example.sprintproject.utils.AccommodationCheckInDateComparator;
import com.example.sprintproject.utils.AccommodationCheckOutDateComparator;
import com.example.sprintproject.utils.AccommodationRoomTypeComparator;
import com.example.sprintproject.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccommodationsViewModel extends ViewModel {
    private MutableLiveData<List<IAccommodationModel>> accommodations = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private static final DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.
            getInstance().getReference();
    private String tripId;
    private DatabaseReference accommodationsReference;

    // new database to maybe move to, currently for accommodations only
    private static final String ACCOMMODATIONS_DATABASE = "accommodation";

    public AccommodationsViewModel() {
        fetchTripId();
    }

    // read trip id of user, then load accommodations
    private void fetchTripId() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(uid);

        userRef.child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class) != null) {
                    tripId = snapshot.getValue(String.class);
                } else {
                    tripId = "trip" + uid;
                    userRef.child("trip").setValue(tripId);
                }
                accommodationsReference = DATABASE_REFERENCE.child(ACCOMMODATIONS_DATABASE).
                        child(tripId).child("accommodations");
                loadAccommodations();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public void loadAccommodations() {
        if (tripId != null) {
            accommodationsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<IAccommodationModel> accommodationsList = new ArrayList<>();
                    for (DataSnapshot accommodationSnapshot : snapshot.getChildren()) {
                        AccommodationModel accommodation = accommodationSnapshot.
                                getValue(AccommodationModel.class);
                        accommodationsList.add(accommodation);
                    }
                    accommodations.setValue(accommodationsList);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    // toggle expired of item on list and return new status
    public boolean toggleExpired(int index) {
        List<IAccommodationModel> tempList = accommodations.getValue();
        IAccommodationModel tempAccommodation = tempList.get(index);
        tempAccommodation.toggleExpired();
        tempList.set(index, tempAccommodation);
        accommodations.setValue(tempList);
        saveAccommodations();
        return tempAccommodation.getExpired();
    }

    public void saveAccommodations() {
        accommodationsReference.setValue(accommodations.getValue());
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<List<IAccommodationModel>> getAccommodations() {
        return accommodations;
    }

    public void clearToastMessage() {
        toastMessage.setValue(null);
    }

    private IAccommodationModel createAccommodation(String checkInDate, String checkOutDate,
                                              String location, String numRooms, String roomType) {
        // check empty input
        if (TextUtils.isEmpty(checkInDate) || TextUtils.isEmpty(checkOutDate)
                || TextUtils.isEmpty(location) || TextUtils.isEmpty(numRooms)
                || TextUtils.isEmpty(roomType)) {
            toastMessage.setValue("Please fill in all fields");
            return null;
        }

        // create accommodation model
        AccommodationModel accommodationModel = new AccommodationModel();
        // input from spinner should not throw exceptions
        try {
            // parse check in date
            accommodationModel.setCheckInDate(DateUtils.parseShort(checkInDate));
            // parse check out date
            accommodationModel.setCheckOutDate(DateUtils.parseShort(checkOutDate));
            accommodationModel.setLocation(location);
            // parse number of rooms
            accommodationModel.setNumRooms(Integer.parseInt(numRooms));
            accommodationModel.setRoomType(roomType);
        } catch (ParseException pe) {
            toastMessage.setValue("Wrong date format");
            return null;
        } catch (NumberFormatException nfe) {
            toastMessage.setValue("Wrong number of rooms format");
            return null;
        }

        return accommodationModel;
    }

    public boolean isAccommodationDuplicate(IAccommodationModel accommodation) {
        Set<IAccommodationModel> accommodationsSet = new HashSet<>(accommodations.getValue());
        // add is false if set already contains element
        if (!accommodationsSet.add(accommodation)) {
            toastMessage.setValue("Cannot add duplicated accommodation");
            return true;
        }
        return false;
    }

    public void addAccommodation(String checkInDate, String checkOutDate, String location,
                                 String numRooms, String roomType) {
        // validate inputs
        IAccommodationModel accommodationModel = createAccommodation(checkInDate, checkOutDate,
                location, numRooms, roomType);
        if (accommodationModel == null) {
            return;
        }

        // check for duplicate. do not add if isDuplicate is true
        if (isAccommodationDuplicate(accommodationModel)) {
            return;
        }

        List<IAccommodationModel> tempList = accommodations.getValue();
        tempList.add(accommodationModel);
        accommodations.setValue(tempList);

        // update changes to database
        saveAccommodations();
    }

    // ways to sort accommodations
    public void sortAccommodationsCheckIn() {
        List<IAccommodationModel> tempList = accommodations.getValue();
        Collections.sort(tempList, new AccommodationCheckInDateComparator());
        accommodations.setValue(tempList);
        saveAccommodations();
    }

    public void sortAccommodationsCheckOut() {
        List<IAccommodationModel> tempList = accommodations.getValue();
        Collections.sort(tempList, new AccommodationCheckOutDateComparator());
        accommodations.setValue(tempList);
        saveAccommodations();
    }

    public void sortAccommodationsRoomType() {
        List<IAccommodationModel> tempList = accommodations.getValue();
        Collections.sort(tempList, new AccommodationRoomTypeComparator());
        accommodations.setValue(tempList);
        saveAccommodations();
    }
}

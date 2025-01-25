package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiningViewModel extends ViewModel {
    private final MutableLiveData<List<Map<String, Object>>> reservations = new MutableLiveData<>(
            new ArrayList<>());
    private static final String DINING = "dining";
    private DatabaseReference tripsRef;
    private String tripId;
    private static final String LOCATION = "location";
    private static final String WEBSITE = "website";
    private static final String EXPIRED = "expired";

    private static final List<Map<String, Object>> RESERVATIONS_T = new ArrayList<>();


    public DiningViewModel() {
        tripsRef = FirebaseDatabase.getInstance().getReference("trips");
        fetchTripId();
    }

    private void fetchTripId() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(uid);

        userRef.child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class) != null) {
                    tripId = snapshot.getValue(String.class);
                    loadReservations();
                } else {
                    tripId = "trip" + uid;
                    userRef.child("trip").setValue(tripId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    private void loadReservations() {
        if (tripId != null) {
            tripsRef.child(tripId).child(DINING).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                public void onDataChange(DataSnapshot snapshot) {
                            List<Map<String, Object>> reservationList = new ArrayList<>();
                            for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                                Map<String, Object> reservationData = new HashMap<>();
                                reservationData.put("id", reservationSnapshot.getKey());
                                reservationData.put(LOCATION, reservationSnapshot.
                                        child(LOCATION)
                                        .getValue(String.class));
                                reservationData.put(WEBSITE, reservationSnapshot.
                                        child(WEBSITE)
                                        .getValue(String.class));
                                reservationData.put("time", reservationSnapshot.
                                        child("time")
                                        .getValue(String.class));
                                reservationData.put(EXPIRED, reservationSnapshot.
                                        child(EXPIRED)
                                        .getValue(Boolean.class));

                                reservationList.add(reservationData);
                            }
                            reservations.setValue(reservationList);
                        }

                        @Override
                public void onCancelled(DatabaseError error) {
                            // Handle error
                            }
                        });
        }
    }

    public void addReservation(String location, String website, String time, boolean expired) {
        if (tripId != null) {
            DatabaseReference reservationRef = tripsRef.child(tripId).child(DINING)
                    .push();
            reservationRef.child(LOCATION).setValue(location);
            reservationRef.child(WEBSITE).setValue(website);
            reservationRef.child("time").setValue(time);
            reservationRef.child(EXPIRED).setValue(expired)
                    .addOnSuccessListener(aVoid -> loadReservations());
        }
    }

    public void updateReservation(String location, boolean isExpired) {
        List<Map<String, Object>> reservationsList = reservations.getValue();

        if (reservationsList != null) {
            for (Map<String, Object> reservation : reservationsList) {
                if (reservation.get(LOCATION).equals(location)) {
                    String reservationId = (String) reservation.get("id"); // Retrieve reservationID

                    // Update expired status locally
                    reservation.put(EXPIRED, isExpired);

                    // Update expired status in Firebase
                    tripsRef.child(tripId).child(DINING).child(reservationId)
                            .child(EXPIRED).setValue(isExpired)
                            .addOnSuccessListener(aVoid ->
                                    // Notify observers of the change
                                    reservations.setValue(reservationsList)
                            )
                            .addOnFailureListener(e -> {
                                // Optional: handle failure case
                            });
                    break;
                }
            }
        }
    }

    public LiveData<List<Map<String, Object>>> getReservations() {
        return reservations;
    }



    // Dummy addReservation method for testing logic
    public static void addReservationT(
            String location, String website, String time, boolean expired) {
        if (isDuplicateReservation(location, time)) {
            return; // Avoid adding duplicate reservations
        }
        Map<String, Object> reservation = new HashMap<>();
        reservation.put(LOCATION, location);
        reservation.put(WEBSITE, website);
        reservation.put("time", time);
        reservation.put(EXPIRED, expired);
        RESERVATIONS_T.add(reservation);
    }

    // Dummy updateReservation method for testing logic
    public static void updateReservationT(String location, boolean isExpired) {
        for (Map<String, Object> reservation : RESERVATIONS_T) {
            if (reservation.get(LOCATION).equals(location)) {
                reservation.put(EXPIRED, isExpired);
                break;
            }
        }
    }

    // Dummy method to check for duplicate reservations
    private static boolean isDuplicateReservation(String location, String time) {
        for (Map<String, Object> reservation : RESERVATIONS_T) {
            if (reservation.get(LOCATION).equals(location)
                    && reservation.get("time").equals(time)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to retrieve the reservations for testing
    public static List<Map<String, Object>> getReservationsT() {
        return RESERVATIONS_T;
    }
}

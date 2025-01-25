package com.example.sprintproject.viewmodel;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.DateAndTimeModel;
import com.example.sprintproject.model.DestinationModel;
import com.example.sprintproject.model.IDateAndTimeModel;
import com.example.sprintproject.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestinationsViewModel extends ViewModel {
    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<Integer> duration = new MutableLiveData<>();
    private MutableLiveData<String> newStartDate = new MutableLiveData<>();
    private MutableLiveData<String> newEndDate = new MutableLiveData<>();
    private MutableLiveData<Integer> totalDays = new MutableLiveData<>(0);

    private IDateAndTimeModel allocatedVacationTimeModel;
    private IDateAndTimeModel newAllocatedVacationTimeModel;

    private DatabaseReference databaseReference;
    private static final String USERS_DATABASE = "users";
    private String uid;
    private static final String TRIPS_DATABASE = "trips";
    private String tripId;
    private String tripPath;
    private static final String TRIP_NODE = "trip";
    private static final String ALLOCATED_VACATION_TIME_NODE = "allocatedVacationTime";

    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<List<DestinationModel>> destinations =
            new MutableLiveData<>(new ArrayList<>());
    private DatabaseReference destinationsReference;
    private static final String DESTINATIONS_DATABASE = "destination";
    private static final DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.
            getInstance().getReference();
    private MutableLiveData<Integer> plannedVacationTime = new MutableLiveData<>();
    private static final String PLANNED_VACATION_TIME_NODE = "plannedVacationTime";
    private DatabaseReference plannedReference;

    public DestinationsViewModel() {
        allocatedVacationTimeModel = new DateAndTimeModel();
        newAllocatedVacationTimeModel = new DateAndTimeModel();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tripId = String.format("trip%s", uid);
        tripPath = String.format("%s/%s", TRIPS_DATABASE, tripId);

        databaseReference.child(USERS_DATABASE).child(uid).child(TRIP_NODE).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getValue(String.class) != null) {
                            tripId = snapshot.getValue(String.class);
                            tripPath = String.format("%s/%s", TRIPS_DATABASE, tripId);
                        } else {
                            databaseReference.child(USERS_DATABASE).child(uid).
                                    child(TRIP_NODE).setValue(tripId);
                        }
                        listenAllocatedVacationTime(tripPath);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //Throw error if necessary
                    }
                });

        DateAndTimeModel.updateDateFormatter();
        fetchTripId();
    }

    // read trip id of user, then load destinations
    private void fetchTripId() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(USERS_DATABASE)
                .child(currentUserId);

        userRef.child("trip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class) != null) {
                    tripId = snapshot.getValue(String.class);
                } else {
                    tripId = "trip" + currentUserId;
                    userRef.child("trip").setValue(tripId);
                }
                destinationsReference = DATABASE_REFERENCE.child(DESTINATIONS_DATABASE).
                        child(tripId).child("destinations");
                plannedReference = databaseReference.child(TRIPS_DATABASE).child(tripId).
                        child(PLANNED_VACATION_TIME_NODE);
                loadDestinations();
                loadPlannedVacationTime();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public void loadDestinations() {
        if (tripId != null) {
            destinationsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<DestinationModel> destinationsList = new ArrayList<>();
                    for (DataSnapshot destinationSnapshot : snapshot.getChildren()) {
                        DestinationModel destination = destinationSnapshot.
                                getValue(DestinationModel.class);
                        destinationsList.add(destination);
                    }
                    destinations.setValue(destinationsList);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    public void loadPlannedVacationTime() {
        if (tripId != null) {
            databaseReference.child(tripPath).child(PLANNED_VACATION_TIME_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.getValue(Integer.class) != null) {
                                plannedVacationTime.setValue(snapshot.getValue(Integer.class));
                            } else {
                                plannedVacationTime.setValue(0);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle error
                        }
                    });
        }
    }

    public LiveData<String> getStartDate() {
        return startDate; }
    public LiveData<String> getEndDate() {
        return endDate; }
    public LiveData<Integer> getDuration() {
        return duration; }
    public LiveData<String> getNewStartDate() {
        return newStartDate; }
    public LiveData<String> getNewEndDate() {
        return newEndDate; }
    public LiveData<Integer> getTotalDays() {
        return totalDays; }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public void clearToastMessage() {
        toastMessage.setValue(null);
    }

    public LiveData<List<DestinationModel>> getDestinations() {
        return destinations;
    }
    public LiveData<Integer> getPlannedVacationTime() {
        return plannedVacationTime;
    }

    public void setNewStartDate(int y, int m, int d) {
        Calendar date = Calendar.getInstance();
        date.set(y, m, d, 0, 0, 0);
        date.set(Calendar.MILLISECOND, 0);
        newStartDate.setValue("Change to: " + DateAndTimeModel.
                formatTimestamp(date.getTimeInMillis()));
        newAllocatedVacationTimeModel.setStartDate(date.getTimeInMillis());
    }

    public void setNewEndDate(int y, int m, int d) {
        Calendar date = Calendar.getInstance();
        date.set(y, m, d, 0, 0, 0);
        date.set(Calendar.MILLISECOND, 0);
        newEndDate.setValue("Change to: " + DateAndTimeModel.
                formatTimestamp(date.getTimeInMillis()));
        newAllocatedVacationTimeModel.setEndDate(date.getTimeInMillis());
    }

    public void setNewDuration(int days) {
        if (days < 1) {
            days = 1;
        }
        newAllocatedVacationTimeModel.setDuration(DateUtils.daysToMillis(days - 1));
    }

    public void calculateThirdField() throws IllegalArgumentException {
        long startFilled = newAllocatedVacationTimeModel.getStartDate();
        long endFilled = newAllocatedVacationTimeModel.getEndDate();
        long durationFilled = newAllocatedVacationTimeModel.getDuration();

        if ((startFilled != 0 ? 1 : 0) + (endFilled != 0 ? 1 : 0)
                + (durationFilled != -1 ? 1 : 0) != 2) {
            throw new IllegalArgumentException("Need exactly 2 fields to be filled");
        }

        if (startFilled == 0) {
            long calculatedStart = endFilled - durationFilled;
            if (calculatedStart < 0) {
                throw new
                        IllegalArgumentException("Calculated start date is invalid.");
            }
            newAllocatedVacationTimeModel.setStartDate(calculatedStart);
            newStartDate.setValue("Start: " + DateAndTimeModel.formatTimestamp(calculatedStart));
        } else if (endFilled == 0) {
            long calculatedEnd = startFilled + durationFilled;
            newAllocatedVacationTimeModel.setEndDate(calculatedEnd);
            newEndDate.setValue("End: " + DateAndTimeModel.formatTimestamp(calculatedEnd));
        } else {
            if (startFilled > endFilled) {
                throw new
                        IllegalArgumentException("End date cannot be earlier than start date");
            }
            long calculatedDuration = endFilled - startFilled;
            newAllocatedVacationTimeModel.setDuration(calculatedDuration);
            duration.setValue(DateUtils.millisToDays(calculatedDuration) + 1);
        }
    }

    public void updateAllocatedVacationTime() {
        allocatedVacationTimeModel = newAllocatedVacationTimeModel;
        databaseReference.child(tripPath).child(ALLOCATED_VACATION_TIME_NODE).
                setValue(allocatedVacationTimeModel)
                .addOnSuccessListener(aVoid -> calculateTotalDays());
    }

    private void calculateTotalDays() {
        long start = allocatedVacationTimeModel.getStartDate();
        long end = allocatedVacationTimeModel.getEndDate();

        if (start != 0 && end != 0 && end >= start) {
            long totalDaysInTrip = TimeUnit.MILLISECONDS.toDays(end - start) + 1;
            totalDays.setValue((int) totalDaysInTrip);
        } else {
            totalDays.setValue(0);
        }
    }

    public void clearEnteredAllocatedVacationTime() {
        newAllocatedVacationTimeModel = new DateAndTimeModel();
        newStartDate.setValue("");
        newEndDate.setValue("");
    }

    public void listenAllocatedVacationTime(String tripPath) {
        databaseReference.child(tripPath).child(ALLOCATED_VACATION_TIME_NODE).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getValue(DateAndTimeModel.class)
                                != null && dataSnapshot.getValue(DateAndTimeModel.class).
                                getDuration() != -1L) {
                            DateAndTimeModel firebaseAVT = dataSnapshot.
                                    getValue(DateAndTimeModel.class);
                            allocatedVacationTimeModel = firebaseAVT;
                            startDate.setValue(DateAndTimeModel.formatTimestamp(firebaseAVT.
                                    getStartDate()));
                            endDate.setValue(DateAndTimeModel.
                                    formatTimestamp(firebaseAVT.getEndDate()));
                            duration.setValue(DateUtils.
                                    millisToDays(firebaseAVT.getDuration()) + 1);
                            calculateTotalDays();
                        } else {
                            databaseReference.child(tripPath).child(ALLOCATED_VACATION_TIME_NODE).
                                    setValue(allocatedVacationTimeModel);
                            startDate.setValue("");
                            endDate.setValue("");
                            duration.setValue(0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Throw if needed
                    }
                });
    }

    public void addDestination(String destination, String start, String end) {
        // check empty input
        if (TextUtils.isEmpty(destination) || TextUtils.isEmpty(start)
                || TextUtils.isEmpty(end)) {
            toastMessage.setValue("Please fill out all fields");
            return;
        }
        // create destination model
        DestinationModel destinationModel = new DestinationModel();
        destinationModel.setDestination(destination);
        try {
            // parse start and end dates
            long d1 = DateUtils.parseShort(start).getTime();
            long d2 = DateUtils.parseShort(end).getTime();
            if (d2 - d1 < 0) {
                toastMessage.setValue("End Date mst not be later than Start Date");
                return;
            }
            destinationModel.setDuration(DateUtils.millisToDays(d2 - d1) + 1);
        } catch (ParseException pe) {
            toastMessage.setValue("Wrong date format");
            return;
        }

        List<DestinationModel> tempList = destinations.getValue();
        tempList.add(destinationModel);
        destinations.setValue(tempList);

        // update changes to database
        saveDestinations();
        addAndSavePlannedDays(destinationModel.getDuration());
    }

    public void saveDestinations() {
        destinationsReference.setValue(destinations.getValue());
    }

    public void addAndSavePlannedDays(int days) {
        plannedVacationTime.setValue(plannedVacationTime.getValue() + days);
        plannedReference.setValue(plannedVacationTime.getValue());
    }
}

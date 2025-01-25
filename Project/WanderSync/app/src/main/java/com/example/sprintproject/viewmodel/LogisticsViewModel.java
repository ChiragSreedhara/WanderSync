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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogisticsViewModel extends ViewModel {
    private final MutableLiveData<Long> allocatedDays = new MutableLiveData<>();
    private final MutableLiveData<List<String>> contributors =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> invalidUserId = new MutableLiveData<>(false);
    private final MutableLiveData<List<String>> notes = new MutableLiveData<>(new ArrayList<>());

    private DatabaseReference userRef;
    private DatabaseReference tripsRef; // Reference to trips database
    private String tripId; // To store the trip ID

    public LogisticsViewModel() {
        //Constructor method
    }

    public void start() {
        userRef = FirebaseDatabase.getInstance().getReference("users");
        tripsRef = FirebaseDatabase.getInstance().getReference("trips"); // Initialize trips

        fetchTripId(); // Fetch trip ID on initialization
        fetchAllocatedDuration();
    }

    //FOR UNIT TEST
    public boolean isValidUser(String userId) {
        return userId != null && !userId.trim().isEmpty(); // Example validation
    }

    //FOR UNIT TEST!
    public boolean addNoteForTest(String noteContent, String noteAuthor) {
        return noteContent != null && !noteContent.trim().isEmpty() && noteAuthor != null;
    }


    private void fetchTripId() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef.child(uid).child("trip").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getValue(String.class) != null) {
                            tripId = snapshot.getValue(String.class);
                            loadCurrentContributors();
                            loadNotes();
                        } else {
                            tripId = "trip" + uid; // Create a new trip ID if none exists
                            userRef.child(uid).child("trip").setValue(tripId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Log or handle database error if needed
                    }
                });
    }

    private void loadCurrentContributors() {
        if (tripId != null) {
            tripsRef.child(tripId).child("contributors").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            List<String> currentContributors = new ArrayList<>();
                            for (DataSnapshot contributorSnapshot : snapshot.getChildren()) {
                                currentContributors.add(contributorSnapshot.getKey());
                            }

                            // Add existing contributors to the list
                            Set<String> uniqueContributors = new HashSet<>(currentContributors);
                            uniqueContributors.add(FirebaseAuth.getInstance().
                                    getCurrentUser().getUid());
                            contributors.setValue(new ArrayList<>(uniqueContributors));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle error if needed
                        }
                    });
        }
    }

    private void fetchAllocatedDuration() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef.child(userId).child("allocatedDuration").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            long allocatedMillis = snapshot.getValue(Long.class);
                            long allocatedInDays = allocatedMillis / (1000 * 60 * 60 * 24);
                            allocatedDays.setValue(allocatedInDays);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Log or handle database error if needed
                    }
                });
    }

    public LiveData<Long> getAllocatedDays() {
        return allocatedDays;
    }

    public LiveData<List<String>> getContributors() {
        return contributors;
    }

    public LiveData<Boolean> getInvalidUserId() {
        return invalidUserId;
    }

    public void addContributor(String userId) {
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> currentContributors = contributors.getValue();
                    if (currentContributors != null && !currentContributors.contains(userId)) {
                        currentContributors.add(userId);
                        contributors.setValue(currentContributors);
                        invalidUserId.setValue(false);

                        // Add contributor to the trip in the trips database
                        addContributorToTrip(userId);
                        updateContributorTripId(userId); // Update contributor's trip ID
                    }
                } else {
                    invalidUserId.setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void addContributorToTrip(String userId) {
        if (tripId != null) {
            tripsRef.child(tripId).child("contributors").child(userId).setValue(true)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully added contributor to trip
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to add contributor
                    });
        }
    }

    private void updateContributorTripId(String userId) {
        if (userId != null && tripId != null) {
            userRef.child(userId).child("trip").setValue(tripId)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully updated contributor's trip ID
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to update trip ID
                    });
        }
    }

    public LiveData<List<String>> getNotes() {
        return notes;
    }

    public void addNote(String userId, String noteContent) {
        if (tripId != null) {
            DatabaseReference notesRef = tripsRef.child(tripId).child("notes").push();
            notesRef.child("noteAuthor").setValue(userId);
            notesRef.child("noteContent").setValue(noteContent)
                    .addOnSuccessListener(aVoid -> loadNotes()); // Refresh the notes after adding

        }
    }

    private void loadNotes() {
        if (tripId != null) {
            tripsRef.child(tripId).child("notes").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            List<String> notesList = new ArrayList<>();
                            for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                                String author =
                                        noteSnapshot.child("noteAuthor").getValue(String.class);
                                String content =
                                        noteSnapshot.child("noteContent").getValue(String.class);
                                if (author != null && content != null) {
                                    notesList.add(author + " - " + content);
                                }
                            }
                            notes.setValue(notesList); // Update LiveData with the notes
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle error if needed
                        }
                    });
        }
    }
}

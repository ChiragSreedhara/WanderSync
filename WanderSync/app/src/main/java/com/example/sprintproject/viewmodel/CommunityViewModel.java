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
import java.util.stream.Collectors;

public class CommunityViewModel extends ViewModel {
    private MutableLiveData<List<Map<String, Object>>> posts;
    private static final String START_DATE = "start_date";

    public LiveData<List<Map<String, Object>>> getPosts() {
        if (posts == null) {
            posts = new MutableLiveData<>();
            loadPosts();
        }
        return posts;
    }

    private void loadPosts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference communityRef = database.getReference("community");

        communityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map<String, Object>> postList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> post = (Map<String, Object>) postSnapshot.getValue();
                    postList.add(post);
                }
                posts.setValue(postList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    public void addPost(String destination, String startDate, String endDate, String accommodations,
                        String diningReservations, String notes) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference communityRef = database.getReference("community");

        String postId = communityRef.push().getKey();
        if (postId != null) {
            Map<String, Object> post = new HashMap<>();
            post.put("destination", destination);
            post.put(startDate, startDate);
            post.put("end_date", endDate);
            post.put("accommodations", accommodations);
            post.put("dining_reservations", diningReservations);
            post.put("notes", notes);
            post.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

            communityRef.child(postId).setValue(post);
        }
    }

    public List<Map<String, Object>> sortPostsByStartDate(List<Map<String, Object>> posts) {
        posts.sort((p1, p2) -> ((String) p1.get(START_DATE)).compareTo((String) p2.get(START_DATE)));
        return posts;
    }

    public List<Map<String, Object>> filterPostsByDestination(String destination, List<Map<String, Object>> posts) {
        return posts.stream()
                .filter(post -> destination.equals(post.get("destination")))
                .collect(Collectors.toList());
    }

}
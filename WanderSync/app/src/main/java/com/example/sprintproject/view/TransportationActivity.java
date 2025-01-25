package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class TransportationActivity extends AppCompatActivity {
    private static final String TAG = "TransportationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transportation_screen);

        Log.d(TAG, "onCreate called");

        // Destination Button
        ImageButton destinationButton = findViewById(R.id.destinationButton);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransportationActivity.this, DestinationsActivity.class);
                startActivity(intent);
            }
        });

        // Dining Button
        ImageButton diningButton = findViewById(R.id.diningButton);
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransportationActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        // Accommodations Button
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransportationActivity.this,
                        AccommodationsActivity.class);
                startActivity(intent);
            }
        });

        // Logistics Button
        ImageButton logisticsButton = findViewById(R.id.logisticButton);
        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransportationActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        // Community Button
        ImageButton communityButton = findViewById(R.id.communityButton);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransportationActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}

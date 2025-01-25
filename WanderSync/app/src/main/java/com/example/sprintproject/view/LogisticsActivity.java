package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.LogisticsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {
    private static final String TAG = "LogisticsActivity";
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogisticsViewModel logisticsViewModel;
        Button chartButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_screen);

        Log.d(TAG, "onCreate called");

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);
        pieChart = findViewById(R.id.pieChart);
        chartButton = findViewById(R.id.visualizeButton); // Initialize chartButton
        logisticsViewModel.start();
        // Set the pie chart to be initially hidden
        pieChart.setVisibility(View.GONE);

        logisticsViewModel.getAllocatedDays().observe(this, allocatedDays -> {
            if (allocatedDays != null && allocatedDays > 0) {
                List<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(allocatedDays, "Allocated Days"));

                PieDataSet dataSet = new PieDataSet(entries, "Trip Days");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                PieData data = new PieData(dataSet);
                dataSet.setDrawValues(false);

                pieChart.setData(data);
                pieChart.invalidate(); // Refresh chart
            }
        });

        // Toggle the visibility of the chart when the button is clicked
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pieChart.getVisibility() == View.VISIBLE) {
                    pieChart.setVisibility(View.GONE); // Hide chart
                } else {
                    pieChart.setVisibility(View.VISIBLE); // Show chart
                    pieChart.animateY(1000); // Animate when showing
                }
            }
        });

        // Destination Button
        ImageButton destinationButton = findViewById(R.id.destinationButton);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, DestinationsActivity.class);
                intent.putExtra("KEY", "fill");
                startActivity(intent);
            }
        });

        // Dining Button
        ImageButton diningButton = findViewById(R.id.diningButton);
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, DiningActivity.class);
                intent.putExtra("KEY", "fill");
                startActivity(intent);
            }
        });

        // Accommodations Button
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, AccommodationsActivity.class);
                intent.putExtra("KEY", "fill");
                startActivity(intent);
            }
        });

        // Transportation Button
        ImageButton transportationButton = findViewById(R.id.transportationButton);
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, TransportationActivity.class);
                intent.putExtra("KEY", "fill");
                startActivity(intent);
            }
        });

        // Community Button
        ImageButton communityButton = findViewById(R.id.communityButton);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, CommunityActivity.class);
                intent.putExtra("KEY", "fill");
                startActivity(intent);
            }
        });

        EditText userIdEditText = findViewById(R.id.userIdEditText);
        Button inviteButton = findViewById(R.id.inviteButton);
        TextView contributorsTextView = findViewById(R.id.contributorsTextView);

        logisticsViewModel.getContributors().observe(this, contributors -> {
            String contributorsText = "Contributors: " + String.join(", ", contributors);
            contributorsTextView.setText(contributorsText);
        });

        logisticsViewModel.getInvalidUserId().observe(this, isInvalid -> {
            if (isInvalid) {
                Toast.makeText(this, "Invalid UserID!", Toast.LENGTH_SHORT).show();
            }
        });

        inviteButton.setOnClickListener(v -> {
            String userId = userIdEditText.getText().toString().trim();
            if (!userId.isEmpty()) {
                logisticsViewModel.addContributor(userId);
            }
        });

        EditText noteEditText = findViewById(R.id.addNotesBox);
        Button addNoteButton = findViewById(R.id.notesButton);
        TextView notesTextView = findViewById(R.id.notesView);

        logisticsViewModel.getNotes().observe(this, notes -> {
            StringBuilder notesDisplay = new StringBuilder();
            for (String note : notes) {
                notesDisplay.append(note).append("\n"); // Append each note on a new line
            }
            notesTextView.setText(notesDisplay.toString());
        });

        addNoteButton.setOnClickListener(v -> {
            String noteContent = noteEditText.getText().toString().trim();
            if (!noteContent.isEmpty()) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                logisticsViewModel.addNote(userId, noteContent);
                noteEditText.setText(""); // Clear the input field
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

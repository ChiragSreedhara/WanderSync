package com.example.sprintproject.view;

import com.example.sprintproject.R;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprintproject.viewmodel.DiningViewModel;
import java.util.List;
import java.util.Map;

public class DiningActivity extends AppCompatActivity {

    private ListView reservationsListView; // Use ListView for displaying reservations
    private static final String LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DiningViewModel diningViewModel;
        Button addReservationButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dining_establishment_screen); // Adjust to your layout name

        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);
        reservationsListView = findViewById(R.id.reservationsListView);
        // Make sure this ID matches your layout
        addReservationButton = findViewById(R.id.addReservationButton); // Adjust if necessary

        diningViewModel.getReservations().observe(this,
                new Observer<List<Map<String, Object>>>() {
                @Override
            public void onChanged(List<Map<String, Object>> reservations) {
                    updateListView(reservations); // Update the ListView when data changes
                }
            });

        addReservationButton.setOnClickListener(v -> showAddReservationDialog(diningViewModel));

        // Set up item click listener for toggling expired status
        reservationsListView.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, Object> reservation =
                    (Map<String, Object>) parent.getItemAtPosition(position);
            String location = (String) reservation.get(LOCATION);
            Boolean isExpired = (Boolean) reservation.get("expired");

            // Toggle the expired status
            diningViewModel.updateReservation(location, !isExpired);
        });


        // Logistics Button
        ImageButton logisticsButton = findViewById(R.id.logisticButton);
        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiningActivity.this,
                        LogisticsActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Destination Button
        ImageButton destinationButton = findViewById(R.id.destinationButton);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiningActivity.this,
                        DestinationsActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Transportation Button
        ImageButton transportationButton = findViewById(R.id.transportationButton);
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiningActivity.this,
                        TransportationActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Accommodations Button
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiningActivity.this,
                        AccommodationsActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Community Button
        ImageButton communityButton = findViewById(R.id.communityButton);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiningActivity.this,
                        CommunityActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Order Button for sorting by date
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> {
            List<Map<String, Object>> sortedReservations =
                    diningViewModel.getReservations().getValue();

            if (sortedReservations != null) {
                // Sort by 'time' field (assuming HH:mm format for lexicographical sorting)
                sortedReservations.sort((a, b) -> ((String) a.get("time")).
                        compareTo((String) b.get("time")));
                updateListView(sortedReservations);
            }
        });

    }

    private void showAddReservationDialog(DiningViewModel diningViewModel) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_reservation);

        EditText locationEditText = dialog.findViewById(R.id.locationEditText);
        EditText websiteEditText = dialog.findViewById(R.id.websiteEditText);
        EditText timeEditText = dialog.findViewById(R.id.timeEditText);
        Button submitReservationButton = dialog.findViewById(R.id.submitReservationButton);

        submitReservationButton.setOnClickListener(v -> {
            String location = locationEditText.getText().toString();
            String website = websiteEditText.getText().toString();
            String time = timeEditText.getText().toString();

            // Validate input
            if (TextUtils.isEmpty(location) || TextUtils.isEmpty(website)
                    || TextUtils.isEmpty(time)) {
                Toast.makeText(this, "Please fill in all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidTimeFormat(time)) {
                Toast.makeText(this, "Time must be in HH:mm format",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for duplicates
            List<Map<String, Object>> currentReservations = diningViewModel.
                    getReservations().getValue();
            if (currentReservations != null) {
                for (Map<String, Object> reservation : currentReservations) {
                    if (reservation.get(LOCATION).equals(location) && reservation.get("time").
                            equals(time)) {
                        Toast.makeText(this, "This reservation already exists",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        return;
                    }
                }
            }

            diningViewModel.addReservation(location, website, time, false);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateListView(List<Map<String, Object>> reservations) {
        ArrayAdapter<Map<String, Object>> adapter = new ArrayAdapter<Map<String, Object>>(
                this,
                android.R.layout.simple_list_item_1, reservations) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                Map<String, Object> reservation = getItem(position);
                String displayText = reservation.get(LOCATION) + " - "
                        + reservation.get("website") + " - " + reservation.get("time") + " - "
                        + (Boolean.TRUE.equals(reservation.get("expired"))
                        ? "Expired" : "Not Expired");
                textView.setText(displayText);
                return view;
            }
        };
        reservationsListView.setAdapter(adapter);
    }

    private boolean isValidTimeFormat(String time) {
        String regex = "([01]\\d|2[0-3]):([0-5]\\d)"; // Regex for HH:mm format
        return time.matches(regex);
    }
}

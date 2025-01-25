package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.DestinationScreenBinding;
import com.example.sprintproject.ui.DestinationAdapter;
import com.example.sprintproject.utils.DateUtils;
import com.example.sprintproject.viewmodel.DestinationsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DestinationsActivity extends AppCompatActivity {
    private static final String TAG = "DestinationsActivity";

    // layout under logTravelButton
    private LinearLayout logTravelLayout;
    private boolean logTravelLayoutVisible = true;

    // layout under calculateTimeButton
    private LinearLayout calculateTimeLayout;
    private boolean calculateTimeLayoutVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText destinationEdit;
        EditText estimatedStartEdit;
        EditText estimatedEndEdit;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);
        DestinationScreenBinding binding = DestinationScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate called");

        DestinationsViewModel viewModel = new ViewModelProvider(this)
                .get(DestinationsViewModel.class);
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                viewModel.clearToastMessage();
            }
        });

        // list view to show list of accommodations
        ListView listView = findViewById(R.id.destinations_list_view);
        DestinationAdapter adapter = new DestinationAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);


        // observe destinations
        viewModel.getDestinations().observe(this, destinations -> {
            Log.d("DestinationAdapter", "Destinations: " + destinations);
            // Update the adapter's data whenever the list changes
            adapter.clear();
            if (destinations != null) {
                adapter.addAll(destinations);
            }
        });

        // Logistics Button
        ImageButton logisticsButton = findViewById(R.id.logisticButton);
        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationsActivity.this, LogisticsActivity.class);
                intent.putExtra("KEY", "fill");
                startActivity(intent);
            }
        });
        // Dining Button
        ImageButton diningButton = findViewById(R.id.diningButton);
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationsActivity.this, DiningActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });
        // Transportation Button
        ImageButton transportationButton = findViewById(R.id.transportationButton);
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationsActivity.this, TransportationActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });
        // Accommodations Button
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationsActivity.this, AccommodationsActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });
        // Community Button
        ImageButton communityButton = findViewById(R.id.communityButton);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationsActivity.this, CommunityActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Log Travel Button
        Button logTravelButton = findViewById(R.id.log_travel_button);
        logTravelLayout = findViewById(R.id.log_layout);
        logTravelButton.setOnClickListener(view -> toggleLogLayout());

        destinationEdit = findViewById(R.id.travel_location_edit);
        estimatedStartEdit = findViewById(R.id.estimated_start_edit);
        estimatedEndEdit = findViewById(R.id.estimated_end_edit);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button submitButton = findViewById(R.id.submit_button);

        cancelButton.setOnClickListener(view -> {
            destinationEdit.setText("");
            estimatedStartEdit.setText("");
            estimatedEndEdit.setText("");
            hideKeyboard();
            toggleLogLayout();
        });

        estimatedStartEdit.setOnClickListener(v ->
                showDatePickerDialog((view, yearSelected, monthSelected, daySelected) -> {
                    Date d = DateUtils.createDate(yearSelected, monthSelected, daySelected);
                    estimatedStartEdit.setText(DateUtils.formatShort(d));
                }));

        estimatedEndEdit.setOnClickListener(v ->
                showDatePickerDialog((view, yearSelected, monthSelected, daySelected) -> {
                    Date d = DateUtils.createDate(yearSelected, monthSelected, daySelected);
                    estimatedEndEdit.setText(DateUtils.formatShort(d));
                }));

        submitButton.setOnClickListener(v -> {
            String destination = destinationEdit.getText().toString();
            String start = estimatedStartEdit.getText().toString();
            String end = estimatedEndEdit.getText().toString();
            viewModel.addDestination(destination, start, end);
            destinationEdit.setText("");
            estimatedStartEdit.setText("");
            estimatedEndEdit.setText("");
            destinationEdit.clearFocus();
        });

        // Calculate Time Button
        Button calculateTimeButton = findViewById(R.id.calculate_time_button);
        calculateTimeButton.setOnClickListener(view -> toggleCalculateTimeLayout());

        // Calculate Time Layout
        calculateTimeLayout = findViewById(R.id.calculate_time_layout);
        Button startDateButton = findViewById(R.id.start_date_button);
        Button endDateButton = findViewById(R.id.end_date_button);
        EditText durationField = findViewById(R.id.duration_edit);
        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(view -> {
            String days = durationField.getText().toString();
            try {
                if (!days.isEmpty()) {
                    viewModel.setNewDuration(Integer.parseInt(days));
                }
                viewModel.calculateThirdField();
                viewModel.updateAllocatedVacationTime();
                viewModel.clearEnteredAllocatedVacationTime();
                durationField.setText("");
                Toast.makeText(DestinationsActivity.this, "Updated allocated"
                      + " vacation time", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException nfe) {
                Toast.makeText(DestinationsActivity.this, "Invalid date format",
                        Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException iae) {
                Toast.makeText(DestinationsActivity.this, iae.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        startDateButton.setOnClickListener(v ->
                showDatePickerDialog((view, yearSelected, monthSelected, daySelected) ->
                        viewModel.setNewStartDate(yearSelected, monthSelected, daySelected))
        );

        endDateButton.setOnClickListener(v ->
                showDatePickerDialog((view, yearSelected, monthSelected, daySelected) ->
                        viewModel.setNewEndDate(yearSelected, monthSelected, daySelected))
        );

        toggleLogLayout();
        toggleCalculateTimeLayout();
    }

    private void toggleLogLayout() {
        logTravelLayout.setVisibility(logTravelLayoutVisible ? View.GONE : View.VISIBLE);
        logTravelLayoutVisible = !logTravelLayoutVisible;
    }

    private void toggleCalculateTimeLayout() {
        calculateTimeLayout.setVisibility(calculateTimeLayoutVisible ? View.GONE : View.VISIBLE);
        calculateTimeLayoutVisible = !calculateTimeLayoutVisible;
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

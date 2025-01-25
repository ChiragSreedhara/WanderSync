package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.IAccommodationModel;
import com.example.sprintproject.ui.AccommodationAdapter;
import com.example.sprintproject.viewmodel.AccommodationsViewModel;
import com.example.sprintproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {
    private static final String TAG = "AccommodationsActivity";
    private AccommodationsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accommodation_screen);

        Log.d(TAG, "onCreate called");

        viewModel = new ViewModelProvider(this).get(AccommodationsViewModel.class);

        // list view to show list of accommodations
        ListView listView = findViewById(R.id.accommodations_list_view);
        AccommodationAdapter adapter = new AccommodationAdapter(this, new ArrayList<>(),
                viewModel);
        listView.setAdapter(adapter);

        // plus button to add accommodation
        Button plusButton = findViewById(R.id.plus_button);
        plusButton.setOnClickListener(v -> showAddAccommodationDialog(viewModel));

        // observe accommodations
        viewModel.getAccommodations().observe(this, new Observer<List<IAccommodationModel>>() {
            @Override
            public void onChanged(List<IAccommodationModel> accommodations) {
                // Update the adapter's data whenever the list changes
                adapter.clear();
                if (accommodations != null) {
                    adapter.addAll(accommodations);
                }
            }
        });

        // Logistics Button
        ImageButton logisticsButton = findViewById(R.id.logisticButton);
        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, LogisticsActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Destination Button
        ImageButton destinationButton = findViewById(R.id.destinationButton);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, DestinationsActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Dining Button
        ImageButton diningButton = findViewById(R.id.diningButton);
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, DiningActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Community Button
        ImageButton communityButton = findViewById(R.id.communityButton);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, CommunityActivity.class);

                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });

        // Transportation Button
        ImageButton transportationButton = findViewById(R.id.transportationButton);
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this,
                        TransportationActivity.class);
                intent.putExtra("KEY", "fill");

                startActivity(intent);
            }
        });
    }

    private void showAddAccommodationDialog(AccommodationsViewModel viewModel) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.accommodation_dialog);

        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                viewModel.clearToastMessage();
            }
        });

        EditText checkInEditText = dialog.findViewById(R.id.check_in_edit);
        EditText checkOutEditText = dialog.findViewById(R.id.check_out_edit);
        EditText locationEditText = dialog.findViewById(R.id.location_edit);
        Spinner numRoomsSpinner = dialog.findViewById(R.id.num_rooms_spinner);
        Spinner roomTypeSpinner = dialog.findViewById(R.id.room_type_spinner);
        Button submitButton = dialog.findViewById(R.id.submit_button);

        checkInEditText.setOnClickListener(v ->
                showDatePickerDialog((view, yearSelected, monthSelected, daySelected) -> {
                    Date d = DateUtils.createDate(yearSelected, monthSelected, daySelected);
                    checkInEditText.setText(DateUtils.formatShort(d));
                })
        );

        checkOutEditText.setOnClickListener(v ->
                showDatePickerDialog((view, yearSelected, monthSelected, daySelected) -> {
                    Date d = DateUtils.createDate(yearSelected, monthSelected, daySelected);
                    checkOutEditText.setText(DateUtils.formatShort(d));
                })
        );

        submitButton.setOnClickListener(v -> {
            String checkIn = checkInEditText.getText().toString();
            String checkOut = checkOutEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String numRooms = numRoomsSpinner.getSelectedItem().toString();
            String roomType = roomTypeSpinner.getSelectedItem().toString();
            viewModel.addAccommodation(checkIn, checkOut, location, numRooms, roomType);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void sortByCheckIn(View view) {
        viewModel.sortAccommodationsCheckIn();
    }

    public void sortByCheckOut(View view) {
        viewModel.sortAccommodationsCheckOut();
    }

    public void sortByRoomType(View view) {
        viewModel.sortAccommodationsRoomType();
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.show();
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
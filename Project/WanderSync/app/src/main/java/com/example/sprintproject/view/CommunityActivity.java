package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.CommunityViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommunityActivity extends AppCompatActivity {

    private ListView communityPostsListView;
    private static final String DESTINATION = "destination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_community_screen);

        CommunityViewModel communityViewModel = new ViewModelProvider(this).
                get(CommunityViewModel.class);
        communityPostsListView = findViewById(R.id.reservationsListView);
        Button addPostButton = findViewById(R.id.addReservationButton);

        communityViewModel.getPosts().observe(
                this, new Observer<List<Map<String, Object>>>() {
                    @Override
            public void onChanged(List<Map<String, Object>> posts) {
                            updateListView(posts);
                        }
                    });

        addPostButton.setOnClickListener(v -> showAddPostDialog(communityViewModel));
        setupNavigationButtons();
    }

    private void showAddPostDialog(CommunityViewModel communityViewModel) {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_community_post, null);
        dialog.setContentView(dialogView);

        EditText destinationInput = dialogView.findViewById(R.id.destinationInput);
        EditText accommodationInput = dialogView.findViewById(R.id.accommodationInput);
        EditText diningInput = dialogView.findViewById(R.id.diningInput);
        EditText notesInput = dialogView.findViewById(R.id.notesInput);
        Button startDateButton = dialogView.findViewById(R.id.startDateButton);
        Button endDateButton = dialogView.findViewById(R.id.endDateButton);
        Button submitPostButton = dialogView.findViewById(R.id.submitPostButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        final String[] startDate = {""};
        final String[] endDate = {""};

        startDateButton.setOnClickListener(v -> setupDatePicker((view, year, month, dayOfMonth) -> {
            int adjustedMonth = month + 1;
            String selectedDate = adjustedMonth + "/" + dayOfMonth + "/" + year;
            startDate[0] = selectedDate;
            startDateButton.setText(selectedDate);
        }));

        endDateButton.setOnClickListener(v -> setupDatePicker((view, year, month, dayOfMonth) -> {
            int adjustedMonth = month + 1;
            String selectedDate = adjustedMonth + "/" + dayOfMonth + "/" + year;
            endDate[0] = selectedDate;
            endDateButton.setText(selectedDate);
        }));

        submitPostButton.setOnClickListener(v -> {
            String destination = destinationInput.getText().toString().trim();
            String accommodations = accommodationInput.getText().toString().trim();
            String diningReservations = diningInput.getText().toString().trim();
            String notes = notesInput.getText().toString().trim();

            if (TextUtils.isEmpty(destination) || TextUtils.isEmpty(startDate[0])
                    || TextUtils.isEmpty(endDate[0])) {
                Toast.makeText(this, "Please fill in all required fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidDateRange(startDate[0], endDate[0])) {
                Toast.makeText(this, "Start date must be before end date",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            communityViewModel.addPost(destination, startDate[0], endDate[0], accommodations,
                    diningReservations, notes);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void setupDatePicker(DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean isValidDateRange(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return start != null && end != null && start.before(end);
        } catch (ParseException e) {
            return false;
        }
    }

    private void updateListView(List<Map<String, Object>> posts) {
        ArrayAdapter<Map<String, Object>> adapter = new ArrayAdapter<Map<String, Object>>(
                this, android.R.layout.simple_list_item_1, posts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                Map<String, Object> post = getItem(position);
                String displayText = post.get(DESTINATION) + " - " + post.get("start_date")
                        + " to " + post.get("end_date");
                textView.setText(displayText);
                view.setOnClickListener(v -> showPostDetailsDialog(post));
                return view;
            }
        };
        communityPostsListView.setAdapter(adapter);
    }


    private void showPostDetailsDialog(Map<String, Object> post) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.travel_post_item);

        TextView destinationText = dialog.findViewById(R.id.destinations);
        TextView startDateText = dialog.findViewById(R.id.startDateLabel);
        TextView endDateText = dialog.findViewById(R.id.endDateLabel);
        TextView accommodationsText = dialog.findViewById(R.id.accommodationLabel);
        TextView diningReservationsText = dialog.findViewById(R.id.diningLabel);
        TextView notesText = dialog.findViewById(R.id.notesLabel);
        TextView authorText = dialog.findViewById(R.id.authorLabel);

        Log.d("CommunityActivity", "Post data: " + post);


        destinationText.setText(post.get(DESTINATION).toString());
        startDateText.setText(post.get("start_date").toString());
        endDateText.setText(post.get("end_date").toString());
        accommodationsText.setText(post.get("accommodations").toString());
        diningReservationsText.setText(post.get("dining_reservations").toString());
        String notes = post.get("notes") != null ? post.get("notes").toString() : "None";
        notesText.setText(notes);
        authorText.setText(post.get("user_id").toString());

        dialog.show();
    }

    private void setupNavigationButtons() {
        ImageButton logisticsButton = findViewById(R.id.logisticButton);
        logisticsButton.setOnClickListener(v -> startActivity(new Intent(
                CommunityActivity.this, LogisticsActivity.class)));

        ImageButton destinationButton = findViewById(R.id.destinationButton);
        destinationButton.setOnClickListener(v -> startActivity(new Intent(
                CommunityActivity.this, DestinationsActivity.class)));

        ImageButton transportationButton = findViewById(R.id.transportationButton);
        transportationButton.setOnClickListener(v -> startActivity(new Intent(
                CommunityActivity.this, TransportationActivity.class)));

        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        accommodationsButton.setOnClickListener(v -> startActivity(new Intent(
                CommunityActivity.this, AccommodationsActivity.class)));

        ImageButton diningButton = findViewById(R.id.diningButton);
        diningButton.setOnClickListener(v -> startActivity(new Intent(
                CommunityActivity.this, DiningActivity.class)));
    }
}

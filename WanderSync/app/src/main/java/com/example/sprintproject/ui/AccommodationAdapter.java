package com.example.sprintproject.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.AccommodationItemBinding;
import com.example.sprintproject.model.IAccommodationModel;
import com.example.sprintproject.viewmodel.AccommodationsViewModel;

import java.util.List;

public class AccommodationAdapter extends ArrayAdapter<IAccommodationModel> {
    private AccommodationsViewModel viewModel;

    public AccommodationAdapter(@NonNull Context context, List<IAccommodationModel> items,
                                AccommodationsViewModel viewModel) {
        super(context, 0, items);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationItemBinding binding;

        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.accommodation_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (AccommodationItemBinding) convertView.getTag();
        }

        IAccommodationModel item = getItem(position);
        TextView label = convertView.findViewById(R.id.location_num_text);
        label.setText("Location " + (position + 1));
        Button expiredButton = convertView.findViewById(R.id.expired_button);
        expiredButton.setText(item.getExpired() ? "Expired" : "Not expired");
        expiredButton.setOnClickListener(v -> {
            boolean newStatus = viewModel.toggleExpired(position);
            expiredButton.setText(newStatus ? "Expired" : "Not expired");
        });

        binding.setA(item);
        binding.executePendingBindings();

        return convertView;
    }
}

package com.example.sprintproject.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.DestinationItemBinding;
import com.example.sprintproject.model.DestinationModel;

import java.util.List;

public class DestinationAdapter extends ArrayAdapter<DestinationModel> {
    public DestinationAdapter(@NonNull Context context, List<DestinationModel> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DestinationItemBinding binding;

        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.destination_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (DestinationItemBinding) convertView.getTag();
        }

        DestinationModel item = getItem(position);

        binding.setD(item);
        binding.executePendingBindings();

        return convertView;
    }
}

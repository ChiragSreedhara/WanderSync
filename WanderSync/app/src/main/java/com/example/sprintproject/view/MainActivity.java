package com.example.sprintproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.sprintproject.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(MainActivity.this, EmailPasswordActivity.class);

        intent.putExtra("KEY", "fill");

        startActivity(intent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
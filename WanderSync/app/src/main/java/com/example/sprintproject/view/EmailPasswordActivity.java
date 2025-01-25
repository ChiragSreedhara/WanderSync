package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button loginButton;
        Button registerButton;
        Button exitButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_password_screen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.enter_email);
        passwordField = findViewById(R.id.enter_password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        exitButton = findViewById(R.id.exit_button);

        loginButton.setOnClickListener(view -> loginUser());
        registerButton.setOnClickListener(view -> registerUser());
        exitButton.setOnClickListener(view -> finish());
    }

    // Login user using FirebaseAuth
    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(EmailPasswordActivity.this, "All fields are required",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(EmailPasswordActivity.this, "Login successful!",
                                Toast.LENGTH_SHORT).show();
                        // Redirect to your main activity (home screen)
                        Intent intent = new Intent(EmailPasswordActivity.this,
                                LogisticsActivity.class);
                        startActivity(intent);
                        finish(); // Close login activity
                    } else {
                        Toast.makeText(EmailPasswordActivity.this, "Authentication failed: "
                                + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Register new user
    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(EmailPasswordActivity.this, "All fields are required",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(EmailPasswordActivity.this, "Registration successful!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EmailPasswordActivity.this, "Registration failed: "
                                        + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
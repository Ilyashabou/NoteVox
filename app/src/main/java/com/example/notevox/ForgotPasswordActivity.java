package com.example.notevox;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private TextView statusTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        statusTextView = findViewById(R.id.statusTextView);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Set button click listener
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                return;
            }

            // Send password reset email
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Reset link sent. Check your email.", Toast.LENGTH_SHORT).show();
                            statusTextView.setText("Reset link sent to: " + email);
                            statusTextView.setTextColor(getResources().getColor(R.color.successTextColor));
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to send reset link.";
                            statusTextView.setText(errorMessage);
                            statusTextView.setTextColor(getResources().getColor(R.color.errorTextColor));
                        }
                        statusTextView.setVisibility(View.VISIBLE);
                    });
        });
    }
}

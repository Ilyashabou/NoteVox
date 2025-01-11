package com.example.notevox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "notevox_prefs"; // Use the same preferences file
    private static final String KEY_FIRST_TIME = "first_time";
    private static final String DARK_MODE_KEY = "dark_mode_enabled"; // Key for dark mode setting
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply the theme based on the saved preference before setContentView()
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean nightMode = preferences.getBoolean(DARK_MODE_KEY, false);  // Read the dark mode preference
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main); // Now, set the content view after applying the theme

        auth = FirebaseAuth.getInstance();

        boolean isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true);

        if (isFirstTime) {
            // If it's the first time, navigate to the LoginActivity
            new Handler().postDelayed(() -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_FIRST_TIME, false); // Update the flag for first-time check
                editor.apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close MainActivity
            }, 3000);  // Optional splash screen delay
        } else {
            // If it's not the first time, check if the user is logged in
            if (auth.getCurrentUser() != null) {
                // If user is logged in, navigate to HomeActivity
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();  // Close MainActivity
                }, 3000);  // Optional delay
            } else {
                // If not logged in, navigate to LoginActivity
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();  // Close MainActivity
                }, 3000);  // Optional delay
            }
        }
    }
}

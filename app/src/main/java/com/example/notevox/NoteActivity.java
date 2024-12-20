package com.example.notevox;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Initialize buttons
        ImageView mainButton = findViewById(R.id.mainButton);
        ImageView button1 = findViewById(R.id.button1);
        ImageView button2 = findViewById(R.id.button2);

        // Set click listener on the main button
        mainButton.setOnClickListener(view -> {
            // Toggle visibility of button1 and button2
            if (button1.getVisibility() == View.GONE && button2.getVisibility() == View.GONE) {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
            } else {
                button1.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
            }
        });
    }
}

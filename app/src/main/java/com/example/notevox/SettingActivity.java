package com.example.notevox;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private static final String PREFERENCES_FILE = "notevox_preferences";
    private static final String DARK_MODE_KEY = "dark_mode_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);



    }
    public void GoHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void GoSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
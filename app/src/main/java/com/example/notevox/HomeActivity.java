package com.example.notevox;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the parent class's onCreate
        setContentView(R.layout.activity_home);

        // Get reference to RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // Dummy data for testing
        List<String> notes = Arrays.asList("Note 1", "Note 2", "Note 3", "Note 4", "Note 5", "Note 6", "Note 7", "Note 8", "Note 9", "Note 10", "Note 11", "Note 12", "Note 13", "Note 15", "Note 16", "Note 18");

        // Set up RecyclerView with LayoutManager and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter(notes);
        recyclerView.setAdapter(adapter);

    }

    public void AddNotes(View view) {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }


}
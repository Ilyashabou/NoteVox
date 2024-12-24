package com.example.notevox;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the parent class's onCreate
        setContentView(R.layout.activity_home);

        // Get reference to RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize note list
        noteList = new ArrayList<>();

        // Fetch notes from Firebase and update RecyclerView
        fetchNotesFromFirebase();

        // Initialize the adapter (start with an empty list)
        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setAdapter(noteAdapter);
    }

    // Method to fetch notes from Firebase
    private void fetchNotesFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notesRef = database.getReference("notes"); // Reference to your notes data

        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noteList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract note data (assuming "id" and "name" fields in Firebase)
                    Note note = snapshot.getValue(Note.class);
                    if (note != null) {
                        note.setId(snapshot.getKey());
                        noteList.add(note); // Add note to the list
                    }
                }
                // Notify the adapter that the data has changed
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any error that occurred while reading data
                Toast.makeText(HomeActivity.this, "Error fetching notes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void AddNotes(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
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

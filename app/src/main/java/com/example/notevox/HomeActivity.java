package com.example.notevox;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.SearchView;
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
    private List<Note> filteredNoteList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the parent class's onCreate
        setContentView(R.layout.activity_home);


        // Get reference to RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize note list
        noteList = new ArrayList<>();
        filteredNoteList = new ArrayList<>();

        // Initialize the adapter (start with an empty list)
        noteAdapter = new NoteAdapter(this, filteredNoteList, this::deleteNoteFromFirebase);
        recyclerView.setAdapter(noteAdapter);

        // Fetch notes from Firebase and update RecyclerView
        fetchNotesFromFirebase();

        // Get SearchView from the layout (no need for onCreateOptionsMenu)
        searchView = findViewById(R.id.search_view);

        // Set up listener for search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Optional: Handle query submission
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });
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
                filteredNoteList.clear();
                filteredNoteList.addAll(noteList); // Initially show all notes
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any error that occurred while reading data
                Toast.makeText(HomeActivity.this, "Error fetching notes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter notes based on search query
    private void filterNotes(String query) {
        filteredNoteList.clear();
        if (query.isEmpty()) {
            filteredNoteList.addAll(noteList); // Show all notes if query is empty
        } else {
            for (Note note : noteList) {
                if (note.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredNoteList.add(note);
                }
            }
        }
        noteAdapter.notifyDataSetChanged(); // Notify the adapter to update the list
    }

    // Method to delete a note from Firebase
    private void deleteNoteFromFirebase(String noteId, int position) {
        if (noteId == null || position < 0 || position >= noteList.size()) {
            Toast.makeText(this, "Invalid note or position", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notesRef = database.getReference("notes");

        // Remove note from Firebase
        notesRef.child(noteId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Successfully deleted from Firebase
                    noteList.remove(position); // Remove from local list
                    filteredNoteList.remove(position); // Remove from filtered list too
                    noteAdapter.notifyItemRemoved(position); // Update RecyclerView
                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Toast.makeText(this, "Failed to delete note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Intent to add a new note
    public void AddNotes(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    // Navigate to home
    public void GoHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Navigate to settings
    public void GoSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}

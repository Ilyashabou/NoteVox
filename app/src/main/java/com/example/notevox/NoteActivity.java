package com.example.notevox;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {

    private EditText noteTitle, noteContent;
    private String existingNoteId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Initialize EditText fields
        noteTitle = findViewById(R.id.noteBanner);
        noteContent = findViewById(R.id.noteContent);

        // Initialize buttons
        ImageView mainButton = findViewById(R.id.mainButton);
        ImageView button1 = findViewById(R.id.button1);
        ImageView button2 = findViewById(R.id.button2);

        // Initially hide the buttons
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);

        // Set the button click listener to toggle visibility
        mainButton.setOnClickListener(view -> {
            if (button1.getVisibility() == View.GONE && button2.getVisibility() == View.GONE) {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
            } else {
                button1.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
            }
        });

        // Check if the activity was opened with an existing note ID
        existingNoteId = getIntent().getStringExtra("NOTE_ID");
        if (existingNoteId != null) {
            // If it's an existing note, populate the fields with the note data
            loadExistingNoteData(existingNoteId);
        }
    }

    private void loadExistingNoteData(String noteId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notes").child(noteId);

        myRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String title = snapshot.child("name").getValue(String.class);
                String content = snapshot.child("content").getValue(String.class);

                if (title != null && content != null) {
                    noteTitle.setText(title);
                    noteContent.setText(content);
                }
            }
        });
    }

    // Save the note to Firebase
    public void GoSave(View view) {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        // Ensure title and content are not empty
        if (title.isEmpty() || content.isEmpty()) {
            return; // You can add a Toast message here if needed
        }

        // Get Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notes");

        if (existingNoteId != null) {
            // Update the existing note with the same ID
            myRef.child(existingNoteId).child("name").setValue(title);
            myRef.child(existingNoteId).child("content").setValue(content);
        } else {
            // Create a new note with a unique ID
            Note newNote = new Note();
            newNote.setName(title);
            newNote.setContent(content);

            // Push the new note to Firebase
            String key = myRef.push().getKey(); // Generate a unique key for the new note
            if (key != null) {
                newNote.setId(key);
                myRef.child(key).setValue(newNote); // Save the new note under the generated key
            }
        }

        // After saving, navigate back to HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to go back to HomeActivity
    }
}

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
        setContentView(R.layout.activity_note); // Set the content for this activity

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
            // You will need to implement logic to load the note data from Firebase here
            loadExistingNoteData(existingNoteId);
        }
    }

    private void loadExistingNoteData(String noteId) {
        // Fetch the note data from Firebase and update the EditText fields
        // This is just a placeholder for loading existing data.
        // You should use Firebase methods to retrieve and set data for the title and content.

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notes").child(noteId);

        // Assuming you're loading data into the fields (you can customize this based on your Firebase setup)
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

    // Navigate to HomeActivity
    public void GoHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Navigate to SettingActivity
    public void GoSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    // Save the note to Firebase
    public void GoSave(View view) {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        // Ensure title and content are not empty
        if (title.isEmpty() || content.isEmpty()) {
            // Show a message if fields are empty
            return;
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
                myRef.child(key).setValue(newNote); // Save the new note under the generated key
            }
        }

        // After saving, navigate back to HomeActivity (optional)
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to go back to HomeActivity
    }
}

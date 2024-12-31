package com.example.notevox;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private static final int VOICE_INPUT_REQUEST_CODE = 100;
    private EditText noteTitle, noteContent;
    private ImageView saveButton, mainButton;
    private String existingNoteId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        // Initialize EditText fields
        noteTitle = findViewById(R.id.noteBanner);
        noteContent = findViewById(R.id.noteContent);

        // Initialize buttons
        mainButton = findViewById(R.id.mainButton);
        saveButton = findViewById(R.id.button2);

        // Initially hide the buttons
        saveButton.setVisibility(View.GONE);

        // Add TextWatcher to EditText fields to toggle saveButton visibility
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show saveButton if any text is entered
                toggleSaveButtonVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        };

        noteTitle.addTextChangedListener(textWatcher);
        noteContent.addTextChangedListener(textWatcher);

        // Main button starts/restarts voice input
        mainButton.setOnClickListener(view -> {
            saveButton.setVisibility(View.VISIBLE);
            startVoiceInput();
        });


        // Save button saves the note
        saveButton.setOnClickListener(this::GoSave);

        // Load existing note if applicable
        existingNoteId = getIntent().getStringExtra("NOTE_ID");
        if (existingNoteId != null) {
            loadExistingNoteData(existingNoteId);
        }
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your note...");

        try {
            startActivityForResult(intent, VOICE_INPUT_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Your device does not support voice input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_INPUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String recognizedText = results.get(0);
                noteContent.append(recognizedText + " "); // Append recognized text to the note content
                toggleSaveButtonVisibility();
            }
        }
    }

    private void loadExistingNoteData(String noteId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's UID
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userNotesRef = database.getReference("notes").child(userId).child(noteId);

        userNotesRef.get().addOnSuccessListener(snapshot -> {
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


    private void toggleSaveButtonVisibility() {
        // Show saveButton only if there is text in either EditText field
        if (!noteTitle.getText().toString().trim().isEmpty() || !noteContent.getText().toString().trim().isEmpty()) {
            saveButton.setVisibility(View.VISIBLE);
        } else {
            saveButton.setVisibility(View.GONE);
        }
    }

    // Save the note to Firebase
    public void GoSave(View view) {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's UID
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userNotesRef = database.getReference("notes").child(userId);

        if (existingNoteId != null) {
            userNotesRef.child(existingNoteId).child("name").setValue(title);
            userNotesRef.child(existingNoteId).child("content").setValue(content);
        } else {
            Note newNote = new Note();
            newNote.setName(title);
            newNote.setContent(content);

            String key = userNotesRef.push().getKey();
            if (key != null) {
                newNote.setId(key);
                userNotesRef.child(key).setValue(newNote);
            }
        }

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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

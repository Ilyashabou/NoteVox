package com.example.notevox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notevox.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final List<Note> notes; // Use Note objects, not just Strings
    private final Context context;

    // Constructor to pass data to the adapter
    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    // ViewHolder class to hold references to each item
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView noteText;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.note_text);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteText.setText(note.getName()); // Set note name or title

        // Set OnClickListener to navigate to NoteActivity with the note ID
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra("NOTE_ID", note.getId()); // Pass the unique ID of the note
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }
}

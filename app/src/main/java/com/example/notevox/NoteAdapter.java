package com.example.notevox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final List<Note> notes; // Use Note objects, not just Strings
    private final Context context;
    private final NoteDeleteListener deleteListener;

    // Constructor to pass data to the adapter
    public NoteAdapter(Context context, List<Note> notes, NoteDeleteListener deleteListener) {
        this.context = context;
        this.notes = notes;
        this.deleteListener = deleteListener;
    }

    // ViewHolder class to hold references to each item
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView noteText;
        public ImageButton deleteButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.note_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
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

        // Set OnClickListener for delete button
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(note.getId(), position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Interface for delete listener
    public interface NoteDeleteListener {
        void onDelete(String noteId, int position);
    }
}

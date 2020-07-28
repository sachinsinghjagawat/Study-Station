package com.example.studystation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.Note;
import com.example.studystation.databaseClasses.SubjectName;
import com.example.studystation.notes.NoteList;
import com.example.studystation.studentNotes.PdfOpen;
import com.example.studystation.studentNotes.StudentNotesList;
import com.example.studystation.studentNotes.StudentNotesSubject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    List<Note> noteList;
    NoteList context;

    public NotesAdapter(List<Note> noteList, NoteList context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_card_view , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        final Note note = noteList.get(position);
        holder.heading.setText(note.getTitle());
        holder.content.setText(note.getContent());
        if (note.getUrl().isEmpty()) {
            holder.openFile.setVisibility(View.GONE);
        }

        holder.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PdfOpen fragment = PdfOpen.newInstance( note.getUrl() , note.getTitle());

                FragmentManager fragmentManager = context.getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_right , R.anim.enter_from_right , R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading , content;
        Button openFile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = (TextView) itemView.findViewById(R.id.notesHeading);
            content = (TextView) itemView.findViewById(R.id.notesContent);
            openFile = (Button) itemView.findViewById(R.id.notesPdfOpen);

        }
    }

}

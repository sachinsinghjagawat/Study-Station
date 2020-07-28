package com.example.studystation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.SubjectName;
import com.example.studystation.professorNotes.ProfessorNotesList;
import com.example.studystation.professorNotes.ProfessorNotesSubject;
import com.example.studystation.studentNotes.StudentNotesList;
import com.example.studystation.studentNotes.StudentNotesSubject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfessorNotesSubjectAdapter extends RecyclerView.Adapter<ProfessorNotesSubjectAdapter.ViewHolder> {

    List<SubjectName> subjectNameList;
    ProfessorNotesSubject context;

    public ProfessorNotesSubjectAdapter(List<SubjectName> subjectNameList, ProfessorNotesSubject context) {
        this.subjectNameList = subjectNameList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfessorNotesSubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_notes_subject_card_view , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorNotesSubjectAdapter.ViewHolder holder, int position) {
        final SubjectName subjectName = subjectNameList.get(position);
        holder.subjectTextView.setText(subjectName.getNameOfSubject());
        Picasso.get().load(subjectName.getUrlOfImage()).fit().into(holder.subjectImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfessorNotesList fragment = ProfessorNotesList.newInstance(subjectName.getNameOfSubject());

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
        return subjectNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView subjectImage;
        TextView subjectTextView;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectImage = (ImageView) itemView.findViewById(R.id.subjectImage);
            subjectTextView = (TextView) itemView.findViewById(R.id.subjectTextView);
            linearLayout = itemView.findViewById(R.id.subjectLinearLayout);

        }
    }

}

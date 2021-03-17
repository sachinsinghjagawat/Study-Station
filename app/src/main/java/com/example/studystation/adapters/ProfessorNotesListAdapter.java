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
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.professorNotes.ProfessorNotesList;
import com.example.studystation.studentNotes.PdfDetails;
import com.example.studystation.studentNotes.StudentNotesList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfessorNotesListAdapter extends RecyclerView.Adapter<ProfessorNotesListAdapter.ViewHolder> {

    List<DocCardViewDetails> dcList;
    ProfessorNotesList context;

    public ProfessorNotesListAdapter(List<DocCardViewDetails> dcList, ProfessorNotesList context) {
        this.dcList = dcList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfessorNotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_notes_list_card_view , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorNotesListAdapter.ViewHolder holder, int position) {
        final DocCardViewDetails docCardViewDetails = dcList.get(position);
        holder.nameOfPdfTextView.setText(docCardViewDetails.getNameOfPdf());
        holder.userName.setText("-by " + docCardViewDetails.getNameOfUser());
        holder.rating.setText(docCardViewDetails.getRating() + "");
        Picasso.get().load(docCardViewDetails.getUrlOfImage()).centerCrop().fit().error(R.drawable.app_icon).into(holder.userImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDetails fragment = PdfDetails.newInstance(docCardViewDetails.getUrlOfPdf() , docCardViewDetails.getUrlOfImage());

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
        return dcList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView nameOfPdfTextView , userName , rating;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.userPhoto2);
            nameOfPdfTextView = (TextView) itemView.findViewById(R.id.nameOfPdfTextView);
            userName = itemView.findViewById(R.id.userName2);
            rating = itemView.findViewById(R.id.ratingText2);
            TextView ratingText = itemView.findViewById(R.id.ratingTextView2);
            ImageView ratingImage = itemView.findViewById(R.id.ratingImage2);
            linearLayout = itemView.findViewById(R.id.notesListLinearLayout);

            rating.setVisibility(View.GONE);
            ratingImage.setVisibility(View.GONE);
            ratingText.setVisibility(View.GONE);

        }
    }
}

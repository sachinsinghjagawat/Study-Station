package com.example.studystation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.Question;
import com.example.studystation.studentNotes.PdfDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    List<Question> questionList;
    PdfDetails context;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    public QuestionAdapter(List<Question> questionList, PdfDetails context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card_view , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionAdapter.ViewHolder holder, int position) {

        final Question question = questionList.get(position);
        holder.discussionQuestion.setText(question.getQuestion());
        String answer = "";
        try {
            int n = 1;
            for (String ans : question.getAnsList()) {
                if (n == 1){
                    answer = ans + answer;
                    n++;
                }else {
                    answer = ans + "\n\n" + answer;
                }
            }
            holder.discussionAnswer.setText(answer);
        }catch (Exception e){
            holder.discussionAnswer.setText("Answer");
            e.printStackTrace();
        }
        holder.discussionSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                Question newQuestion = new Question();
                newQuestion.setQuestion(question.getQuestion());
                List<String> ansList = new ArrayList<>();
                try {
                    ansList.addAll(question.getAnsList());
                }catch (Exception e){
                    e.printStackTrace();
                }
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String ans = holder.discussionAddAnswer.getText().toString() + "\n  -by " + user.getDisplayName();
                ansList.add(ans);
                newQuestion.setAnsList(ansList);
                db.collection("PdfFiles").document(PdfDetails.id).update("discussion" , FieldValue.arrayRemove(question));
                db.collection("PdfFiles").document(PdfDetails.id).update("discussion" , FieldValue.arrayUnion(newQuestion));
                holder.discussionAddAnswer.setText("");
            }
        });


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView discussionQuestion , discussionAnswer ;
        EditText discussionAddAnswer;
        Button discussionSendAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            discussionQuestion = itemView.findViewById(R.id.discussionQuestion);
            discussionAnswer = itemView.findViewById(R.id.discussionAnswer);
            discussionAddAnswer = itemView.findViewById(R.id.discussionAddAnswer);
            discussionSendAnswer = itemView.findViewById(R.id.discussionSendAnswer);

        }
    }

}

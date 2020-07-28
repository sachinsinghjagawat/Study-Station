package com.example.studystation.dialog;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.Favourite;
import com.example.studystation.databaseClasses.Note;
import com.example.studystation.databaseClasses.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddNotesDialog extends DialogFragment {

    public static final String TAG = "AddNotesDialog";
    String email;
    EditText heading , content ;
    Button addNote;

    public interface OnInputSelected{
        void sendInput(String heading , String content);
    }
    public OnInputSelected mOnInputSelected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_notes_fragment, container, false);
        heading = rootView.findViewById(R.id.addNotesHeading);
        content = rootView.findViewById(R.id.addNotesContent);
        addNote = rootView.findViewById(R.id.adddNotesButton);

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (heading.getText().toString().isEmpty() || content.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter Both The Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                mOnInputSelected.sendInput(heading.getText().toString() , content.getText().toString());
                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }


}
package com.example.studystation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studystation.R;

public class AddNoteInPdf extends DialogFragment {

    public static final String TAG = "AddNotesInPdfDialog";
    String email;
    EditText content ;
    Button addNote;

    public interface OnInputSelected{
        void sendInputInPdf(String content);
    }
    public OnInputSelected mOnInputSelected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_notes_in_pdf_dialog, container, false);
        content = rootView.findViewById(R.id.addNotesContent2);
        addNote = rootView.findViewById(R.id.adddNotesButton2);

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( content.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter The Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                mOnInputSelected.sendInputInPdf(content.getText().toString());
                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (AddNoteInPdf.OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }


}

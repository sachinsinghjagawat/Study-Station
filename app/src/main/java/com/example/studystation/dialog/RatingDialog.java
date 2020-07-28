package com.example.studystation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studystation.R;

import static android.content.ContentValues.TAG;

public class RatingDialog extends DialogFragment {

    public interface OnInputSelected{
        void sendInput(double input);
    }
    private static final String TAG = "RatingDialog";
    public OnInputSelected mOnInputSelected;
    Button cancel , submit ;
    RatingBar ratingBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rating_dialog, container, false);

        ratingBar = rootView.findViewById(R.id.ratingBar);
        cancel = rootView.findViewById(R.id.cancelButton2);
        submit = rootView.findViewById(R.id.submitButton2);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnInputSelected.sendInput(ratingBar.getRating());
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

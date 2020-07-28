package com.example.studystation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studystation.R;

public class MusicSelectionDialog extends DialogFragment {

    private static final String TAG = "MusicDialog";
    RadioGroup radioGroup;
    Button playButton , cancelButton;

    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;
    RadioButton radio1 , radio2 , radio3 , radio4 , radio5, radioStop;
    String fluteKrishna , memoryConcentrationAlphaWaves , morningMusicWithBirdsPiano , morningPositiveEnergy , relaxingStudyingPianoRain , musicStop , musicUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_selection_dialog, container, false);

        radioGroup = rootView.findViewById(R.id.radioGroup);
        radio1 = rootView.findViewById(R.id.radioButton1);
        radio2 = rootView.findViewById(R.id.radioButton2);
        radio3 = rootView.findViewById(R.id.radioButton3);
        radio4 = rootView.findViewById(R.id.radioButton4);
        radio5 = rootView.findViewById(R.id.radioButton5);
        radioStop = rootView.findViewById(R.id.radioButton6);
        playButton = rootView.findViewById(R.id.playButton);
        cancelButton = rootView.findViewById(R.id.cancelButton);

        fluteKrishna = "https://firebasestorage.googleapis.com/v0/b/study-station-2fd38.appspot.com/o/Audio%2Fflute_krishna.m4a?alt=media&token=402dd9df-a443-485c-9a60-cd937bf828a1";
        memoryConcentrationAlphaWaves = "https://firebasestorage.googleapis.com/v0/b/study-station-2fd38.appspot.com/o/Audio%2Fmemory_concentration_alpha%20waves.m4a?alt=media&token=873495b0-c493-402b-8b76-1c4a79c83334";
        morningMusicWithBirdsPiano = "https://firebasestorage.googleapis.com/v0/b/study-station-2fd38.appspot.com/o/Audio%2Fmorning_music_with_birds_piano.m4a?alt=media&token=c6157dff-692b-48e3-b156-4fe9eaf88e9d";
        morningPositiveEnergy = "https://firebasestorage.googleapis.com/v0/b/study-station-2fd38.appspot.com/o/Audio%2Fmorning_positive_energy.m4a?alt=media&token=47abdc58-c7f3-4b10-81ee-d27e52325c4f";
        relaxingStudyingPianoRain = "https://firebasestorage.googleapis.com/v0/b/study-station-2fd38.appspot.com/o/Audio%2Frelaxing_studing_piano_rain.m4a?alt=media&token=a5485977-a51e-4f46-a400-a9ff2b436c4f";
        musicStop = "STOP";
        musicUrl = morningMusicWithBirdsPiano;

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUrl = morningMusicWithBirdsPiano;
            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUrl = memoryConcentrationAlphaWaves;
            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUrl = fluteKrishna;
            }
        });

        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUrl = morningPositiveEnergy;
            }
        });

        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUrl = relaxingStudyingPianoRain;
            }
        });

        radioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUrl = musicStop;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnInputSelected.sendInput(musicUrl);
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

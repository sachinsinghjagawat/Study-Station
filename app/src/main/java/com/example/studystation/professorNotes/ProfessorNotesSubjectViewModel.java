package com.example.studystation.professorNotes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfessorNotesSubjectViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> subjectSelected = new MutableLiveData<>();

    public void setSubjectSelected(String input) {
        subjectSelected.setValue(input);
    }

    public LiveData<String> getSubjectSelected (){
        return subjectSelected;
    }
}
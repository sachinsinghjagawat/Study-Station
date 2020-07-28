package com.example.studystation.professorNotes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.adapters.ProfessorNotesSubjectAdapter;
import com.example.studystation.adapters.StudentNotesSubjectAdapter;
import com.example.studystation.databaseClasses.SubjectList;
import com.example.studystation.databaseClasses.SubjectName;
import com.example.studystation.studentNotes.StudentNotesSubject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ProfessorNotesSubject extends Fragment {

    private ProfessorNotesSubjectViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<SubjectName> subjectNameList;
    FirebaseFirestore db ;
    ListenerRegistration subjectListener;

    public static ProfessorNotesSubject newInstance() {
        return new ProfessorNotesSubject();
    }

    @Override
    public void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile" , Context.MODE_PRIVATE);
        String department =sharedPreferences.getString("department",  "");
        subjectListener = db.collection("SubjectLists").whereEqualTo("department" , department).addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges() ){
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    SubjectList subjectList = documentSnapshot.toObject(SubjectList.class);
                    subjectNameList.clear();
                    for (SubjectName subjectName : subjectList.getSubjectNameList()){
                        subjectNameList.add( 0 , subjectName);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.professor_notes_subject_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.professorNotesSubjectRecyclerView);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectNameList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2 , GridLayoutManager.VERTICAL , false));
        adapter = new ProfessorNotesSubjectAdapter( subjectNameList , ProfessorNotesSubject.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProfessorNotesSubjectViewModel.class);
        mViewModel.getSubjectSelected().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
        // TODO: Use the ViewModel
    }

    @Override
    public void onStop() {
        super.onStop();
        subjectListener.remove();
    }

}
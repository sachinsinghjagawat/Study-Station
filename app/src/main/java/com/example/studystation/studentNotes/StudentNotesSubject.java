package com.example.studystation.studentNotes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.adapters.StudentNotesSubjectAdapter;
import com.example.studystation.databaseClasses.SubjectList;
import com.example.studystation.databaseClasses.SubjectName;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

public class StudentNotesSubject extends Fragment {

    private StudentNotesSubjectViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<SubjectName> subjectNameList;
    FirebaseFirestore db ;
    ListenerRegistration subjectListener;

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
        View rootView = inflater.inflate(R.layout.student_notes_subject_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.studentNotesSubjectRecyclerView);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectNameList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2 , GridLayoutManager.VERTICAL , false));
        adapter = new StudentNotesSubjectAdapter( subjectNameList ,StudentNotesSubject.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StudentNotesSubjectViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getSubjectSelected().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        subjectListener.remove();
    }
}
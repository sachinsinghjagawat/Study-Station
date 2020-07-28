package com.example.studystation.studentNotes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.studystation.MainActivity;
import com.example.studystation.R;
import com.example.studystation.adapters.StudentNotesListAdapter;
import com.example.studystation.adapters.StudentNotesSubjectAdapter;
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.databaseClasses.DocList;
import com.example.studystation.databaseClasses.SubjectName;
import com.example.studystation.myProfile.MyProfileSetting;
import com.example.studystation.professorNotes.ProfessorNotesList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class StudentNotesList extends Fragment {

    private StudentNotesListViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<DocCardViewDetails> docCardViewDetailsList;
    FirebaseFirestore db;
    ListenerRegistration pdfListListener;
    String subjectSelected;
    FloatingActionButton addNotes;

    public static StudentNotesList newInstance(String subject) {
        StudentNotesList fragment = new StudentNotesList();
        Bundle args = new Bundle();
        args.putString("subjectSelected", subject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            subjectSelected = getArguments().getString("subjectSelected");
        }
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile", Context.MODE_PRIVATE);
        String department = sharedPreferences.getString("department", "");
        String section = sharedPreferences.getString("section", "");

        db = FirebaseFirestore.getInstance();
        pdfListListener = db.collection("PdfList").whereEqualTo("department", department)
                .whereEqualTo("section", section)
                .whereEqualTo("subject", subjectSelected)
                .whereEqualTo("type", "Student Notes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            DocList docList = documentSnapshot.toObject(DocList.class);
                            docCardViewDetailsList.clear();
                            for (DocCardViewDetails docCardViewDetails : docList.getDocCardViewDetailsList()) {
                                docCardViewDetailsList.add(0, docCardViewDetails);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.student_notes_list_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.studentNotesListRecyclerView);
        addNotes = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        docCardViewDetailsList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentNotesListAdapter(docCardViewDetailsList, StudentNotesList.this);
        recyclerView.setAdapter(adapter);

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudentNotes fragment = AddStudentNotes.newInstance(subjectSelected);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_right , R.anim.enter_from_right , R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StudentNotesListViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStop() {
        super.onStop();
        pdfListListener.remove();
    }

}
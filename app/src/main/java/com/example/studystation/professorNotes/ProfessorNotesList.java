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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.adapters.ProfessorNotesListAdapter;
import com.example.studystation.adapters.ProfessorNotesSubjectAdapter;
import com.example.studystation.adapters.StudentNotesListAdapter;
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.databaseClasses.DocList;
import com.example.studystation.studentNotes.AddStudentNotes;
import com.example.studystation.studentNotes.StudentNotesList;
import com.example.studystation.studentNotes.StudentNotesSubjectViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ProfessorNotesList extends Fragment {

    private StudentNotesSubjectViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<DocCardViewDetails> docCardViewDetailsList;
    FirebaseFirestore db;
    ListenerRegistration pdfListListener;
    String subjectSelected;

    public static ProfessorNotesList newInstance(String subject) {
        ProfessorNotesList fragment = new ProfessorNotesList();
        Bundle args = new Bundle();
        args.putString("subjectSelected" , subject );
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
                .whereEqualTo("type", "Professor Notes")
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
        View rootView = inflater.inflate(R.layout.professor_notes_list_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.professorNotesListRecyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        docCardViewDetailsList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfessorNotesListAdapter(docCardViewDetailsList, ProfessorNotesList.this);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(StudentNotesSubjectViewModel.class);
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
        pdfListListener.remove();
    }

}
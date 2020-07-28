package com.example.studystation.motivation;

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

import com.example.studystation.R;
import com.example.studystation.adapters.MotivationAdapter;
import com.example.studystation.adapters.StudentNotesListAdapter;
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.databaseClasses.DocList;
import com.example.studystation.databaseClasses.Motivation;
import com.example.studystation.studentNotes.StudentNotesList;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class MotivationList extends Fragment {

    private MotivationListViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<Motivation> motivationList;
    FirebaseFirestore db;
    ListenerRegistration motivationListListener;
    public static String id;

    public static MotivationList newInstance() {
        return new MotivationList();
    }

    @Override
    public void onStart() {
        super.onStart();

        db = FirebaseFirestore.getInstance();
        motivationListListener = db.collection("Motivation")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            id = documentSnapshot.getId();
                            com.example.studystation.databaseClasses.MotivationList moList = documentSnapshot.toObject(com.example.studystation.databaseClasses.MotivationList.class);
                            motivationList.clear();
                            for (Motivation motivation : moList.getMotivationContent()) {
                                motivationList.add(0, motivation);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.motivation_list_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.motivationListRecyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        motivationList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MotivationAdapter(motivationList, MotivationList.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MotivationListViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStop() {
        super.onStop();
        motivationListListener.remove();
    }

}
package com.example.studystation.notes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.adapters.NotesAdapter;
import com.example.studystation.databaseClasses.Favourite;
import com.example.studystation.databaseClasses.Note;
import com.example.studystation.databaseClasses.User;
import com.example.studystation.dialog.AddNotesDialog;
import com.example.studystation.dialog.RatingDialog;
import com.example.studystation.studentNotes.PdfDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NoteList extends Fragment implements AddNotesDialog.OnInputSelected {

    private NoteListViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<Note> noteList;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String email;
    String id;
    ListenerRegistration noteListListener;
    FloatingActionButton addNotes;
    String heading , content;
    int position;

    public static NoteList newInstance() {
        return new NoteList();
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        email = firebaseUser.getEmail();


        db = FirebaseFirestore.getInstance();
        noteListListener = db.collection("User").whereEqualTo("email" , email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            id = documentSnapshot.getId();
                            try {
                                User user = documentSnapshot.toObject(User.class);
                                noteList.clear();
                                for (Note note : user.getNoteList()) {
                                    noteList.add(0, note);
                                    adapter.notifyDataSetChanged();
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }

                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_list_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.notesListRecyclerView);
        addNotes = (FloatingActionButton) rootView.findViewById(R.id.notesFloatingActionButton);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotesAdapter(noteList, NoteList.this);
        recyclerView.setAdapter(adapter);

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNotesDialog dialog = new AddNotesDialog();
                dialog.setTargetFragment(NoteList.this, 1);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "AddNotesDialog");
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
                deleteNote();
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void deleteNote() {
        try {

            db.collection("User").document(id).update("noteList", FieldValue.arrayRemove(noteList.get(position)))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Note Deleted !", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendInput(String headingInput , String contentInput) {
        heading = headingInput;
        content = contentInput;
        updateDatabase();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStop() {
        super.onStop();
        noteListListener.remove();
    }

    private void updateDatabase() {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile", Context.MODE_PRIVATE);
        final String department = sharedPreferences.getString("department", "");
        final String section = sharedPreferences.getString("section", "");

        db.collection("User").whereEqualTo("email" , email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == 0){
                    List<Favourite> favouriteList = new ArrayList<>();
//                    List<String> content = new ArrayList<>();
//                    content.add(addNote.getText().toString());
                    Note note = new Note(heading , content , "");
                    List<Note> noteList = new ArrayList<>();
                    noteList.add(note);

                    User user = new User(email , department , section , favouriteList , noteList);
                    db.collection("User").document().set(user);

                }else {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    final String id = documentSnapshot.getId();
//                    List<String> content = new ArrayList<>();
//                    content.add(addNote.getText().toString());
                    Note note = new Note(heading , content , "");
                    db.collection("User").document(id).update("noteList" , FieldValue.arrayUnion(note)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Note Added :)", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}
package com.example.studystation.news;

import androidx.lifecycle.ViewModelProviders;

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
import com.example.studystation.adapters.NewsAdapter;
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.databaseClasses.Motivation;
import com.example.studystation.databaseClasses.News;
import com.example.studystation.motivation.MotivationList;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class NewsList extends Fragment {

    private NewsListViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<News> newsList;
    FirebaseFirestore db;
    ListenerRegistration newsListListener;

    public static NewsList newInstance() {
        return new NewsList();
    }

    public void onStart() {
        super.onStart();

        db = FirebaseFirestore.getInstance();
        newsListListener = db.collection("News")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            com.example.studystation.databaseClasses.NewsList nsList = documentSnapshot.toObject(com.example.studystation.databaseClasses.NewsList.class);
                            newsList.clear();
                            for (News news : nsList.getNewsList()) {
                                newsList.add(0, news);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_list_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.newsRecyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter(newsList, NewsList.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsListViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStop() {
        super.onStop();
        newsListListener.remove();
    }

}
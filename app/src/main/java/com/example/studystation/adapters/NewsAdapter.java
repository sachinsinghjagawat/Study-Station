package com.example.studystation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.Motivation;
import com.example.studystation.databaseClasses.News;
import com.example.studystation.motivation.MotivationList;
import com.example.studystation.motivation.WebView;
import com.example.studystation.news.NewsList;
import com.example.studystation.news.NewsPdf;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    List<News> newsList;
    NewsList context;

    public NewsAdapter(List<News> newsList, NewsList context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_view , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsAdapter.ViewHolder holder, int position) {
        final News news = newsList.get(position);

        holder.newsHeading.setText(news.getHeading());
        Picasso.get().load(news.getImageUrl()).fit().into(holder.newsImage);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news.getType().equals("PDF")){
                    NewsPdf fragment = NewsPdf.newInstance(news.getLink());

                    FragmentManager fragmentManager = context.getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else {
                    WebView fragment = WebView.newInstance(news.getLink());

                    FragmentManager fragmentManager = context.getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage ;
        TextView newsHeading;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsHeading = itemView.findViewById(R.id.newsHeading);
            linearLayout = itemView.findViewById(R.id.newsLinearLayout);

        }
    }
}

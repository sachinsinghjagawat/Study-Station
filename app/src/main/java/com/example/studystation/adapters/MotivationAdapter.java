package com.example.studystation.adapters;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.Motivation;
import com.example.studystation.motivation.MotivationList;
import com.example.studystation.motivation.WebView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MotivationAdapter extends RecyclerView.Adapter<MotivationAdapter.ViewHolder> {

    List<Motivation> motivationList;
    MotivationList context;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String email;
    long time;
    boolean firstTouch = false;
    AnimatedVectorDrawable avd2 ;
    AnimatedVectorDrawableCompat avd;

    public MotivationAdapter(List<Motivation> motivationList, MotivationList context) {
        this.motivationList = motivationList;
        this.context = context;

//        db = FirebaseFirestore.getInstance();
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        assert firebaseUser != null;
//        email = firebaseUser.getEmail();

    }

    @NonNull
    @Override
    public MotivationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.motivation_card_view , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MotivationAdapter.ViewHolder holder, int position) {
        final Motivation motivation = motivationList.get(position);
        holder.motivationContent.setText(motivation.getContent());
        int count = (int) motivation.getLike();
        holder.likeCount.setVisibility(View.GONE);
        Picasso.get().load(motivation.getImageUrl()).fit().into(holder.motivationImage);

        holder.linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView fragment = WebView.newInstance(motivation.getExternalLink());

                FragmentManager fragmentManager = context.getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_right , R.anim.enter_from_right , R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.motivationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstTouch && (System.currentTimeMillis() - time) <= 300) {

                    Drawable drawable = holder.imagePopUp.getDrawable();
                    holder.imagePopUp.setAlpha(0.8f);

                    if (drawable instanceof AnimatedVectorDrawableCompat){
                        avd = (AnimatedVectorDrawableCompat) drawable;
                        avd.start();
                    }else if (drawable instanceof AnimatedVectorDrawable){
                        avd2 = (AnimatedVectorDrawable) drawable;
                        avd2.start();
                    }

                    Picasso.get().load(R.drawable.heart_colored).fit().into(holder.likeImage);
                    holder.likeImage.setTag("colored");
                    firstTouch = false;

                } else {
                    firstTouch = true;
                    time = System.currentTimeMillis();
                    Log.e("** SINGLE  TAP**"," First Tap time  "+time);
                }
            }
        });


        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.likeImage.getTag().equals("outline")) {
                    Picasso.get().load(R.drawable.heart_colored).fit().into(holder.likeImage);
                    holder.likeImage.setTag("colored");
                }else {
                    Picasso.get().load(R.drawable.love_heart_outline).fit().into(holder.likeImage);
                    holder.likeImage.setTag("outline");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return motivationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView motivationImage , likeImage , imagePopUp;
        TextView linkText , motivationContent , likeCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            motivationImage = itemView.findViewById(R.id.motivationImage);
            likeImage = itemView.findViewById(R.id.likeImage);
            motivationContent = itemView.findViewById(R.id.motivationContent);
            linkText = itemView.findViewById(R.id.linkText);
            likeCount = itemView.findViewById(R.id.likeCount);
            imagePopUp = itemView.findViewById(R.id.imagePopUp);

        }
    }
}

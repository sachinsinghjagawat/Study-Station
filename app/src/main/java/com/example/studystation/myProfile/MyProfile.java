package com.example.studystation.myProfile;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studystation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MyProfile extends Fragment {

    private MyProfileViewModel mViewModel;
    FirebaseAuth mAuth;
    ImageView profilePhoto;
    TextView name , email , section , department;

    public static MyProfile newInstance() {
        return new MyProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_profile_fragment, container, false);
        profilePhoto = (ImageView) rootView.findViewById(R.id.profilePhoto2);
        name = rootView.findViewById(R.id.editTextName2);
        email = rootView.findViewById(R.id.textEmail2);
        section = rootView.findViewById(R.id.sectionTextView2);
        department = rootView.findViewById(R.id.departmentTextView2);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        name.setText(firebaseUser.getDisplayName());
        email.setText(firebaseUser.getEmail());
        Picasso.get().load(firebaseUser.getPhotoUrl()).fit().centerCrop().into(profilePhoto);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile" , Context.MODE_PRIVATE);
        section.setText(sharedPreferences.getString("section",  ""));
        department.setText(sharedPreferences.getString("department",  ""));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
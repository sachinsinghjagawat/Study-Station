package com.example.studystation.myProfile;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.studystation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MyProfile extends Fragment {

    private MyProfileViewModel mViewModel;
    FirebaseAuth mAuth;
    ImageView profilePhoto;
    TextView name , email , section , department;
    Switch darkModeSwitch;
    SharedPreferences sharedPreferenceTheme;

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
        darkModeSwitch = rootView.findViewById(R.id.darkModeSwitch);

        sharedPreferenceTheme = this.getActivity().getSharedPreferences("com.example.studystation.myProfile" , Context.MODE_PRIVATE);



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

        if (sharedPreferences.getBoolean("DarkMode" , false)) {
            darkModeSwitch.setChecked(true);
            sharedPreferenceTheme.edit().putBoolean("DarkMode", true).apply();
        } else {
            darkModeSwitch.setChecked(false);
            sharedPreferenceTheme.edit().putBoolean("DarkMode", false).apply();
        }

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPreferenceTheme.edit().putBoolean("DarkMode", true).apply();
                    Log.i("Message from sachuda", "I WON");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPreferenceTheme.edit().putBoolean("DarkMode", true).apply();
                    //triggerRebirth(requireContext());
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPreferenceTheme.edit().putBoolean("DarkMode", false).apply();
                    //triggerRebirth(requireContext());

                }
            }
        });

    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
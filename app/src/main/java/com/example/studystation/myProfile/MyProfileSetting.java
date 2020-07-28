package com.example.studystation.myProfile;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studystation.MainActivity;
import com.example.studystation.R;
import com.example.studystation.professorNotes.ProfessorNotesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MyProfileSetting extends Fragment {

    private MyProfileSettingViewModel mViewModel;
    Spinner department , section ;
    TextView name;
    TextView email;
    ImageView userImage;
    String departmentSelected , sectionSelected ;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    Button save;

    public static MyProfileSetting newInstance() {
        return new MyProfileSetting();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_profile_setting_fragment, container, false);
        department =(Spinner) rootView.findViewById(R.id.departmentSpinner);
        section = (Spinner) rootView.findViewById(R.id.sectionSpinner);
        name = (TextView) rootView.findViewById(R.id.editTextName);
        email = (TextView) rootView.findViewById(R.id.textEmail);
        userImage = (ImageView) rootView.findViewById(R.id.profilePhoto);
        save = (Button) rootView.findViewById(R.id.buttonSave);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        name.setText(firebaseUser.getDisplayName());
        email.setText(firebaseUser.getEmail());
        Picasso.get().load(firebaseUser.getPhotoUrl()).fit().centerCrop().into(userImage);

        sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile" , Context.MODE_PRIVATE);


        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(getContext() , R.array.departmentList , android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.createFromResource(getContext() , R.array.sectionList , android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(departmentAdapter);
        section.setAdapter(sectionAdapter);

        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectionSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyProfileSettingViewModel.class);
        // TODO: Use the ViewModel
    }

        public void save (){
        if(!name.getText().toString().isEmpty()){
            if (departmentSelected.equals("Metallurgy") && sectionSelected.equals("A")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }
            if (departmentSelected.equals("Metallurgy") && sectionSelected.equals("B")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }
            if (departmentSelected.equals("Chemical") && sectionSelected.equals("A")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }
            if (departmentSelected.equals("Chemical") && sectionSelected.equals("B")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }
            if (departmentSelected.equals("Civil") && sectionSelected.equals("No Section")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }
            if (departmentSelected.equals("Computer Science") && sectionSelected.equals("No Section")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }if (departmentSelected.equals("Electrical") && sectionSelected.equals("No Section")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }if (departmentSelected.equals("Electronics And Communication") && sectionSelected.equals("No Section")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }if (departmentSelected.equals("Production") && sectionSelected.equals("No Section")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }if (departmentSelected.equals("Instrumentation And Control") && sectionSelected.equals("No Section")){
                Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
                return;
            }



            new AlertDialog.Builder(getContext())
                    .setTitle("Save")
                    .setMessage("Are you sure you want to save it ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sharedPreferences.edit().putString("department" , departmentSelected).apply();
                            sharedPreferences.edit().putString("section" , sectionSelected).apply();

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_right , R.anim.enter_from_right , R.anim.exit_to_right);
                            fragmentTransaction.replace(R.id.fragmentContainer, new MyProfile());
//                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

//                            Intent intent = new Intent(MyProfileSetting.this , MainActivity.class);
//                            startActivity(intent);
                        }
                    }).setNegativeButton("Cancel" , null).show();


        }
    }

}
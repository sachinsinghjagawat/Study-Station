package com.example.studystation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studystation.draw.CreativeFragment;
import com.example.studystation.extraActivity.Help;
import com.example.studystation.getDemand.GetDemandList;
import com.example.studystation.motivation.MotivationList;
import com.example.studystation.myProfile.MyProfile;
import com.example.studystation.myProfile.MyProfileSetting;
import com.example.studystation.news.NewsList;
import com.example.studystation.notes.NoteList;
import com.example.studystation.professorNotes.ProfessorNotesSubject;
import com.example.studystation.putDemand.PutDemand;
import com.example.studystation.studentNotes.AddStudentNotes;
import com.example.studystation.studentNotes.StudentNotesSubject;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    public static final int KEY = 1;
    NavigationView navigationView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() == null) {
            MainActivity.this.finish();
            Toast.makeText(MainActivity.this, "Not LoggedIn Properly", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.studystation.myProfile" , Context.MODE_PRIVATE);


        if (sharedPreferences.getBoolean("DarkMode" , false)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String name = firebaseUser.getDisplayName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        TextView userId = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userId);
        ImageView profilePictureView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userPhoto);
        userName.setText(firebaseUser.getDisplayName());
        userId.setText(firebaseUser.getEmail());
        Picasso.get().load(firebaseUser.getPhotoUrl()).centerCrop().fit().into(profilePictureView);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Intent intent = getIntent();
        boolean firstSignIn = intent.getBooleanExtra("firstSignIn" , false);
        if (firstSignIn == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer , new MyProfileSetting()).commit();
            navigationView.setCheckedItem(R.id.my_profile);
        }else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new StudentNotesSubject()).commit();
                navigationView.setCheckedItem(R.id.student_notes);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.professor_notes : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new ProfessorNotesSubject());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                setTitle("Welcome");
//                navigationView.setCheckedItem(R.id.professor_notes);

                break;
            }
            case R.id.student_notes : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new StudentNotesSubject());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                navigationView.setCheckedItem(R.id.student_notes);

                break;
            }
            case R.id.notes : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new NoteList());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                navigationView.setCheckedItem(R.id.put_demand);

                break;
            }
            case R.id.creativeCorner : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new CreativeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Toast.makeText(this, "Start drawing anything", Toast.LENGTH_SHORT).show();

                break;
            }


            case R.id.news : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new NewsList());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                navigationView.setCheckedItem(R.id.news);

                break;
            }
            case R.id.motivation : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new MotivationList());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                navigationView.setCheckedItem(R.id.motivation);

                break;
            }
            case R.id.my_profile : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new MyProfile());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                navigationView.setCheckedItem(R.id.my_profile);

                break;
            }
            case R.id.help : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new Help());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Toast.makeText(this, "Feel free to contact me :)", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.feedback : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, new Help());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Toast.makeText(this, "Please Leave your feedback on my WhatsApp", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.social_media_handle : {
                Toast.makeText(this, "This button is under construction :(", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.terms_and_condition : {
                Toast.makeText(this, "This button is under construction :(", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void changeProfile(View view) {

        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_right , R.anim.enter_from_right , R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.fragmentContainer, new MyProfileSetting());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void signOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this , GoogleSignInActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == KEY && resultCode==RESULT_OK && data!= null && data.getData() != null ){
            Uri uri = data.getData();
            AddStudentNotes fragment = new AddStudentNotes();
            Bundle args = new Bundle();
            args.putString("uri" , uri.toString());
            fragment.setArguments(args);
        }
    }

}
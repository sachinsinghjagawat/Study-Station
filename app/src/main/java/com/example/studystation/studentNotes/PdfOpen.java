package com.example.studystation.studentNotes;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.Favourite;
import com.example.studystation.databaseClasses.Note;
import com.example.studystation.databaseClasses.User;
import com.example.studystation.dialog.AddNoteInPdf;
import com.example.studystation.dialog.MusicSelectionDialog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PdfOpen extends Fragment implements MusicSelectionDialog.OnInputSelected , AddNoteInPdf.OnInputSelected {

    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            Activity activity = getActivity();
            if (activity != null
                    && activity.getWindow() != null) {
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Runnable updater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()){
                musicProgress.setProgress((mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100 );
                handler.postDelayed(updater , 1000);
            }
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
                try {
                    mControlsView.setVisibility(View.VISIBLE);
                    mControlsView.setTranslationY(0100);
                    mControlsView.setAlpha(0);
                    mControlsView.animate().alpha(1).translationY(000).setDuration(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 00;

    private static final String TAG = "PdfFragment";
    OvershootInterpolator interpolator = new OvershootInterpolator();
    private View mContentView;
    private View mControlsView;
    public PDFView pdfView;
    ProgressBar progressBar;
    String urlOfPdf , title , content;
    FloatingActionButton fabNightMode , fabAddNote , fabMusic , fabMain;
    SeekBar musicProgress;
//    EditText addNote;
//    Button submitNote;
//    LinearLayout addNoteLinearLayout;
    MediaPlayer mediaPlayer;
    String musicUrl;
    Handler handler = new Handler();
    FirebaseFirestore db;
    String email , department , section;
    FirebaseAuth mAuth;
    boolean isMenuOpen = false;
    boolean nightMode = false;
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public static PdfOpen newInstance(String url , String title) {
        PdfOpen fragment = new PdfOpen();
        Bundle args = new Bundle();
        args.putString("url" , url);
        args.putString("title" , title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_open, container, false);
        mControlsView = view.findViewById(R.id.fullscreen_content_controls);
        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        fabNightMode = view.findViewById(R.id.fabDraw);
        fabAddNote = view.findViewById(R.id.fabAddNote);
//        fabReadText = view.findViewById(R.id.fabReadText);
        fabMusic = view.findViewById(R.id.fabMusic);
        fabMain = view.findViewById(R.id.fabMain);
        musicProgress = view.findViewById(R.id.musicProgress);
//        addNote = view.findViewById(R.id.addNote);
//        submitNote = view.findViewById(R.id.submitNote);
//        addNoteLinearLayout = view.findViewById(R.id.addNoteLinearLayout);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        email = firebaseUser.getEmail();

        mediaPlayer = new MediaPlayer();
        musicProgress.setMax(100);

        mVisible = true;
        closeMenu();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile", Context.MODE_PRIVATE);
        department = sharedPreferences.getString("department", "");
        section = sharedPreferences.getString("section", "");

        musicProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int playPosition = ( mediaPlayer.getDuration() / 100 ) * progress;
                mediaPlayer.seekTo(playPosition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (getArguments() != null) {
            urlOfPdf = getArguments().getString("url");
            title = getArguments().getString("title");
        }

        new RetrivePdfStream().execute(urlOfPdf);

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen){
                    closeMenu();
                }else {
                    openMenu();
                }
            }
        });

        fabNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMode){
                    nightMode = false;
                    synchronized (pdfView){
                        pdfView.setNightMode(false);
                    }
                }else {
                    nightMode = true;
                    synchronized (pdfView){
                        pdfView.setNightMode(true);
                    }
                    Toast.makeText(getContext(), "Click The PDF To Update The Changes", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicSelectionDialog dialog = new MusicSelectionDialog();
                dialog.setTargetFragment(PdfOpen.this, 1);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "MusicDialog");
//                prepareMediaPlayer();
            }
        });

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteInPdf dialog = new AddNoteInPdf();
                dialog.setTargetFragment(PdfOpen.this, 1);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "AddNotesInPdfDialog");

            }
        });

//        submitNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!addNote.getText().toString().isEmpty()){
//                    updateDatabase();
//                    addNote.setText("");
//                    addNoteLinearLayout.setVisibility(View.GONE);
//                    InputMethodManager mgr = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    assert mgr != null;
//                    mgr.hideSoftInputFromWindow(addNote.getWindowToken() , 0);
//
//                }
//            }
//        });

    }

    private void updateDatabase() {

        db.collection("User").whereEqualTo("email" , email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == 0){
                    List<Favourite> favouriteList = new ArrayList<>();
//                    List<String> content = new ArrayList<>();
//                    content.add(addNote.getText().toString());
                    Note note = new Note(title , content , urlOfPdf);
                    List<Note> noteList = new ArrayList<>();
                    noteList.add(note);

                    User user = new User(email , department , section , favouriteList , noteList);
                    db.collection("User").document().set(user);

                }else {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    final String id = documentSnapshot.getId();
//                    List<String> content = new ArrayList<>();
//                    content.add(addNote.getText().toString());
                    Note note = new Note(title , content , urlOfPdf);
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

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);

        musicUrl = input;

        if (musicUrl.equals("STOP")){
            mediaPlayer.reset();
            musicProgress.setProgress(0);
            musicProgress.setVisibility(View.GONE);
            return;
        }
        mediaPlayer.reset();
        Toast.makeText(getActivity(), "loading...", Toast.LENGTH_SHORT).show();
        prepareMediaPlayer(musicUrl);
    }

    @Override
    public void sendInputInPdf(String contentInput) {
        Log.d(TAG, "sendInput: found incoming input: " + contentInput);

        content = contentInput;
        updateDatabase();
    }

    private void prepareMediaPlayer(String url) {
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepare();
            mediaPlayer.prepareAsync();
//            mediaPlayer.start();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Something Went Wrong\nPlease Check Your Internet", Toast.LENGTH_SHORT).show();
            e.getStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                musicProgress.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Music Started Enjoy :)", Toast.LENGTH_SHORT).show();
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                musicProgress.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicProgress.setProgress(0);
                mediaPlayer.reset();
                musicProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Music Completed :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeMenu() {
        fabNightMode.setVisibility(View.GONE);
        fabAddNote.setVisibility(View.GONE);
//        fabReadText.setVisibility(View.GONE);
        fabMusic.setVisibility(View.GONE);

        fabNightMode.setAlpha(0f);
        fabAddNote.setAlpha(0f);
//        fabReadText.setAlpha(0f);
        fabMusic.setAlpha(0f);

        fabMain.animate().rotation(0f);

        isMenuOpen = false;
    }

    private void openMenu() {
        fabNightMode.setVisibility(View.VISIBLE);
        fabAddNote.setVisibility(View.VISIBLE);
//        fabReadText.setVisibility(View.VISIBLE);
        fabMusic.setVisibility(View.VISIBLE);

        fabNightMode.setTranslationY(100);
        fabNightMode.animate().alpha(1).translationY(0).setInterpolator(interpolator).setDuration(300);

        fabAddNote.setTranslationY(0100);
        fabAddNote.animate().alpha(1).translationY(000).setInterpolator(interpolator).setDuration(300);

//        fabReadText.setTranslationY(0100);
//        fabReadText.animate().alpha(1).translationY(000).setInterpolator(interpolator).setDuration(300);

        fabMusic.setTranslationY(0100);
        fabMusic.animate().alpha(1).translationY(000).setInterpolator(interpolator).setDuration(300);

        fabMain.animate().rotation(45f).setInterpolator(interpolator).setDuration(300);


        isMenuOpen = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        delayedHide(100);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        mContentView = null;
        mControlsView = null;
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        closeMenu();
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        pdfView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
    }

    class RetrivePdfStream extends AsyncTask<String , Void , InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;

            try {
                URL url = new URL (strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return inputStream;

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .onDraw(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                        }
                    })
                    .onDrawAll(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }) // called after document is loaded and starts to be rendered
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(getActivity(), "Something Went Wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onPageError(new OnPageErrorListener() {
                        @Override
                        public void onPageError(int page, Throwable t) {
                            Toast.makeText(getActivity(), "Could Not Load This Page :(", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .scrollHandle(new DefaultScrollHandle(getContext()))
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(nightMode) // toggle night mode
                    .load();
        }
    }

}
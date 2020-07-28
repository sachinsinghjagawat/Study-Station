package com.example.studystation.studentNotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.adapters.QuestionAdapter;
import com.example.studystation.adapters.StudentNotesListAdapter;
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.databaseClasses.DocDetails;
import com.example.studystation.databaseClasses.DocList;
import com.example.studystation.databaseClasses.Question;
import com.example.studystation.dialog.MusicSelectionDialog;
import com.example.studystation.dialog.RatingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PdfDetails extends Fragment implements RatingDialog.OnInputSelected {

    private PdfDetailsViewModel mViewModel;
    TextView heading , userName, rating , rateThisPdf;
    Button openPdf , downloadPdf , sendQuestion;
    EditText askQuestion;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    String urlOfPdf ;
    public static String id ;
    public static String username;
    String email;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DocDetails docDetails;
    List<Question> questionList;

    public static PdfDetails newInstance(String url) {
        PdfDetails pdfDetails = new PdfDetails();
        Bundle args = new Bundle();
        args.putString("url" , url);
        pdfDetails.setArguments(args);
        return pdfDetails;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            urlOfPdf = getArguments().getString("url");
        }
        db = FirebaseFirestore.getInstance();
        db.collection("PdfFiles").whereEqualTo("urlOfPdf" , urlOfPdf).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    id = documentSnapshot.getId();
                    docDetails = documentSnapshot.toObject(DocDetails.class);
                    heading.setText(docDetails.getNameOfPdf());
                    userName.setText("-by " + docDetails.getNameOfUser());
                    double ratingValue = (docDetails.getSumRating() / docDetails.getTotalRating()) * 5 ;
                    int r1 = (int) (ratingValue * 100);
                    float r2 = (float) r1 / 100 ;
                    rating.setText(r2 + "");

                    questionList.clear();
                    for (Question question : docDetails.getDiscussion()) {
                        questionList.add(0, question);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pdf_details_fragment, container, false);
        heading = rootView.findViewById(R.id.pdfHeading);
        userName = rootView.findViewById(R.id.pdfUsername);
        rating = rootView.findViewById(R.id.pdfRating);
        rateThisPdf = rootView.findViewById(R.id.pdfRateThisPdf);
        openPdf = rootView.findViewById(R.id.pdfOpen);
        downloadPdf = rootView.findViewById(R.id.pdfDownload);
        sendQuestion = rootView.findViewById(R.id.pdfSendQuestion);
        askQuestion = rootView.findViewById(R.id.pdfAskQuestion);
        recyclerView = rootView.findViewById(R.id.pdfDiscussionRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        username = firebaseUser.getDisplayName();
        email = firebaseUser.getEmail();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionList = new LinkedList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionAdapter(questionList, PdfDetails.this);
        recyclerView.setAdapter(adapter);

        openPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfOpen fragment = PdfOpen.newInstance(docDetails.getUrlOfPdf() , docDetails.getNameOfPdf());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_to_right , R.anim.enter_from_right , R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        downloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
////                intent.setType(Intent.ACTION_OPEN_DOCUMENT);
//                intent.setData(Uri.parse(docDetails.getUrlOfPdf()));
//                startActivity(intent);
                new DownloadTask(getContext(), urlOfPdf);
            }
        });

        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askQuestion.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please Enter Your Question", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendQuestion();
            }
        });

        rateThisPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (docDetails.getUsers().contains(email)) {
                        Toast.makeText(getActivity(), "You Already Rated This Document !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                RatingDialog dialog = new RatingDialog();
                dialog.setTargetFragment(PdfDetails.this, 1);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "RatingDialog");
            }
        });


    }

    @Override
    public void sendInput(double input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);

        WriteBatch batch = db.batch();
        DocumentReference doc1 = db.collection("PdfFiles").document(id);
        batch.update(doc1 , "sumRating" , docDetails.getSumRating() + input);
        batch.update(doc1 , "totalRating" , docDetails.getTotalRating() + 5);
        batch.update(doc1 , "users"  , FieldValue.arrayUnion(email));
        batch.commit();

    }


    private void sendQuestion() {
        String askedQuestion = askQuestion.getText().toString() + "  -by " + username ;
        Question question = new Question();
        question.setQuestion(askedQuestion);

        db.collection("PdfFiles").document(id).update("discussion" , FieldValue.arrayUnion(question));
        askQuestion.setText("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PdfDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

    public class DownloadTask {
        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;

            this.downloadUrl = downloadUrl;


            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
            Log.e(TAG, downloadFileName);

            //Start Downloading Task
            new DownloadingTask().execute();
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {

            File apkStorage = null;
            File outputFile = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();
//                        ContextThemeWrapper ctw = new ContextThemeWrapper( context, R.style.Theme_AlertDialog);
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("Document  ");
                        alertDialogBuilder.setMessage("Document Downloaded Successfully ");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                        alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/CodePlayon/" + downloadFileName);  // -> filename = maven.pdf
                                Uri path = Uri.fromFile(pdfFile);
                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.setDataAndType(path, "application/pdf");
                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try{
                                    context.startActivity(pdfIntent);
                                }catch(ActivityNotFoundException e){
                                    Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialogBuilder.show();
//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    //Change button text if exception occurs

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

                }


                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
//                    if (new CheckForSDCard().isSDCardPresent()) {

                        apkStorage = new File(Environment.getDownloadCacheDirectory() + "/" + "CodePlayon");
//                    } else
//                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }

                return null;
            }
        }
    }

    public class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }

}
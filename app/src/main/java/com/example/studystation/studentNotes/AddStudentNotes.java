package com.example.studystation.studentNotes;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studystation.R;
import com.example.studystation.databaseClasses.DocCardViewDetails;
import com.example.studystation.databaseClasses.DocDetails;
import com.example.studystation.databaseClasses.DocList;
import com.example.studystation.databaseClasses.Question;
import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.studystation.studentNotes.NotificationClass.CHANNEL_1_ID;
import static com.example.studystation.studentNotes.NotificationClass.CHANNEL_2_ID;

public class AddStudentNotes extends Fragment {

    private AddStudentNotesViewModel mViewModel;
    FirebaseAuth mAuth;
    Button selectFileButton , uploadButton;
    EditText nameOfFile;
    TextView subject , department, section;
    ImageView photo;
    ProgressBar progressBar;
    String sectionText , departmentText , subjectText , pdfUrl , imageUrl , userName , uriString ;
    Uri uri;
    NotificationManagerCompat notificationManager;

    public static final int KEY = 1;

    WriteBatch batch;
    private StorageReference mStorageRef;
    FirebaseFirestore db;
    StorageTask<UploadTask.TaskSnapshot> mPdfUploadTask;

    public static AddStudentNotes newInstance(String subject) {
        AddStudentNotes fragment = new AddStudentNotes();
        Bundle args = new Bundle();
        args.putString("subject" , subject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_student_notes_fragment, container, false);
        selectFileButton = rootView.findViewById(R.id.selectFileButton3);
        uploadButton = rootView.findViewById(R.id.buttonUpload3);
        nameOfFile = rootView.findViewById(R.id.nameOfPdfEditText3);
        subject = rootView.findViewById(R.id.textSubject3);
        department = rootView.findViewById(R.id.textDepartment3);
        section = rootView.findViewById(R.id.textSection3);
        photo = rootView.findViewById(R.id.profilePhoto3);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarImage3);

        notificationManager = NotificationManagerCompat.from(getContext());
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            subjectText = getArguments().getString("subject" , "");
            subject.setText(subjectText);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        userName = firebaseUser.getDisplayName();
        imageUrl = firebaseUser.getPhotoUrl().toString();
        Picasso.get().load(imageUrl).centerCrop().fit().into(photo);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.studystation.myProfile", Context.MODE_PRIVATE);
        departmentText = sharedPreferences.getString("department", "");
        sectionText = sharedPreferences.getString("section", "");

        department.setText(departmentText);
        section.setText(sectionText);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfChooser();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddStudentNotesViewModel.class);
        // TODO: Use the ViewModel
    }

    public void selectFile (){

        if(nameOfFile.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Give Proper Details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mPdfUploadTask != null && mPdfUploadTask.isInProgress()) {
            Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
        }
    }

    private void uploadFile() {

        String path = departmentText + sectionText;
        mStorageRef = FirebaseStorage.getInstance().getReference(path);

        if (getArguments() != null) {
            uriString = getArguments().getString("uri" , "");
        }

        if ( uri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + "pdf");

            fileReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pdfUrl = uri.toString();
                            updateDatabase();
                        }
                    });
                }
            });

            mPdfUploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 5000);

                            Toast.makeText(getActivity(), "PDF Upload Successful", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });


        } else {
            Toast.makeText(getActivity() , "Please Select PDF", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateDatabase() {
        batch = db.batch();

        db.collection("PdfList").whereEqualTo("department" , departmentText)
                .whereEqualTo("section" , sectionText)
                .whereEqualTo("subject" , subjectText)
                .whereEqualTo("type" , "Student Notes" )
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() == 0){

                    DocCardViewDetails docCardViewDetails = new DocCardViewDetails(nameOfFile.getText().toString() , userName ,
                            0.0 , imageUrl , pdfUrl);
                    List<DocCardViewDetails> docCardViewDetailsList = new ArrayList<>();
                    docCardViewDetailsList.add(docCardViewDetails);
                    DocList docList = new DocList(departmentText , sectionText , subjectText ,
                            "Student Notes" , docCardViewDetailsList);

                    DocumentReference doc1 = db.collection("PdfList").document();
                    batch.set(doc1 , docList);

                    DocumentReference doc2 = db.collection("PdfFiles").document();
                    List<String > ans = new ArrayList<>();
                    ans.add("");
                    Question question = new Question("" , ans);
                    List<Question> questionList = new ArrayList<>();
                    List<String> users = new ArrayList<>();
                    DocDetails docDetails = new DocDetails(departmentText , sectionText , subjectText ,
                            "Student Notes" , nameOfFile.getText().toString() , userName ,
                            5.0 , 5.0 , pdfUrl , questionList , users);
                    batch.set(doc2 , docDetails);
                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Database Updated ", Toast.LENGTH_SHORT).show();
                            Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)
                                    .setSmallIcon(R.drawable.thanks_icon)
                                    .setContentTitle("Vote Of Thanks")
                                    .setContentText("Thank For Sharing your notes in this Community :)")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManager.notify(1, notification);

                            Notification notification2 = new NotificationCompat.Builder(getContext(), CHANNEL_2_ID)
                                    .setSmallIcon(R.drawable.app_icon)
                                    .setContentTitle("Notes Update")
                                    .setContentText("A New Note is uploaded in " + subjectText)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManager.notify(2, notification2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    String id = documentSnapshot.getId();
                    DocCardViewDetails docCardViewDetails = new DocCardViewDetails(nameOfFile.getText().toString() , userName ,
                            0.0 , imageUrl , pdfUrl);
                    DocumentReference doc1 = db.collection("PdfList").document(id);
//                    doc1.update("docCardViewDetailsList" , FieldValue.arrayUnion(docCardViewDetails));
                    batch.update(doc1 , "docCardViewDetailsList" , FieldValue.arrayUnion(docCardViewDetails));

                    DocumentReference doc2 = db.collection("PdfFiles").document();
                    List<String > ans = new ArrayList<>();
                    ans.add("");
                    Question question = new Question("" , ans);
                    List<Question> questionList = new ArrayList<>();
                    List<String> users = new ArrayList<>();
                    DocDetails docDetails = new DocDetails(departmentText , sectionText , subjectText ,
                            "Student Notes" , nameOfFile.getText().toString() , userName ,
                            5.0 , 5.0 , pdfUrl , questionList , users);
                    batch.set(doc2 , docDetails);
                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Database Updated ", Toast.LENGTH_SHORT).show();
                            Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_1_ID)
                                    .setSmallIcon(R.drawable.thanks_icon)
                                    .setContentTitle("Vote Of Thanks")
                                    .setContentText("Thank For Sharing your notes in this Community :)")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManager.notify(1, notification);

                            Notification notification2 = new NotificationCompat.Builder(getContext(), CHANNEL_2_ID)
                                    .setSmallIcon(R.drawable.app_icon)
                                    .setContentTitle("Notes Update")
                                    .setContentText("A New Note is uploaded in " + subjectText)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManager.notify(2, notification2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == KEY && resultCode==RESULT_OK && data!= null && data.getData() != null ){
            uri = data.getData();
        }
    }


    public void openPdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , KEY);
    }

}
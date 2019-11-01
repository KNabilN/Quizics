package com.nawa.android.physix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Admin extends AppCompatActivity {

    EditText Link, ques1, ques2, ques3, ques4, ques5, ques6, ques7, ques8, ques9, ques10;

    private String link,
            q1, q2, q3, q4, q5, q6, q7, q8, q9, q10;
    private static final int GallaryPich = 1;
    private EditText A, B, C,D,E ,Code, Ques , Min , Sec;
    private String a, b, c,d,e, code, ques , downloadImageUrl ;
    private Long  min , sec;
    private Button btn;
    private Uri imageUri;
    private StorageReference ImagesRef;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private String type;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        type = getIntent().getStringExtra("type");
        progressDialog = new ProgressDialog(this);
        if (type.equalsIgnoreCase("professor")) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin);
            imageView = findViewById(R.id.image);
            A = findViewById(R.id.admin_A);
            B = findViewById(R.id.admin_B);
            C = findViewById(R.id.admin_C);
            D = findViewById(R.id.admin_D);
            E = findViewById(R.id.admin_E);
            Code = findViewById(R.id.admin_codeE);
            Ques = findViewById(R.id.admin_quesE);
            btn = findViewById(R.id.admin_quizB);
            Min = findViewById(R.id.minutes);
            Sec = findViewById(R.id.seconds);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            ImagesRef = FirebaseStorage.getInstance().getReference().child("Places Images");


            //افتح الاستديو
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //choose photo from gallery method
                    openGallary();
                }
            });


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setTitle("Quiz preparation");
                    progressDialog.setMessage("جاري التحقق من البيانات");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    checkData();

                }
            });

        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.team_admin);
            progressDialog = new ProgressDialog(this);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            Min = findViewById(R.id.tminutes);
            Sec = findViewById(R.id.tseconds);
            Code = findViewById(R.id.codeTeam);
            ques1 = findViewById(R.id.a11);
            ques2 = findViewById(R.id.a21);
            ques3 = findViewById(R.id.a31);
            ques4 = findViewById(R.id.a41);
            ques5 = findViewById(R.id.a51);
            ques6 = findViewById(R.id.a61);
            ques7 = findViewById(R.id.a71);
            ques8 = findViewById(R.id.a81);
            ques9 = findViewById(R.id.a91);
            ques10 = findViewById(R.id.a101);
            Link = findViewById(R.id.link);


            Button team = findViewById(R.id.team_confirm);
            Button delete = findViewById(R.id.delete_confirm);
            team.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    q1 = ques1.getText().toString();
                    q2 = ques2.getText().toString();
                    q3 = ques3.getText().toString();
                    q4 = ques4.getText().toString();
                    q5 = ques5.getText().toString();
                    q6 = ques6.getText().toString();
                    q7 = ques7.getText().toString();
                    q8 = ques8.getText().toString();
                    q9 = ques9.getText().toString();
                    q10 = ques10.getText().toString();
                    code = Code.getText().toString();
                    link = Link.getText().toString();
                    sec = Long.parseLong(Sec.getText().toString());
                    min = Long.parseLong(Min.getText().toString());

                    if (TextUtils.isEmpty(code) || TextUtils.isEmpty(q1) || TextUtils.isEmpty(q2) || TextUtils.isEmpty(q3) || TextUtils.isEmpty(q4) ||
                            TextUtils.isEmpty(q5) || TextUtils.isEmpty(q6) || TextUtils.isEmpty(q7) ||
                            TextUtils.isEmpty(q8) || TextUtils.isEmpty(q9) || TextUtils.isEmpty(q10)) {

                        Toast.makeText(Admin.this, "Please check data", Toast.LENGTH_LONG).show();

                    } else {
                        progressDialog.setTitle("Quiz preparation");
                        progressDialog.setMessage("جاري التحقق من البيانات");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        createTeamQuiz();


                    }

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("TQuiz").removeValue();
                }
            });
        }

    }

    private void createTeamQuiz() {
        Long timer = (min * 60 * 1000) + (sec *1000) ;

        HashMap<String, Object> quiz = new HashMap<>();
        quiz.put("q1", q1);
        quiz.put("q2", q2);
        quiz.put("q3", q3);
        quiz.put("q4", q4);
        quiz.put("q5", q5);
        quiz.put("q6", q6);
        quiz.put("q7", q7);
        quiz.put("q8", q8);
        quiz.put("q9", q9);
        quiz.put("q10", q10);
        quiz.put("code", code);
        quiz.put("link", link);
        quiz.put("timer", timer);

        databaseReference.child("TQuiz").child(code).setValue(quiz).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Admin.this, "Quiz is ready by the code : " + code, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Admin.this, Code.class);
                    intent.putExtra("type", type);

                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Admin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkData() {
        a = A.getText().toString();
        b = B.getText().toString();
        c = C.getText().toString();
        d = D.getText().toString();
        e = E.getText().toString();
        code = Code.getText().toString();
        ques = Ques.getText().toString();
        sec = Long.parseLong(Sec.getText().toString());
        min = Long.parseLong(Min.getText().toString());
        if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c) || TextUtils.isEmpty(code) || TextUtils.isEmpty(ques)|| TextUtils.isEmpty(sec.toString())|| TextUtils.isEmpty(min.toString())
        || TextUtils.isEmpty(d) || TextUtils.isEmpty(e)) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please complete data required", Toast.LENGTH_SHORT).show();
        } else {
            saveData();
        }
    }

    private void saveData() {
        if (imageUri != null) {
            final StorageReference path = ImagesRef.child(imageUri.getLastPathSegment() + ".jpg");
            final UploadTask uploadTask = path.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Admin.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Admin.this, "good", Toast.LENGTH_SHORT).show();

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();


                            }
                            downloadImageUrl = path.getDownloadUrl().toString();
                            return path.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Toast.makeText(Admin.this, "got Url", Toast.LENGTH_SHORT).show();
                                saveInfoToDatabase();
                                //image is okay now the whole data

                            } else {
                                Toast.makeText(Admin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            });

        }else {
            saveInfoToDatabase();
        }
    }

    private void saveInfoToDatabase() {
        Long timer = (min * 60 * 1000) + (sec *1000) ;
        HashMap<String, Object> quiz = new HashMap<>();
        quiz.put("ques", ques);
        quiz.put("A", a);
        quiz.put("B", b);
        quiz.put("C", c);
        quiz.put("D", d);
        quiz.put("E", e);
        quiz.put("num", 0);
        quiz.put("anum", 0);
        quiz.put("bnum", 0);
        quiz.put("cnum", 0);
        quiz.put("dnum", 0);
        quiz.put("enumb", 0);
        quiz.put("timer", timer);
        quiz.put("image", downloadImageUrl);
        quiz.put("available", "yes");

        databaseReference.child("SQuiz").child(code).setValue(quiz).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Admin.this, "Quiz is ready by the code : " + code, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Admin.this, Code.class);
                    intent.putExtra("type", type);

                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Admin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallary() {
        //image getter intent
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //onActivity result
        startActivityForResult(galleryIntent, GallaryPich);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GallaryPich && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }
}

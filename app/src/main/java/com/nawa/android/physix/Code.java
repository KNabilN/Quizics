package com.nawa.android.physix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawa.android.physix.Prevelant.Prevelant;
import com.nawa.android.physix.model.SQuiz;
import com.nawa.android.physix.model.TQuiz;

import io.paperdb.Paper;

public class Code extends AppCompatActivity {

    private Button codeB, signout;
    private EditText codeE;

    DatabaseReference sQuezRef;
    FirebaseAuth mAuth;
    private String type;
    ProgressDialog progressDialog;
    Button studentquiz, teamquiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);


        studentquiz = findViewById(R.id.studentquiz);
        studentquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Code.this, Admin.class);
                intent.putExtra("type", type);
                startActivity(intent);
                finish();
            }
        });
        Paper.init(this);
        codeB = findViewById(R.id.codeB);
        type = getIntent().getStringExtra("type");
        progressDialog = new ProgressDialog(this);

        codeE = findViewById(R.id.codeE);
        mAuth = FirebaseAuth.getInstance();

        signout = findViewById(R.id.signoutB);

        if (type.equalsIgnoreCase("student")) {
            signout.setVisibility(View.GONE);
            studentquiz.setVisibility(View.GONE);
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(Code.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        codeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equalsIgnoreCase("student")) {
                    getSQuiz();
                } else if (type.equalsIgnoreCase("professor")){
                    getSQuiz();
                }else {
                    Toast.makeText(Code.this, "whhhhht !!!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void getSQuiz() {
        sQuezRef = FirebaseDatabase.getInstance().getReference().child("SQuiz");
        final String code = codeE.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            progressDialog.setTitle("Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            sQuezRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SQuiz sQuiz = dataSnapshot.getValue(SQuiz.class);
                        Intent intent = new Intent(Code.this, StudentQuiz.class);
                        intent.putExtra("ques", sQuiz.getQues());
                        intent.putExtra("A", sQuiz.getA());
                        intent.putExtra("B", sQuiz.getB());
                        intent.putExtra("C", sQuiz.getC());
                        intent.putExtra("E", sQuiz.getE());
                        intent.putExtra("D", sQuiz.getD());
                        intent.putExtra("num", sQuiz.getNum());
                        intent.putExtra("anum", sQuiz.getAnum());
                        intent.putExtra("bnum", sQuiz.getBnum());
                        intent.putExtra("cnum", sQuiz.getCnum());
                        intent.putExtra("dnum", sQuiz.getDnum());
                        intent.putExtra("enumb", sQuiz.getEnumb());
                        intent.putExtra("timer", sQuiz.getTimer());
                        intent.putExtra("available", sQuiz.getAvailable());
                        intent.putExtra("code", code);
                        intent.putExtra("type", type);

                        if (sQuiz.getImage() != null)
                            intent.putExtra("image", sQuiz.getImage());
                        progressDialog.dismiss();

                        if (sQuiz.getAvailable().equalsIgnoreCase("no" ) ) {
                            if (type.equalsIgnoreCase("student")) {
                                Toast.makeText(Code.this, "Time up", Toast.LENGTH_SHORT).show();
                            }  else {
                                startActivity(intent);
                            }
                        }  else {
                            startActivity(intent);
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Code.this, code + " not exist ", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Enter quiz code", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();



    }

//    public void dr() {
//        teamquiz = findViewById(R.id.teamquiz);
//        studentquiz = findViewById(R.id.studentquiz);
//        if (mAuth.getCurrentUser() != null) {
//            if (mAuth.getCurrentUser().getEmail().equalsIgnoreCase("mohamedismail@gmail.com")) {
//                studentquiz.setVisibility(View.VISIBLE);
//                teamquiz.setVisibility(View.GONE);
//            }
//        } else {
//            studentquiz.setVisibility(View.GONE);
//            teamquiz.setVisibility(View.GONE);
//        }
//        teamquiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Code.this, Admin.class);
//                intent.putExtra("type", "Team");
//                startActivity(intent);
//            }
//        });
//        studentquiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Code.this, Admin.class);
//                intent.putExtra("type", "Student");
//                startActivity(intent);
//            }
//        });
//    }


    public void getTQuiz() {
        sQuezRef = FirebaseDatabase.getInstance().getReference();
        final String code = codeE.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            sQuezRef.child("TQuiz").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Intent intent = new Intent(Code.this, StudentQuiz.class);
                        intent.putExtra("code", code);
                        intent.putExtra("type", type);
                        progressDialog.dismiss();
                        startActivity(intent);


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Code.this, code + " not exist ", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter quiz code", Toast.LENGTH_SHORT).show();
        }

    }
}

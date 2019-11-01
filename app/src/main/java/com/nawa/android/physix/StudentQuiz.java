package com.nawa.android.physix;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawa.android.physix.Prevelant.Prevelant;
import com.nawa.android.physix.model.SQuiz;
import com.nawa.android.physix.model.TQuiz;
import com.nawa.android.physix.model.team;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

public class StudentQuiz extends AppCompatActivity {
    //    A1, A2, A3, A4, A5, A6, A7, A8, A9, A10,
//    B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, C1, C2, C3, C4, C5, C6, C7, C8, C9, C10;
//    , ques1, ques2, ques3, ques4, ques5, ques6, ques7, ques8, ques9, ques10;
//   , a1, a2, a3, a4, a5, a6, a7,
//    a8, a9, a10, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10,
//    q1, q2, q3, q4, q5, q6, q7, q8, q9, q10;
    private RadioButton A, B, C, D, E;
    boolean processDone = false;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private CountDownTimer mCountDownTimer;
    private TextView ques, line;
    private String path;
    private Button conf, show, stop;
    private String a, b, c, d, e, question, code, link,
            q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, a1, a2, a3, a4, a5, a6, a7,
            a8, a9, a10, image;
    int num, anum, bnum, cnum, enumb , dnum ,score;
    String type, available;
    private EditText ques1, ques2, ques3, ques4, ques5, ques6, ques7, ques8, ques9, ques10;
    private DatabaseReference databaseReference;
    int ar;
    int br;
    int cr;

    //
    private TextView mTextViewCountDown;
    private long mTimeLeftInMillis;
//

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        available = getIntent().getStringExtra("available");
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SQuiz").child(code);
        setContentView(R.layout.activity_student_quiz);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        A = findViewById(R.id.A);
        B = findViewById(R.id.B);
        C = findViewById(R.id.C);
        D = findViewById(R.id.D);
        E = findViewById(R.id.E);
        ques = findViewById(R.id.quiz_head);
        conf = findViewById(R.id.Confirm);
        show = findViewById(R.id.show);
        stop = findViewById(R.id.stoporplay);
        a = getIntent().getStringExtra("A");
        b = getIntent().getStringExtra("B");
        c = getIntent().getStringExtra("C");
        d = getIntent().getStringExtra("D");
        e = getIntent().getStringExtra("E");
        imageView = findViewById(R.id.image_quiz);
        line = findViewById(R.id.line);

        mTimeLeftInMillis = getIntent().getLongExtra("timer", 0);
        question = getIntent().getStringExtra("ques");

        //جلب قيم الاختيارات من قاعدة البيانات
        num = getIntent().getIntExtra("num", 0);
        anum = getIntent().getIntExtra("anum", 0);
        bnum = getIntent().getIntExtra("bnum", 0);
        cnum = getIntent().getIntExtra("cnum", 0);
        enumb = getIntent().getIntExtra("enumb", 0);
        dnum = getIntent().getIntExtra("dnum", 0);


        if (getIntent().getStringExtra("image") != null) {
            imageView.setVisibility(View.VISIBLE);
            image = getIntent().getStringExtra("image");
            Picasso.get().load(image).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (mTimeLeftInMillis != 0) {
            startTimer();
        } else {
            mTextViewCountDown.setVisibility(View.GONE);
            line.setVisibility(View.GONE);

        }
        A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    C.setChecked(false);
                    D.setChecked(false);
                    E.setChecked(false);

                }
            }
        });
        B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    A.setChecked(false);
                    C.setChecked(false);
                    D.setChecked(false);
                    E.setChecked(false);
                }
            }
        });
        C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    A.setChecked(false);
                    D.setChecked(false);
                    E.setChecked(false);
                }
            }
        });
        E.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    A.setChecked(false);
                    D.setChecked(false);
                    C.setChecked(false);
                }
            }
        });
        D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    A.setChecked(false);
                    C.setChecked(false);
                    E.setChecked(false);
                }
            }
        });
        A.setText(a);
        B.setText(b);
        C.setText(c);
        D.setText(d);
        E.setText(e);
        ques.setText(question);


        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkChoice()) {
                    if (mTimeLeftInMillis != 0)
                        mCountDownTimer.cancel();
                    progressDialog.setTitle("Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    num = num + 1;
                    databaseReference.child("num").setValue(num).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child("anum").setValue(anum);
                                databaseReference.child("bnum").setValue(bnum);
                                databaseReference.child("cnum").setValue(cnum);
                                databaseReference.child("enumb").setValue(enumb);
                                databaseReference.child("dnum").setValue(dnum);

                                Toast.makeText(StudentQuiz.this, "Done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StudentQuiz.this, Code.class);
                                intent.putExtra("type", type);

                                A.setEnabled(false);
                                B.setEnabled(false);
                                C.setEnabled(false);
                                E.setEnabled(false);
                                D.setEnabled(false);
                                conf.setEnabled(false);
                                conf.setBackgroundColor(getResources().getColor(R.color.conf));
                                progressDialog.dismiss();
//
                            } else {
                                Toast.makeText(StudentQuiz.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });

                } else {
                    if (Paper.book().read(Prevelant.UserPhone).equals("123") && type.equalsIgnoreCase("professor")) {
                        show.setVisibility(View.VISIBLE);
                        stop.setVisibility(View.VISIBLE);
                        A.setEnabled(false);
                        B.setEnabled(false);
                        C.setEnabled(false);
                        E.setEnabled(false);
                        D.setEnabled(false);

                    }

                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            SQuiz sQuiz = dataSnapshot.getValue(SQuiz.class);
                            String ava = sQuiz.getAvailable();
                            if (ava.equalsIgnoreCase("yes")) {

                                stop.setText("Play");
                                databaseReference.child("available").setValue("no");
                                Toast.makeText(StudentQuiz.this, "Time", Toast.LENGTH_LONG);
                            } else {
                                stop.setText("Stop");
                                databaseReference.child("available").setValue("yes");
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                A.setEnabled(false);
                B.setEnabled(false);
                C.setEnabled(false);
                E.setEnabled(false);
                D.setEnabled(false);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            SQuiz sQuiz = dataSnapshot.getValue(SQuiz.class);
                            Intent intent = new Intent(StudentQuiz.this, BarChart.class);
                            intent.putExtra("A", sQuiz.getAnum());
                            intent.putExtra("B", sQuiz.getBnum());
                            intent.putExtra("C", sQuiz.getCnum());
                            intent.putExtra("E", sQuiz.getEnumb());
                            intent.putExtra("D", sQuiz.getDnum());
                            startActivity(intent);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                updateCountDownText();

            }

            @Override
            public void onFinish() {

                Toast.makeText(StudentQuiz.this, "time is up", Toast.LENGTH_SHORT).show();
                A.setEnabled(false);
                B.setEnabled(false);
                C.setEnabled(false);
                E.setEnabled(false);
                D.setEnabled(false);
            }
        }.start();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;


        if (type.equalsIgnoreCase("student")) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SQuiz sQuiz = dataSnapshot.getValue(SQuiz.class);
                        String ava = sQuiz.getAvailable();
                        if (ava.equalsIgnoreCase("no")) {
                            conf.setEnabled(false);
                            conf.setBackgroundColor(getResources().getColor(R.color.conf));
                            A.setEnabled(false);
                            B.setEnabled(false);
                            C.setEnabled(false);
                            E.setEnabled(false);
                            D.setEnabled(false);
                            mCountDownTimer.cancel();
//                        }else if (ava.equalsIgnoreCase("yes")){
//                            conf.setEnabled(true);
//                            conf.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                            A.setEnabled(true);
//                            B.setEnabled(true);
//                            C.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private boolean checkChoice() {

        if (A.isChecked()) {
            anum = anum + 1;
            return true;

        } else if (B.isChecked()) {
            bnum = bnum + 1;
            return true;

        } else if (C.isChecked()) {
            cnum = cnum + 1;
            return true;
        } else if (D.isChecked()) {
            dnum = dnum + 1;
            return true;
        } else if (E.isChecked()) {
            enumb = enumb + 1;
            return true;
        } else if (Paper.book().read(Prevelant.UserPhone).equals("123") && type.equalsIgnoreCase("professor")) {

            return false;
        } else {
            Toast.makeText(this, "Please choose an answer", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void getTeamQuizData() {
        setContentView(R.layout.team_quiz);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        ques1 = findViewById(R.id.a1);
        ques2 = findViewById(R.id.a2);
        ques3 = findViewById(R.id.a3);
        ques4 = findViewById(R.id.a4);
        ques5 = findViewById(R.id.a5);
        ques6 = findViewById(R.id.a6);
        ques7 = findViewById(R.id.a7);
        ques8 = findViewById(R.id.a8);
        ques9 = findViewById(R.id.a9);
        ques10 = findViewById(R.id.a10);
        line = findViewById(R.id.line);
        mTimeLeftInMillis = getIntent().getLongExtra("timer", 0);
        if (mTimeLeftInMillis != 0) {
            startTimer();
        } else {
            mTextViewCountDown.setVisibility(View.GONE);
            line.setVisibility(View.GONE);

        }
        Button linkbtn = findViewById(R.id.link_btn);
        linkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("TQuiz").child(code).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TQuiz tQuiz = dataSnapshot.getValue(TQuiz.class);
                        link = tQuiz.getLink();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                String url = link;
                if (link != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(getPackageManager()) != null) {
                        startActivity(i);
                    }

                }
            }
        });


        Button team = findViewById(R.id.team_confirm);
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


                if (TextUtils.isEmpty(q1) || TextUtils.isEmpty(q2) || TextUtils.isEmpty(q3) || TextUtils.isEmpty(q4) ||
                        TextUtils.isEmpty(q5) || TextUtils.isEmpty(q6) || TextUtils.isEmpty(q7) ||
                        TextUtils.isEmpty(q8) || TextUtils.isEmpty(q9) || TextUtils.isEmpty(q10)) {
                    Toast.makeText(StudentQuiz.this, "Please answer all questions", Toast.LENGTH_LONG).show();

                } else {
                    progressDialog.setTitle("Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    databaseReference.child("TQuiz").child(code).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            TQuiz tQuiz = dataSnapshot.getValue(TQuiz.class);

                            a1 = tQuiz.getQ1();
                            a2 = tQuiz.getQ2();
                            a3 = tQuiz.getQ3();
                            a4 = tQuiz.getQ4();
                            a5 = tQuiz.getQ5();
                            a6 = tQuiz.getQ6();
                            a7 = tQuiz.getQ7();
                            a8 = tQuiz.getQ8();
                            a9 = tQuiz.getQ9();
                            a10 = tQuiz.getQ10();
                            link = tQuiz.getLink();


                            scoreCal();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

    }

    private void scoreCal() {
        processDone = false;

        path = mAuth.getCurrentUser().getEmail().replace("@gmail.com", "");

        score = (int) getIntent().getLongExtra("score", 0);
        if (q1.equalsIgnoreCase(a1)) {
            score = score + 1;
        }
        if (q2.equalsIgnoreCase(a2)) {
            score = score + 1;
        }
        if (q3.equalsIgnoreCase(a3)) {
            score = score + 1;
        }
        if (q4.equalsIgnoreCase(a4)) {
            score = score + 1;
        }
        if (q5.equalsIgnoreCase(a5)) {
            score = score + 1;
        }
        if (q6.equalsIgnoreCase(a6)) {
            score = score + 1;
        }
        if (q7.equalsIgnoreCase(a7)) {
            score++;
        }
        if (q8.equalsIgnoreCase(a8)) {
            score++;
        }
        if (q9.equalsIgnoreCase(a9)) {
            score++;
        }
        if (q10.equalsIgnoreCase(a10)) {
            score++;
        }

        path = mAuth.getCurrentUser().getEmail().replace("@gmail.com", "");


        databaseReference.child("Scores").child(path).setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(StudentQuiz.this, "Your current Score is " + score, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentQuiz.this, Code.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();

                } else {
                    Toast.makeText(StudentQuiz.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDownTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCountDownTimer.cancel();
    }
    //    public void getTeamQuizData() {
//        setContentView(R.layout.team_quiz);
//
//        ques1 = findViewById(R.id.one_quiz_head);
//        ques2 = findViewById(R.id.two_quiz_head);
//        ques3 = findViewById(R.id.three_quiz_head);
//        ques4 = findViewById(R.id.four_quiz_head);
//        ques5 = findViewById(R.id.five_quiz_head);
//        ques6 = findViewById(R.id.six_quiz_head);
//        ques7 = findViewById(R.id.seven_quiz_head);
//        ques8 = findViewById(R.id.eight_quiz_head);
//        ques9 = findViewById(R.id.nine_quiz_head);
//        ques10 = findViewById(R.id.ten_quiz_head);
//
//        A1 = findViewById(R.id.A);
//        A2 = findViewById(R.id.two_A);
//        A3 = findViewById(R.id.three_A);
//        A4 = findViewById(R.id.four_A);
//        A5 = findViewById(R.id.five_A);
//        A6 = findViewById(R.id.six_A);
//        A7 = findViewById(R.id.seven_A);
//        A8 = findViewById(R.id.eight_A);
//        A9 = findViewById(R.id.nine_A);
//        A10 = findViewById(R.id.ten_A);
//
//        B1 = findViewById(R.id.B);
//        B2 = findViewById(R.id.two_B);
//        B3 = findViewById(R.id.three_B);
//        B4 = findViewById(R.id.four_B);
//        B5 = findViewById(R.id.five_B);
//        B6 = findViewById(R.id.six_B);
//        B7 = findViewById(R.id.seven_B);
//        B8 = findViewById(R.id.eight_B);
//        B9 = findViewById(R.id.nine_B);
//        B10 = findViewById(R.id.ten_B);
//
//        C1 = findViewById(R.id.C);
//        C2 = findViewById(R.id.two_C);
//        C3 = findViewById(R.id.three_C);
//        C4 = findViewById(R.id.four_C);
//        C5 = findViewById(R.id.five_C);
//        C6 = findViewById(R.id.six_C);
//        C7 = findViewById(R.id.seven_C);
//        C8 = findViewById(R.id.eight_C);
//        C9 = findViewById(R.id.nine_C);
//        C10 = findViewById(R.id.ten_C);
//
//
//
//        sQuezRef = FirebaseDatabase.getInstance().getReference().child("TQuiz");
//        sQuezRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                TQuiz tQuiz = dataSnapshot.getValue(TQuiz.class);
//
//                a1 = tQuiz.getA1();
//                a2 = tQuiz.getA2();
//                a3 = tQuiz.getA3();
//                a4 = tQuiz.getA4();
//                a5 = tQuiz.getA5();
//                a6 = tQuiz.getA6();
//                a7 = tQuiz.getA7();
//                a8 = tQuiz.getA8();
//                a9 = tQuiz.getA9();
//                a10 = tQuiz.getA10();
//
//
//                b1 = tQuiz.getB1();
//                b2 = tQuiz.getB2();
//                b3 = tQuiz.getB3();
//                b4 = tQuiz.getB4();
//                b5 = tQuiz.getB5();
//                b6 = tQuiz.getB6();
//                b7 = tQuiz.getB7();
//                b8 = tQuiz.getB8();
//                b9 = tQuiz.getB9();
//                b10 = tQuiz.getB10();
//
//
//                c1 = tQuiz.getC1();
//                c2 = tQuiz.getC2();
//                c3 = tQuiz.getC3();
//                c4 = tQuiz.getC4();
//                c5 = tQuiz.getC5();
//                c6 = tQuiz.getC6();
//                c7 = tQuiz.getC7();
//                c8 = tQuiz.getC8();
//                c9 = tQuiz.getC9();
//                c10 = tQuiz.getC10();
//
//
//                q1 = tQuiz.getQ1();
//                q2 = tQuiz.getQ2();
//                q3 = tQuiz.getQ3();
//                q4 = tQuiz.getQ4();
//                q5 = tQuiz.getQ5();
//                q6 = tQuiz.getQ6();
//                q7 = tQuiz.getQ7();
//                q8 = tQuiz.getQ8();
//                q9 = tQuiz.getQ9();
//                q10 = tQuiz.getQ10();
//
//                ques1.setText(q1);
//                ques2.setText(q2);
//                ques3.setText(q3);
//                ques4.setText(q4);
//                ques5.setText(q5);
//                ques6.setText(q6);
//                ques7.setText(q7);
//                ques8.setText(q8);
//                ques9.setText(q9);
//                ques10.setText(q10);
//
//                A1.setText(a1);
//                A2.setText(a2);
//                A3.setText(a3);
//                A4.setText(a4);
//                A5.setText(a5);
//                A6.setText(a6);
//                A7.setText(a7);
//                A8.setText(a8);
//                A9.setText(a9);
//                A10.setText(a10);
//
//                B1.setText(b1);
//                B2.setText(b2);
//                B3.setText(b3);
//                B4.setText(b4);
//                B5.setText(b5);
//                B6.setText(b6);
//                B7.setText(b7);
//                B8.setText(b8);
//                B9.setText(b9);
//                B10.setText(b10);
//
//                C1.setText(c1);
//                C2.setText(c2);
//                C3.setText(c3);
//                C4.setText(c4);
//                C5.setText(c5);
//                C6.setText(c6);
//                C7.setText(c7);
//                C8.setText(c8);
//                C9.setText(c9);
//                C10.setText(c10);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
}

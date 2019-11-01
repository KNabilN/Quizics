package com.nawa.android.physix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawa.android.physix.Prevelant.Prevelant;
import com.nawa.android.physix.model.Users;
import com.nawa.android.physix.model.team;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button individul, professors , team;
    private Intent intent;
    private DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        professors = findViewById(R.id.prof);
        individul = findViewById(R.id.individual);
        team = findViewById(R.id.team);
        Paper.init(this);
        rootRef = FirebaseDatabase.getInstance().getReference();

        professors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = Paper.book().read(Prevelant.UserPassword);
                String phone = Paper.book().read(Prevelant.UserPhone);


                if (pass != "" && phone != "") {


                    if (!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(phone)) {
                        if (phone.equalsIgnoreCase("123")) {
                            Intent intent = new Intent(MainActivity.this, Code.class);
                            intent.putExtra("type", "Professor");
                            startActivity(intent);
                            finish();
                        } else {
                            Access(pass, phone);

                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.putExtra("type", "Professor");
                        startActivity(intent);
                        finish();

                    }

                }else {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.putExtra("type", "Professor");
                        startActivity(intent);
                        finish();

                    }
            }
        });
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Code.class);
                intent.putExtra("type", "Team");
                startActivity(intent);
                finish();

            }
        });


        individul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Code.class);
                intent.putExtra("type", "Student");
                startActivity(intent);
                finish();
            }
        });
    }

    private void Access(final String password, final String phone) {
        rootRef.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()) {

                    rootRef.child("Student").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users user = dataSnapshot.getValue(Users.class);
                            if (user.getPassword().equalsIgnoreCase(password)) {
                                Paper.book().write(Prevelant.currentUser, user.getName());
                                Intent intent = new Intent(MainActivity.this, Code.class);
                                intent.putExtra("type", "Student");
                                intent.putExtra("user", phone);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}



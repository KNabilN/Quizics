package com.nawa.android.physix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nawa.android.physix.R;

import java.util.HashMap;


public class SignUp extends AppCompatActivity {

    EditText leaderp, uname,unum,upass,urpass , tname , tpass , trepass , teamLeader , teamFM , teamSM , teamThM , teamFoM;
    String  leaderphone,unames , upasss, urpasss , phone ,teamname , teampass , teamrepass,leader , fm , sm , tm , fom;
    Button addM;

    FirebaseAuth mAuth ;
    DatabaseReference rootRef;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("student")) {
            setContentView(R.layout.activity_sign_up);


            uname = findViewById(R.id.uname);
            unum = findViewById(R.id.unum);
            upass = findViewById(R.id.upass);
            urpass = findViewById(R.id.urepass);
            mAuth = FirebaseAuth.getInstance();
            rootRef = FirebaseDatabase.getInstance().getReference();
            progressDialog = new ProgressDialog(this);

            Button btn = findViewById(R.id.signupbtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAccount();
                }
            });
        }else if (type.equalsIgnoreCase("team")){
            setContentView(R.layout.team_signup);
            tname = findViewById(R.id.teamname);
            tpass = findViewById(R.id.teampass);
            trepass = findViewById(R.id.teamrepass);
            teamFM = findViewById(R.id.fmname);
            teamSM = findViewById(R.id.smname);
            teamLeader = findViewById(R.id.leadername);
            teamThM=findViewById(R.id.thmname);
            leaderp = findViewById(R.id.leaderphone);
            teamFoM = findViewById(R.id.fomname);
            Button btn = findViewById(R.id.signupbtn);
            addM = findViewById(R.id.addmemberbtn);
            progressDialog = new ProgressDialog(this);
            rootRef = FirebaseDatabase.getInstance().getReference();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTeam();
                }
            });
            addM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addmember();
                }
            });
        }
    }

    private void addmember() {
        LinearLayout third = findViewById(R.id.third_member);
        LinearLayout forth = findViewById(R.id.fourth_member);

        if (third.getVisibility()== View.GONE){
            third.setVisibility(View.VISIBLE);
        }else if (forth.getVisibility()== View.GONE){
            forth.setVisibility(View.VISIBLE);
            addM.setVisibility(View.GONE);
        }
    }

    private void createTeam() {

        teamname = tname.getText().toString();
        teampass = tpass.getText().toString();
        teamrepass = trepass.getText().toString();
        leader = teamLeader.getText().toString();
        mAuth=FirebaseAuth.getInstance();
        fm = teamFM.getText().toString();
        sm = teamSM.getText().toString();
        tm = teamThM.getText().toString();
        fom = teamFoM.getText().toString();
        leaderphone = leaderp.getText().toString();
        if (TextUtils.isEmpty(teamname) || TextUtils.isEmpty(teampass) || TextUtils.isEmpty(teamrepass) || TextUtils.isEmpty(leader)|| TextUtils.isEmpty(fm) || TextUtils.isEmpty(sm) || TextUtils.isEmpty(leaderphone)){
            Toast.makeText(this, "تحقق من البيانات التي تم إدخالها", Toast.LENGTH_LONG).show();
        } else if(!teampass.equals(teamrepass)) {
            Toast.makeText(this, "الرقم السري غير متوافق", Toast.LENGTH_LONG).show();
        } else if (leaderphone.length() != 11 || !leaderphone.startsWith("01")) {
            Toast.makeText(this, "رقم الهاتف غير صالح", Toast.LENGTH_LONG).show();

        }else{
            progressDialog.setTitle("إنشاء حساب");
            progressDialog.setMessage("جاري التحقق من البيانات");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            signupTeam();
        }
    }

    private void signupTeam() {
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Team").child(teamname).exists())) {

                    final HashMap<String, Object> teamLeader = new HashMap<>();
                    teamLeader.put("name", leader);
                    teamLeader.put("phone", leaderphone);

                    mAuth.createUserWithEmailAndPassword(((teamname.replace(" ", "")) + "@gmail.com"), teampass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                rootRef.child("Team").child((teamname.replace(" ", ""))).child("Leader").updateChildren(teamLeader).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            rootRef.child("Team").child((teamname.replace(" ", ""))).child("team").setValue(teamname);
                                            rootRef.child("Team").child((teamname.replace(" ", ""))).child("answered").setValue(false);
                                            rootRef.child("Team").child((teamname.replace(" ", ""))).child("FM").setValue(fm);
                                            rootRef.child("Team").child((teamname.replace(" ", ""))).child("SM").setValue(sm);
                                            if (!TextUtils.isEmpty(tm))
                                            rootRef.child("Team").child((teamname.replace(" ", ""))).child("TM").setValue(tm);

                                            if (TextUtils.isEmpty(fom))
                                            rootRef.child("Team").child((teamname.replace(" ", ""))).child("FoM").setValue(fom);

                                            Toast.makeText(SignUp.this, "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(SignUp.this, Login.class);
                                            intent.putExtra("type", "team");
                                            startActivity(intent);
                                            finish();
                                        } else {

                                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

                            } else {

                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignUp.this, teamname + " مسجل من قبل ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "يرجى تسجيل الدخول", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUp.this, Login.class);
                    intent.putExtra("type", "team");
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void createAccount() {

        unames = uname.getText().toString().trim();

        phone = unum.getText().toString();

        upasss = upass.getText().toString().trim();
        urpasss = urpass.getText().toString().trim();

        if (TextUtils.isEmpty(unames) || TextUtils.isEmpty(upasss) || TextUtils.isEmpty(urpasss) || TextUtils.isEmpty(String.valueOf(phone))){
            Toast.makeText(this, "تحقق من البيانات التي تم إدخالها", Toast.LENGTH_LONG).show();
        } else if (phone.length() != 11 || !phone.startsWith("01")){
            Toast.makeText(this, "رقم الهاتف غير صالح", Toast.LENGTH_LONG).show();
        } else if(!upasss.equals(urpasss)){
            Toast.makeText(this, "الرقم السري غير متوافق", Toast.LENGTH_LONG).show();
        }else{
            progressDialog.setTitle("إنشاء حساب");
            progressDialog.setMessage("جاري التحقق من البيانات");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            ValidatehoneNumber();
        }
    }

    private void ValidatehoneNumber() {
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Student").child(phone).exists())) {

                    final HashMap<String, Object> usersMap = new HashMap<>();
                    usersMap.put("name", unames);
                    usersMap.put("phone", phone);
                    usersMap.put("password", upasss);

                    rootRef.child(getIntent().getStringExtra("type")).child(phone).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUp.this, Login.class);
                                intent.putExtra("type", "student");
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                    });


                }  else {
                    Toast.makeText(SignUp.this, phone + " مسجل من قبل ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "يرجى تسجيل الدخول", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUp.this, Login.class);
                    intent.putExtra("type", "Student");
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

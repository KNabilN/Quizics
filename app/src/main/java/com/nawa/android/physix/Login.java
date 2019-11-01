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
import android.widget.TextView;
import android.widget.Toast;

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
import com.nawa.android.physix.Prevelant.Prevelant;
import com.nawa.android.physix.model.Users;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    private EditText Ename, Epassword;
    private Button signin;
    private TextView signup;
    private String name, password;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    String type;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        type = getIntent().getStringExtra("type");

        rootRef = FirebaseDatabase.getInstance().getReference();
        Paper.init(this);

        Ename = findViewById(R.id.usereditli);
        Epassword = findViewById(R.id.passeditli);
        signin = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        if (type.equalsIgnoreCase("Team")){
            Ename.setHint("Team Name");
        }
        progressDialog = new ProgressDialog(this);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , SignUp.class);
                intent.putExtra("type", type);
                startActivity(intent);
                finish();
            }
        });

    }

    private void login() {
        name = Ename.getText().toString().trim();
        password = Epassword.getText().toString();

        progressDialog.setTitle("تسجيل الدخول");
        progressDialog.setMessage("جاري التحقق من البيانات");
        progressDialog.setCanceledOnTouchOutside(false);

        if (name.equalsIgnoreCase("123") && password.equalsIgnoreCase("admin")) {
            mAuth.signInWithEmailAndPassword("mohamedismail@gmail.com", "admin1").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {

                        Intent intent = new Intent(Login.this, Code.class);
                        progressDialog.show();
                        intent.putExtra("type", type);
                        Paper.book().destroy();
                        Paper.book().write(Prevelant.UserPhone, "123");
                        Paper.book().write(Prevelant.UserPassword, "admin");
                        intent.putExtra("user", name);
                        progressDialog.dismiss();
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(Login.this, "Problem happened", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        } else {

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your data", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();

//  check if exist and save data
                rootRef.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(name).exists()) {

                            rootRef.child("Student").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Users user = dataSnapshot.getValue(Users.class);
                                    if (user.getPassword().equalsIgnoreCase(password)) {
                                        Paper.book().destroy();
                                        Paper.book().write(Prevelant.UserPassword, password);
                                        Paper.book().write(Prevelant.UserPhone,name );

                                        Intent intent = new Intent(Login.this, Code.class);
                                        intent.putExtra("type", "Student");
                                        intent.putExtra("user", name);

                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Toast.makeText(Login.this, "This Phone is not registered ... please sign up", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, SignUp.class);
                            intent.putExtra("type", "Student");

                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        }
    }


}

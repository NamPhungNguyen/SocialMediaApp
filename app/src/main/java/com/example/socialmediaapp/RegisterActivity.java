package com.example.socialmediaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailEt, mPasswordEt;
    Button mRegister;
    ProgressDialog progressDialog;

    //declare an instance of FireAuth
    FirebaseAuth mAuth;
    TextView mHaveAccountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // actionBar and its title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Create Account"); // Thiết lập tiêu đề cho ActionBar
        }

        // enable back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);// Bật nút back trên ActionBar
        }



        // init
        mEmailEt = findViewById(R.id.emailET);
        mPasswordEt = findViewById(R.id.passwordET);
        mRegister = findViewById(R.id.btn_register);
        mHaveAccountTv = findViewById(R.id.have_accountTv);

        //IN the onCreate() method. initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        // handle register btn click
        mRegister.setOnClickListener(v->{
            // input email, password
            String email = mEmailEt.getText().toString().trim();
            String password = mPasswordEt.getText().toString().trim();

            //validate
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                //set error and focus to email edittext
                mEmailEt.setError("Invalid Email");
                mEmailEt.setFocusable(true);
            }else if (password.length() < 6){
                mPasswordEt.setError("Password length at least 6 characters");
                mPasswordEt.setFocusable(true);
            }else {
                registerUser(email, password);
            }
        });

        //handle login textView click listener
        mHaveAccountTv.setOnClickListener(v->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser(String email, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // sign in success, dismiss dialog and start register activity
                            progressDialog.dismiss();


                            FirebaseUser user = mAuth.getCurrentUser();
                            //get user email and uid from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //when user is registered store user info in firebase realtime database too
                            // using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            // put info in hasmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", ""); //will add later (e.g edit profile)
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            //put data within hashmap in database
                            reference.child(uid).setValue(hashMap);



                            Toast.makeText(RegisterActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go previous activity
        return super.onSupportNavigateUp();
    }
}
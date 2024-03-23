package com.example.socialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button mRegister_btn, mLogin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRegister_btn = findViewById(R.id.register_btn);
        mLogin_btn = findViewById(R.id.login_btn);

        mRegister_btn.setOnClickListener(v->{
            // start RegisterActivity
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        mLogin_btn.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}
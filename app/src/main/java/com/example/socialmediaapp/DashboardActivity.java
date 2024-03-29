package com.example.socialmediaapp;

import static com.example.socialmediaapp.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        //actionbar and its title
        actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Profile");

        firebaseAuth = FirebaseAuth.getInstance();

        //bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);


        //home fragment transaction (default, on star)
        actionBar.setTitle("Home"); // change actionbar
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // handle item clicks
            if (menuItem.getItemId() == R.id.nav_home) {
                //home fragment transaction
                actionBar.setTitle("Home"); // change actionbar
                HomeFragment fragment1 = new HomeFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.content, fragment1, "");
                ft1.commit();
                return true;
            } else if (menuItem.getItemId() == R.id.nav_profile) {
                actionBar.setTitle("Profile"); // change actionbar
                //profile fragment transaction
                ProfileFragment fragment2 = new ProfileFragment();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.content, fragment2, "");
                ft2.commit();
                return true;
            } else if (menuItem.getItemId() == R.id.nav_users) {
                actionBar.setTitle("Users"); // change actionbar
                //users fragment transaction
                UsersFragment fragment3 = new UsersFragment();
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.content, fragment3, "");
                ft3.commit();
                return true;
            }
            return false;
        }
    };

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            // user is signed in stay here
            //set email of logged in user
        }else{
            //user not signed in, go to main activity
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        // check on start of app
        checkUserStatus();
        super.onStart();
    }

//    inflate options menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }
}
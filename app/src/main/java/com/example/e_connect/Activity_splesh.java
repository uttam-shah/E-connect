package com.example.e_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.e_connect.login.Activity_login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_splesh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get the SharedPreferences
                SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String storedUsername = pref.getString("username", "");
                String storedPassword = pref.getString("password", "");

                if (!storedUsername.isEmpty() && !storedPassword.isEmpty()) {
                     //User data exists in SharedPreferences, check if it's correct
                    checkUsernamePassword(storedUsername, storedPassword);
                } else {
                    // No user data in SharedPreferences, go to the login screen (MainActivity)
                    Intent iNext = new Intent(Activity_splesh.this, Activity_login.class);
                    startActivity(iNext);
                    finish();
                }
            }
        }, 1000);
    }

    private void checkUsernamePassword(String storedUsername, String storedPassword) {
        // Retrieve the user data from your Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        usersRef.child(storedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username exists, now check the password
                    String dbPassword = dataSnapshot.child("pass").getValue(String.class);

                    if (storedPassword.equals(dbPassword)) {
                        // Password is correct, navigate to Dashboard
                        Intent intent = new Intent(Activity_splesh.this, Activity_dashboard.class);
                        intent.putExtra("username", storedUsername);
                        startActivity(intent);
                        finish();
                    } else {
                        // Password is incorrect, redirect to login
                        Toast.makeText(Activity_splesh.this, "Stored password is incorrect", Toast.LENGTH_SHORT).show();
                        Intent iNext = new Intent(Activity_splesh.this, Activity_login.class);
                        startActivity(iNext);
                        finish();
                    }
                } else {
                    // User does not exist, redirect to login
                    Toast.makeText(Activity_splesh.this, "Stored username does not exist", Toast.LENGTH_SHORT).show();
                    Intent iNext = new Intent(Activity_splesh.this, Activity_login.class);
                    startActivity(iNext);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
                Toast.makeText(Activity_splesh.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
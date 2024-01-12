package com.example.e_connect.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_connect.Activity_dashboard;
import com.example.e_connect.R;
import com.example.e_connect.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_login extends AppCompatActivity {

    private EditText loginPhone, loginPassword;
    private Button loginLogBtn, loginCrtAccBtn;
    private TextView loginForgorPass;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Finding All the Id's
        loginPhone = findViewById(R.id.loginphone);
        loginPassword = findViewById(R.id.loginPass);
        loginLogBtn = findViewById(R.id.loginLogBtn);
        loginCrtAccBtn = findViewById(R.id.loginCrtAccBtn);

        // Get a reference to your Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Specify the path to the "Registered Users" data
        usersRef = databaseReference.child("Registered Users");

        loginCrtAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_login.this, Activity_register.class);
                startActivity(intent);
            }
        });

        loginLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginInput = loginPhone.getText().toString().trim();
                String passwordInput = loginPassword.getText().toString().trim();
                Toast.makeText(Activity_login.this, loginInput, Toast.LENGTH_SHORT).show();

                if (TextUtils.isEmpty(loginInput)) {
                    loginPhone.setError("Enter your Username");
                    loginPhone.requestFocus();
                } else if (TextUtils.isEmpty(passwordInput)) {
                    loginPassword.setError("Enter your Password");
                    loginPassword.requestFocus();
                } else {
                    // Retrieve the entered username from the user
                    String enteredUsername = loginInput.trim();

                    // Check if the entered username exists in the database
                    usersRef.child(enteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Username exists, now check the password
                                String storedPassword = dataSnapshot.child("pass").getValue(String.class);

                                // Inside the onDataChange method after "Login Successful" Toast message
                                if (passwordInput.equals(storedPassword)) {
                                    String storedPhone = dataSnapshot.child("phone").getValue(String.class);

                                    // Store data in SharedPreferences
                                    SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("username", enteredUsername);
                                    editor.putString("password", passwordInput);
                                    editor.apply();

                                    Intent intent = new Intent(Activity_login.this, Activity_dashboard.class);
                                    intent.putExtra("username", enteredUsername); // Pass the username
                                    intent.putExtra("phone", storedPhone);
                                    startActivity(intent);
                                    finish(); // Close the current login activity to prevent going back with the back button
                                } else {
                                    Toast.makeText(Activity_login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Activity_login.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors here
                            Toast.makeText(Activity_login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




// Check the user's authentication state in your app's entry point
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // User is already authenticated, navigate to the main part of the app
            // You can also fetch user information from Firebase and display it
            // For example, you can show the user's name or profile picture
        } else {
            // User is not authenticated, navigate to the login or registration screen
        }
    }
}
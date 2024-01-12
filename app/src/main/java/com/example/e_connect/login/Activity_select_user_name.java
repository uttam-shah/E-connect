package com.example.e_connect.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_connect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_select_user_name extends AppCompatActivity {
    private EditText regUserName;
    private Button regNext2;
    private TextView logHaveAcc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_name);

        //finding Id's
        regUserName = findViewById(R.id.regUserName);
        regNext2 = findViewById(R.id.regNext2);
        logHaveAcc = findViewById(R.id.logHaveAcc);

        logHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_select_user_name.this,Activity_login.class);
                startActivity(intent);
            }
        });

        regNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String userName = regUserName.getText().toString().trim();
                // Retrieve the entered username from the user during registration
                String enteredUsername = regUserName.getText().toString().trim(); // Replace with actual user input

                if (TextUtils.isEmpty(enteredUsername)){
                    regUserName.setError("User name is required");
                    regUserName.requestFocus();
                }
                else {

                    // Get a reference to your Firebase Realtime Database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

// Specify the path to the "Registered Users" data
                    DatabaseReference usersRef = databaseReference.child("Registered Users");



// Check if the entered username exists in the database
                    usersRef.child(enteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                regUserName.setError("User Name is all ready taken please enter different user name");
                                regUserName.requestFocus();
                                // Username already exists, show an error message
                                // Username is not unique
                            } else {

                                Intent fromAct = getIntent();
                                String name = fromAct.getStringExtra("name");
                                String dob = fromAct.getStringExtra("dob");
                                String pass = fromAct.getStringExtra("pass");

                                Intent intent = new Intent(Activity_select_user_name.this, Activity_enter_phone_no.class);
                                intent.putExtra("name",name);
                                intent.putExtra("dob",dob);
                                intent.putExtra("pass",pass);
                                intent.putExtra("userName",enteredUsername);
                                startActivity(intent);
                                // Username is unique, proceed with registration
                                // Save user data to the database
                                // You can add the user data to the database here
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Activity_select_user_name.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            // Handle any errors here
                        }
                    });

                }
            }
        });
    }
}
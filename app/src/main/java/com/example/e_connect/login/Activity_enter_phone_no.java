package com.example.e_connect.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_connect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Activity_enter_phone_no extends AppCompatActivity {
    private EditText regPhone;
    private Button regNext3;
    private TextView logHaveAcc;
    private CountryCodePicker ccp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_no);

        regPhone = findViewById(R.id.regPhone);
        regNext3 = findViewById(R.id.regNext3);
        logHaveAcc = findViewById(R.id.logHaveAcc);
        ccp = findViewById(R.id.ccp);
        mAuth = FirebaseAuth.getInstance();


        logHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_enter_phone_no.this, Activity_login.class);
                startActivity(intent);
            }
        });

        regNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String phone = regPhone.getText().toString().trim();
                // Retrieve the entered phone number from the user during registration
                String enteredPhoneNumber =  regPhone.getText().toString().trim(); // Replace with actual user input
                String cc = ccp.getDefaultCountryCodeWithPlus().toString();

                Toast.makeText(Activity_enter_phone_no.this, cc+enteredPhoneNumber, Toast.LENGTH_SHORT).show();

                if (enteredPhoneNumber.isEmpty()) {
                    regPhone.setError("Phone number is required!");
                    regPhone.requestFocus();
                } else if (!Patterns.PHONE.matcher(enteredPhoneNumber).matches()) {
                    regPhone.setError("Invalid phone number!");
                    regPhone.requestFocus();
                } else {

                    // Get a reference to your Firebase Realtime Database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

// Specify the path to the "Registered Users" data
                    DatabaseReference usersRef = databaseReference.child("Registered Users");



// Check if the entered phone number exists in the database
                    Query phoneQuery = usersRef.orderByChild("phone").equalTo("cc"+enteredPhoneNumber);
                    phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Phone number already exists, show an error message
                                // Phone number is not unique
                                regPhone.setError("" +
                                        "please use a different phone Numbe this number is all ready used with a User Account!");
                                regPhone.requestFocus();
                            } else {
                                // Phone number is unique, proceed with registration
                                // Save user data to the database
                                // You can add the user data to the database here

                                // Proceed with registration
                                Intent fromAct = getIntent();
                                String name = fromAct.getStringExtra("name");
                                String dob = fromAct.getStringExtra("dob");
                                String pass = fromAct.getStringExtra("pass");
                                String userName = fromAct.getStringExtra("userName");

                                Toast.makeText(Activity_enter_phone_no.this, cc+enteredPhoneNumber, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Activity_enter_phone_no.this,Activity_enter_opt.class);
                                intent.putExtra("name",name);
                                intent.putExtra("dob",dob);
                                intent.putExtra("pass",pass);
                                intent.putExtra("userName",userName);
                                intent.putExtra("phone",cc+enteredPhoneNumber);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors here
                            Toast.makeText(Activity_enter_phone_no.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });




                }
            }
        });
    }
}
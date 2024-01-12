package com.example.e_connect.login;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Activity_enter_opt extends AppCompatActivity {
    EditText otp;
    Button btnVerify;
    TextView showPhone;
    String otpId;
    FirebaseAuth mAuth;

    String name, dob, pass, userName, phone="", gender="", bio="", profilePhotoUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_opt);
        btnVerify = findViewById(R.id.btnVerify);
        otp = findViewById(R.id.otp);
        showPhone = findViewById(R.id.showPhone);
        mAuth = FirebaseAuth.getInstance();

        //get teh users information
        Intent fromAct = getIntent();
        name = fromAct.getStringExtra("name");
        dob = fromAct.getStringExtra("dob");
        pass = fromAct.getStringExtra("pass");
        userName = fromAct.getStringExtra("userName");
        phone = fromAct.getStringExtra("phone");

        showPhone.setText(phone);
        initiateotp(phone);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpNo = otp.getText().toString().trim();
                if (TextUtils.isEmpty(otpNo)) {
                    Toast.makeText(Activity_enter_opt.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                } else if (otpNo.length()!=6) {
                    Toast.makeText(Activity_enter_opt.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, otpNo);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });
    }

    private void initiateotp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Toast.makeText(Activity_enter_opt.this, "on Verifi complete", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Activity_enter_opt.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Toast.makeText(Activity_enter_opt.this, "on code sent", Toast.LENGTH_SHORT).show();
            otpId = verificationId;
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // verification successful
                            Toast.makeText(Activity_enter_opt.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                            // Register User
                            registerUser( dob, name, pass, phone, userName);
                        } else {
                            Toast.makeText(Activity_enter_opt.this, "Verification failed", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Activity_enter_opt.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // Register the user using their information
    private void registerUser(String dob, String name, String pass, String phone, String userName) {
        // Create a UserInfo object with user data
        UserInfo userInfo = new UserInfo( dob, name, pass, phone, userName);

        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node = db.getReference("Registered Users");

        // Set user data in the database
        node.child(userName).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Registration successful
                    Toast.makeText(Activity_enter_opt.this, "User registration successful", Toast.LENGTH_SHORT).show();

                    // Store data in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", userName);
                    editor.putString("password", pass);
                    editor.apply();

                    Intent intent = new Intent(Activity_enter_opt.this, Activity_dashboard.class);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                    finish();
                    // You can proceed to the next activity or handle further actions here
                } else {
                    // Registration failed
                    Toast.makeText(Activity_enter_opt.this, "User registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
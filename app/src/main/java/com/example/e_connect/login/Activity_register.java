package com.example.e_connect.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e_connect.R;

public class Activity_register extends AppCompatActivity {
    private EditText regName, regDOB, regPass, regConfPass;
    private Button regNext;
    private TextView logHaveAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //finding all the id's
        regName = findViewById(R.id.regName);
        regDOB = findViewById(R.id.regDOB);
        regPass = findViewById(R.id.regPass);
        regConfPass = findViewById(R.id.regConfPass);
        regNext = findViewById(R.id.regNext);
        logHaveAcc = findViewById(R.id.logHaveAcc);

        logHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_register.this, Activity_login.class);
                startActivity(intent);
            }
        });

        regNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtaining the Users Data
                String name = regName.getText().toString();
                String dob = regDOB.getText().toString();
                String pass = regPass.getText().toString();
                String confPass = regConfPass.getText().toString();

                if(TextUtils.isEmpty(name)){
                    regName.setError("Name is required!");
                    regName.requestFocus();
                } else if (TextUtils.isEmpty(dob)) {
                    regDOB.setError("Date Of Birth is Required!");
                    regDOB.requestFocus();
                } else if (pass.length() < 6) {
                    regPass.setError("Your password is too week!");
                    regPass.requestFocus();
                } else if (!confPass.equals(pass)) {
                    regConfPass.setError("Wrong password!");
                    regConfPass.requestFocus();
                }
                else {
                    Intent intent = new Intent(Activity_register.this, Activity_select_user_name.class);
                    intent.putExtra("name",name);
                    intent.putExtra("dob",dob);
                    intent.putExtra("pass",pass);
                    startActivity(intent);
                }
            }
        });
    }
}
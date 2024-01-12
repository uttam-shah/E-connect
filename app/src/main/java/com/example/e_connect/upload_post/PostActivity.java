package com.example.e_connect.upload_post;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.e_connect.R;
import com.google.android.material.tabs.TabLayout;

public class PostActivity extends AppCompatActivity {
    TabLayout tab;
    ViewPager viewPager;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Corrected lines to assign the views
        tab = findViewById(R.id.postTab);
        viewPager = findViewById(R.id.postViewPager);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");

        // Create a Bundle and put the data you want to share
        Bundle bundle = new Bundle();
        bundle.putString("user_name", user_name);

        ViewPagerPostAdapter adapter = new ViewPagerPostAdapter(getSupportFragmentManager(), bundle);

        // Set up the ViewPager with the adapter
        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);
    }
}

package com.example.e_connect.ui.queries;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_connect.R;
import com.example.e_connect.ui.home.home_Adapter;
import com.example.e_connect.ui.my_profile.feed_post_Model;
import com.example.e_connect.upload_post.QueryPostModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class Fragment_queries extends Fragment {

    RecyclerView recyclerView;
    String user_id;
    queries_Adapter mainAdapter;
    public Fragment_queries() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queries, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.queries_recyclerview);

        // set the username
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            user_id = intent.getStringExtra("username");
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<QueryPostModel> options =
                new FirebaseRecyclerOptions.Builder<QueryPostModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users").child(user_id).child("post").child("queri_post"), QueryPostModel.class)
                        .build();

        mainAdapter = new queries_Adapter(options);
        recyclerView.setAdapter(mainAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
    }
}
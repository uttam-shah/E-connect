package com.example.e_connect.ui.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_connect.R;
import com.example.e_connect.ui.queries.queries_Adapter;
import com.example.e_connect.upload_post.NotesPostModel;
import com.example.e_connect.upload_post.QueryPostModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class Fragment_notes extends Fragment {

   RecyclerView recyclerView;
   notes_Adapter mainAdapter;
   String user_id;

    public Fragment_notes() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // set the username
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            user_id = intent.getStringExtra("username");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.notes_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<NotesPostModel> options =
                new FirebaseRecyclerOptions.Builder<NotesPostModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users").child(user_id).child("post").child("notes_post"), NotesPostModel.class)
                        .build();

        mainAdapter = new notes_Adapter(options);
        recyclerView.setAdapter(mainAdapter);


        return  view;
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
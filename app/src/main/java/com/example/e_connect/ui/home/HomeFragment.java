package com.example.e_connect.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_connect.R;
import com.example.e_connect.databinding.FragmentHomeBinding;
import com.example.e_connect.ui.my_profile.feed_post_Model;
import com.example.e_connect.ui.my_profile.my_profile_Adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    String user_id;
    RecyclerView home_RecyclerView;
    home_Adapter mainAdapter;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Context context = requireContext();

        home_RecyclerView = (RecyclerView) root.findViewById(R.id.home_RecyclerView);

        // Display the username
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            user_id = intent.getStringExtra("username");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        home_RecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<feed_post_Model> options =
                new FirebaseRecyclerOptions.Builder<feed_post_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users").child(user_id).child("post").child("FeedPost"), feed_post_Model.class)
                        .build();

        mainAdapter = new home_Adapter(options);
        home_RecyclerView.setAdapter(mainAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
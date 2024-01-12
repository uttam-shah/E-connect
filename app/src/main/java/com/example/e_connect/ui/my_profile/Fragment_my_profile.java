package com.example.e_connect.ui.my_profile;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_connect.R;
import com.example.e_connect.upload_post.PostActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fragment_my_profile extends Fragment {

    ImageView profile_photo, add_post, fragment_icon_menu;
    TextView display_name, user_name;
    String user_id;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("Registered Users");
    AppCompatButton edit_profile_btn;
    RecyclerView profileRecycle;
    my_profile_Adapter mainAdapter;

    public Fragment_my_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Obtain the Context
        Context context = requireContext();

        add_post = view.findViewById(R.id.fragment_icon_add);
        user_name = view.findViewById(R.id.user_name);
        display_name = view.findViewById(R.id.display_name);
        profile_photo = view.findViewById(R.id.profile_photo);
        edit_profile_btn = view.findViewById(R.id.edit_profile_btn);
        fragment_icon_menu = view.findViewById(R.id.fragment_icon_menu);
        profileRecycle = view.findViewById(R.id.profileRecycle);

        // Display the username
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            user_id = intent.getStringExtra("username");
            if (user_id != null) {
                user_name.setText(user_id);
            }
        }

        if (profileRecycle != null) {
            // Initialize the RecyclerView and its adapter
            // ...

            // Use GridLayoutManager with span count 3
            GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
            profileRecycle.setLayoutManager(layoutManager);
        }

        FirebaseRecyclerOptions<feed_post_Model> options =
                new FirebaseRecyclerOptions.Builder<feed_post_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users").child(user_id).child("post").child("FeedPost"), feed_post_Model.class)
                        .build();

        if (mainAdapter == null) {
            // Initialize your adapter
            // ...
            mainAdapter = new my_profile_Adapter(options);
            profileRecycle.setAdapter(mainAdapter);
        }




        //Add Post Button clicked
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("user_name",user_id);
                startActivity(intent);
            }
        });

        fragment_icon_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        // Edit Profile Button clicked
        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Activity_edit_profile.class);
                intent.putExtra("username",user_id);
                startActivity(intent);
            }
        });



        // Show the name of the user
        String usernameToSearch = user_id;
        usersRef.orderByChild("userName").equalTo(usernameToSearch).addListenerForSingleValueEvent(new ValueEventListener() {
            String name;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    name = userSnapshot.child("name").getValue(String.class);
                    if (name != null) {
                        display_name.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // show the user profile
        // String usernameToSearch = user_name;
        usersRef.orderByChild("userName").equalTo(usernameToSearch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profilePhotoUrl = null;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    profilePhotoUrl = userSnapshot.child("profile_url").getValue(String.class);
                }
                // Check if profilePhotoUrl is not empty and not null before loading with Picasso
                if (profilePhotoUrl != null && !profilePhotoUrl.isEmpty()) {
                    Picasso.get().load(profilePhotoUrl).into(profile_photo);
                } else {
                    // Handle the case where the profilePhotoUrl is empty or null
                    // You can set a default image or show an error message.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here.
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDialog() {

        final Dialog dialog1 = new Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_profile_layout);

        LinearLayout dialogSetting = dialog1.findViewById(R.id.dialogSetting);
        LinearLayout dialogMyQueries = dialog1.findViewById(R.id.dialogMyQueries);
        LinearLayout dialogMyNotes = dialog1.findViewById(R.id.dialogMyNotes);
        LinearLayout dialogLogOut = dialog1.findViewById(R.id.dialogLogOut);

        dialogSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Setting is clicked", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        });

        dialogMyQueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "My Queries is clicked", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        });
        dialogMyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "My Notes is clicked", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        });
        dialogLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Logout  is clicked", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        });

        dialog1.show();
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.getWindow().setGravity(Gravity.BOTTOM);
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



}

package com.example.e_connect.upload_post;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.e_connect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class FeedPostFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 123;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ImageView photoImageView;
    private EditText subtitleEditText;
    private Button postButton;
    String user_name;




    public FeedPostFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed_post, container, false);

        photoImageView = root.findViewById(R.id.photoImageView);
        subtitleEditText = root.findViewById(R.id.subtitleEditText);
        postButton = root.findViewById(R.id.postButton);

        // Retrieve the intent from the parent activity
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra("user_name")) {
            user_name = intent.getStringExtra("user_name");
            // Now, you can use the "userName" in your fragment
        }






        // Initialize Firebase Storage and Realtime Database
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        // Set an onClickListener for the ImageView to choose an image
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // Set an onClickListener for the Post button
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        return root;
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);
                photoImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            // Get a reference to the location where the image will be stored
            StorageReference ref = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

            // Upload the image to Firebase Storage
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the image
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Image uploaded successfully, now add other details to the Realtime Database
                                    String subtitle = subtitleEditText.getText().toString();
                                    String userName = user_name; // Replace with the actual user's username

                                    // Create a Post object
                                    FeedPostModel post = new FeedPostModel(downloadUri.toString(), subtitle,userName,System.currentTimeMillis(),0, 0);


                                    // Reference to the "Posts" node
                                    DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference("Registered Users");

                                    // Push the new post to the "Posts" node
                                    String postId = postsReference.push().getKey();
                                    postsReference.child(user_name).child("post").child("FeedPost").push().setValue(post);

                                    Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT).show();

                                    // Clear the form
                                    photoImageView.setImageResource(R.drawable.profile);
                                    subtitleEditText.setText("");
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}

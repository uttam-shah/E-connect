package com.example.e_connect.ui.my_profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_connect.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class Activity_edit_profile extends AppCompatActivity {
    ImageView profile_photo;
    Button btn_browse_photo, btn_upload_photo;
    TextView done, cancle;
    EditText edit_name, edit_bio, edit_gender;
    String username;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("Registered Users");

    Uri filepath;
    Bitmap bitmap;

    // ActivityResultLauncher for requesting permission
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted, now open the file picker
                    openFilePicker();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        done = findViewById(R.id.btn_done);
        edit_name = findViewById(R.id.edit_name);
        edit_bio = findViewById(R.id.edit_bio);
        edit_gender = findViewById(R.id.edit_gender);
        cancle = findViewById(R.id.btn_cancle);
        btn_browse_photo = findViewById(R.id.btn_browse_photo);
        btn_upload_photo = findViewById(R.id.btn_upload_photo);
        profile_photo = findViewById(R.id.profile_photo);

        // Check if there is an intent associated with this activity
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve data from the intent
            username = intent.getStringExtra("username");
            if (username != null) {

            }
        }

        // Show the user profile
        String usernameToSearch = username;
        usersRef.orderByChild("userName").equalTo(usernameToSearch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profilePhotoUrl = null;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    profilePhotoUrl = userSnapshot.child("profile_url").getValue(String.class);
                }
                // Check if profilePhotoUrl is not empty or null before loading with Picasso
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
                Toast.makeText(Activity_edit_profile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_browse_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request permission using ActivityResultLauncher
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        });

        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) {
                    uploadToFirebase();
                } else {
                    Toast.makeText(Activity_edit_profile.this, "Please select an image to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // When click on cancel button
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit name of user
                String enteredName = edit_name.getText().toString().trim();
                if (!enteredName.isEmpty()) {
                    usersRef.child(username).child("name").setValue(enteredName);
                }

                // Edit Bio of user
                String enteredBio = edit_bio.getText().toString().trim();
                if (!enteredBio.isEmpty()) {
                    usersRef.child(username).child("bio").setValue(enteredBio);
                }

                // Edit Gender of user
                String enteredGender = edit_gender.getText().toString().trim();
                if (!enteredGender.isEmpty()) {
                    usersRef.child(username).child("gender").setValue(enteredGender);
                }

                // Clear EditText fields
                edit_name.setText("");
                edit_bio.setText("");
                edit_gender.setText("");

                Toast.makeText(Activity_edit_profile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    // Add this method to open the file picker after getting permission
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Permission granted, now proceed with file selection
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile_photo.setImageBitmap(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadToFirebase() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference().child("profile-photo" + username);
        uploader.putFile(filepath)
                .addOnSuccessListener(taskSnapshot -> {
                    // Retrieve the download URL of the uploaded image
                    uploader.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        if (downloadUri != null) {
                            String imageUrl = downloadUri.toString();
                            // Call a function to store this URL in the Realtime Database
                            storeImageDownloadUrlInDatabase(imageUrl);
                        } else {
                            // Handle the case where downloadUri is null
                        }
                    });

                    dialog.dismiss();
                    Toast.makeText(Activity_edit_profile.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded :" + (int) percent + " %");
                });
    }

    private void storeImageDownloadUrlInDatabase(String imageUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("Registered Users").child(username).child("profile_url").setValue(imageUrl);
    }
}

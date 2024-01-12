package com.example.e_connect.upload_post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.e_connect.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class QueryPostFragment extends Fragment {
    private EditText titleEditText;
    private EditText textEditText;
    private DatabaseReference databaseReference;
    private String user_name;

    public QueryPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_post, container, false);

        titleEditText = view.findViewById(R.id.editTextTitle);
        textEditText = view.findViewById(R.id.editTextText);

        // Retrieve data from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_name = bundle.getString("user_name");
            // Now you have the user_name in your fragment
        }

        // Initialize the Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        view.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditText fields
                String title = titleEditText.getText().toString();
                String text = textEditText.getText().toString();
                long timestamp = Calendar.getInstance().getTimeInMillis(); // Current time in milliseconds
                int likes = 0; // Initialize with 0 likes
                int comments = 0; // Initialize with 0 comments

                // Create a new QueryPostModel instance
                QueryPostModel query = new QueryPostModel(title, text, timestamp, likes, comments);

                // Push the data to Firebase Database
                databaseReference.child(user_name).child("post").child("queri_post").push().setValue(query);

                // Clear the input fields
                titleEditText.setText("");
                textEditText.setText("");
            }
        });

        return view;
    }
}

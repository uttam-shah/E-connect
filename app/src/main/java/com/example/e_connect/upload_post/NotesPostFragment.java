package com.example.e_connect.upload_post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.example.e_connect.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;

public class NotesPostFragment extends Fragment {
    private static final int REQUEST_PICK_PDF = 1;
    private AppCompatButton choosePdfButton;
    private ImageView pdfPreviewImage;
    private TextView selectedFileTextView;
    private EditText streamEditText, subjectEditText, descriptionEditText;
    private AppCompatButton uploadButton;
    private StorageReference storageReference;
    private DatabaseReference pdfsDatabaseReference;
    Uri selectedPdfUri;
    private String user_name;

    public NotesPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_post, container, false);

        storageReference = FirebaseStorage.getInstance().getReference().child("pdfs");
        pdfsDatabaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        choosePdfButton = view.findViewById(R.id.buttonChoosePdf);
        pdfPreviewImage = view.findViewById(R.id.imageViewPdfPreview);
        selectedFileTextView = view.findViewById(R.id.textViewSelectedFile);
        streamEditText = view.findViewById(R.id.editTextStream);
        subjectEditText = view.findViewById(R.id.editTextSubject);
        descriptionEditText = view.findViewById(R.id.editTextDescription);
        uploadButton = view.findViewById(R.id.buttonUpload);

        // Retrieve data from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_name = bundle.getString("user_name");
            // Now you have the user_name in your fragment
        }

        choosePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a file picker to choose a PDF file
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, REQUEST_PICK_PDF);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload the selected PDF file to Firebase
                uploadPdfToFirebase();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_PDF && resultCode == -1) {
            selectedPdfUri = data.getData();
            selectedFileTextView.setText("Selected PDF: " + selectedPdfUri.getLastPathSegment());

            // Load the first page of the PDF into the ImageView
            pdfPreviewImage.setImageBitmap(getPdfFirstPage(selectedPdfUri));
        }
    }

    private Bitmap getPdfFirstPage(Uri pdfUri) {
        try {
            ParcelFileDescriptor fileDescriptor = requireActivity().getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfRenderer renderer = new PdfRenderer(fileDescriptor);

            // Get the number of pages in the PDF
            int pageCount = renderer.getPageCount();

            // Render only the first page
            PdfRenderer.Page page = renderer.openPage(0);

            // Create a bitmap to render the page
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

            // Render the page to the bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            // Close the page and renderer
            page.close();
            renderer.close();

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void uploadPdfToFirebase() {
        // Convert the first page of the PDF to a byte array
        byte[] pdfImageBytes = getByteArrayFromBitmap(getPdfFirstPage(selectedPdfUri));


        // Create a unique file name for the PDF using the current timestamp
        String pdfFileName = System.currentTimeMillis() + ".pdf";

        // Get a reference to the location where the PDF will be stored
        StorageReference pdfRef = storageReference.child(pdfFileName);

        // Upload the PDF to Firebase Storage
        pdfRef.putFile(selectedPdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pdfRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        // Get the download URL of the PDF
                        String pdfUrl = downloadUri.toString();

                        // Create a unique file name for the PDF image using the current timestamp
                        String imageFileName = System.currentTimeMillis() + "_pdf_image.jpg";

                        // Get a reference to the location where the PDF image will be stored
                        StorageReference pdfImageRef = storageReference.child(imageFileName);

                        // Upload the PDF image to Firebase Storage
                        pdfImageRef.putBytes(pdfImageBytes)
                                .addOnSuccessListener(imageTaskSnapshot -> {
                                    pdfImageRef.getDownloadUrl().addOnSuccessListener(imageDownloadUri -> {
                                        // Get the download URL of the PDF image
                                        String pdfImageUrl = imageDownloadUri.toString();

                                        // TODO: Store the pdfUrl, pdfImageUrl, stream, subject, description in Firebase Realtime Database
                                        // For example, create a NotesPostModel and push the data to the database
                                        NotesPostModel postModel = new NotesPostModel(user_name,pdfUrl, pdfImageUrl, streamEditText.getText().toString(), subjectEditText.getText().toString(), descriptionEditText.getText().toString(), System.currentTimeMillis());

                                        // Push the data to the Realtime Database
                                        pdfsDatabaseReference.child(user_name).child("post").child("notes_post").push().setValue(postModel);

                                        // Reset the form
                                        resetForm();

                                        Toast.makeText(requireContext(), "PDF and image uploaded successfully", Toast.LENGTH_SHORT).show();
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "PDF image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "PDF upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void resetForm() {
        selectedFileTextView.setText("No file selected");
        pdfPreviewImage.setImageBitmap(null);
        streamEditText.setText("");
        subjectEditText.setText("");
        descriptionEditText.setText("");
        selectedPdfUri = null; // Reset the selected PDF URI
    }


    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}

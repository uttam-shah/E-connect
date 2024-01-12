package com.example.e_connect.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_connect.R;
import com.example.e_connect.ui.my_profile.feed_post_Model;
import com.example.e_connect.upload_post.NotesPostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class notes_Adapter extends FirebaseRecyclerAdapter<NotesPostModel, notes_Adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public notes_Adapter(@NonNull FirebaseRecyclerOptions<NotesPostModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull NotesPostModel model) {

        if (model != null && model.getImageUrl() != null) {
            // Load the image using Glide or any other image loading library
            Glide.with(holder.pdfimage.getContext())
                    .load(model.getImageUrl())
                    .centerCrop()
                    .into(holder.pdfimage);
        }
        holder.notes_stream.setText(model.getStream());
        holder.notes_subject.setText(model.getSubject());
        holder.notes_subtitle.setText(model.getDescription());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_single_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
         ImageView imageView, pdfimage;
         TextView notes_stream, notes_subject, notes_subtitle;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img1);
            pdfimage = (ImageView)  itemView.findViewById(R.id.pdfimage);
            notes_stream = (TextView) itemView.findViewById(R.id.notes_stream);
            notes_subject = (TextView) itemView.findViewById(R.id.notes_subject);
            notes_subtitle = (TextView) itemView.findViewById(R.id.notes_subtitle);
        }
    }
}

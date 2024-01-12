package com.example.e_connect.ui.home;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

public class home_Adapter extends FirebaseRecyclerAdapter<feed_post_Model, home_Adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public home_Adapter(@NonNull FirebaseRecyclerOptions<feed_post_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull feed_post_Model model) {

        if (model != null && model.getImageUrl() != null) {
            // Load the image using Glide or any other image loading library
            Glide.with(holder.postImage.getContext())
                    .load(model.getImageUrl())
                    .into(holder.postImage);
        }
        // Set text for your TextView
        holder.post_username.setText(model.getUserName());
        holder.subtitle.setText(model.getSubtitle());




    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_single_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
         ImageView postImage,profilePhoto;
         TextView subtitle,post_username;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            profilePhoto = (ImageView) itemView.findViewById(R.id.profile_photo);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            post_username = (TextView) itemView.findViewById(R.id.post_username);

        }
    }
}

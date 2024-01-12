package com.example.e_connect.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_connect.R;
import com.example.e_connect.ui.my_profile.feed_post_Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class notifications_Adapter extends FirebaseRecyclerAdapter<feed_post_Model, notifications_Adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public notifications_Adapter(@NonNull FirebaseRecyclerOptions<feed_post_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull feed_post_Model model) {

        if (model != null && model.getImageUrl() != null) {
            // Load the image using Glide or any other image loading library
            Glide.with(holder.imageView.getContext())
                    .load(model.getImageUrl())
                    .centerCrop()
                    .into(holder.imageView);
        }else {

        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_profile_single_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
         ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img1);
        }
    }
}

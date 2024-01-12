package com.example.e_connect.ui.queries;

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
import com.example.e_connect.upload_post.QueryPostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class queries_Adapter extends FirebaseRecyclerAdapter<QueryPostModel, queries_Adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public queries_Adapter(@NonNull FirebaseRecyclerOptions<QueryPostModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull QueryPostModel model) {


        // Set text for your TextView
        //holder.post_username.setText(model.getUserName());
        holder.postQuerie.setText(model.getText());
        holder.post_Querie_title.setText(model.getTitle());




    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queries_single_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        // ImageView postImage,profilePhoto;
         TextView postQuerie,post_username,post_Querie_title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            postQuerie = (TextView) itemView.findViewById(R.id.post_Querie_text);
            post_username = (TextView) itemView.findViewById(R.id.post_username);
            post_Querie_title = (TextView) itemView.findViewById(R.id.post_Querie_title);

        }
    }
}

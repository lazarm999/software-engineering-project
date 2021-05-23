package com.parovi.zadruga.adapters;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.models.nonEntityModels.CommentUser;

import java.util.ArrayList;
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private ArrayList<CommentUser> commentsList;

    public CommentsAdapter(ArrayList<CommentUser> commentsList){
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentViewHolder nvh = new CommentViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentViewHolder holder, int position) {
        CommentUser currComment = commentsList.get(position);
        holder.civProfileImage.setImageResource(R.drawable.accepted); //TODO: ovde treba da ide profilna
        holder.tvUsername.setText(currComment.getUsername());
        holder.tvTimeAgo.setText("50 hours ago");
        holder.tvComment.setText(currComment.getCommentText());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        private ImageView civProfileImage;
        private TextView tvUsername;
        private TextView tvTimeAgo;
        private TextView tvComment;

        public CommentViewHolder(@NonNull  View itemView) {
            super(itemView);
            civProfileImage = itemView.findViewById(R.id.civProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            tvComment = itemView.findViewById(R.id.tvComment);
        }

        public ImageView getCivProfileImage() {
            return civProfileImage;
        }

        public TextView getTvUsername() {
            return tvUsername;
        }

        public TextView getTvTimeAgo() {
            return tvTimeAgo;
        }

        public TextView getTvComment() {
            return tvComment;
        }
    }
}

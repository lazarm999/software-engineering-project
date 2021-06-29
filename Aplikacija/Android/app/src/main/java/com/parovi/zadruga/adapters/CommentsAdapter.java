package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.databinding.ItemCommentBinding;
import com.parovi.zadruga.models.responseModels.CommentResponse;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<CommentResponse> commentsList;

    private CommentListListener fragment;

    public CommentsAdapter() {
        commentsList = new ArrayList<>();
    }
    public void setCommentsList(List<CommentResponse> commentsList) {
        this.commentsList = commentsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentViewHolder holder, int position) {
        holder.bind(commentsList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onCommentSelected(commentsList.get(position));
            }
        });

        holder.binding.imgBtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onReport(commentsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        private ItemCommentBinding binding;

        public CommentViewHolder(@NonNull  ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(CommentResponse currComment) {
            //binding.civProfileImage.setImageResource(R.drawable.accepted); //TODO: ovde treba da ide profilna

            if (currComment.getUserImage() != null)
                binding.civProfileImage.setImageBitmap(currComment.getUserImage());
            binding.tvUsername.setText(currComment.getUser().getUsername());
            binding.tvTimeAgo.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(currComment.getPostTime()));
            binding.tvComment.setText(currComment.getComment());
        }
    }

    public interface CommentListListener {
        void onCommentSelected(CommentResponse comment);
        void onReport(CommentResponse comment);
    }
}

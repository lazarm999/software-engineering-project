package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.ItemCommentBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.responseModels.CommentResponse;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<CommentResponse> commentsList;
    private Ad ad;
    private CommentListListener fragment;

    public CommentsAdapter(CommentListListener listener) {
        fragment = listener;
        commentsList = new ArrayList<>();
    }
    public void setAd(Ad ad) {
        if (this.ad == null) {
            this.ad = ad;
            notifyDataSetChanged();
        }
    }
    public Ad getAd() {
        return ad;
    }
    public void setCommentsList(List<CommentResponse> commentsList) {
        this.commentsList = commentsList;
        notifyDataSetChanged();
    }
    public void removeItem(int pos) {
        if (pos >= 0 && pos < commentsList.size()) {
            commentsList.remove(pos);
            notifyItemRemoved(pos);
        }
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
        holder.bindTo(commentsList.get(position), ad);
        holder.binding.civProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onCommentAuthorSelected(commentsList.get(position));
            }
        });
        holder.binding.imgBtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onCommentReported(commentsList.get(position));
            }
        });
        holder.binding.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onCommentDeleted(commentsList.get(position), position);
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
        public void bindTo(CommentResponse currComment, Ad ad) {
            if (currComment.getUserImage() != null)
                binding.civProfileImage.setImageBitmap(currComment.getUserImage());
            binding.tvUsername.setText(currComment.getUser().getUsername());
            binding.tvTimeAgo.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(currComment.getPostTime()));
            binding.tvComment.setText(currComment.getComment());

            // logika prikaza dugmica za brisanje i prijavu neprikladnog sadrzaja
            User loggedUser = Utility.getLoggedInUser(App.getAppContext());
            binding.imgBtnReport.setVisibility(View.VISIBLE);
            if (loggedUser.isAdmin() ||
                    loggedUser.getUserId() == currComment.getUser().getUserId()) {
                binding.imgBtnDelete.setVisibility(View.VISIBLE);
                binding.imgBtnReport.setVisibility(View.INVISIBLE);
            }
            else if (ad != null && loggedUser.getUserId() == ad.getEmployer().getUserId()) {
                binding.imgBtnDelete.setVisibility(View.VISIBLE);
                binding.imgBtnReport.setVisibility(View.VISIBLE);
            }
            else
                binding.imgBtnDelete.setVisibility(View.INVISIBLE);
        }
    }

    public interface CommentListListener {
        void onCommentAuthorSelected(CommentResponse comment);
        void onCommentReported(CommentResponse comment);
        void onCommentDeleted(CommentResponse comment, int pos);
    }
}

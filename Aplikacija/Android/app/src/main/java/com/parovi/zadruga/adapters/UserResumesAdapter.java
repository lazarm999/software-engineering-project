package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.data.UserInfoResume;
import com.parovi.zadruga.databinding.UserListItemBinding;

import java.util.List;
import java.util.zip.Inflater;

public class UserResumesAdapter extends RecyclerView.Adapter<UserResumesAdapter.UserResumeViewHolder> {
    private List<UserInfoResume> users;

    public void setUsers(List<UserInfoResume> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserListItemBinding binding = UserListItemBinding.inflate(inflater, parent, false);
        return new UserResumeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserResumeViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserResumeViewHolder extends RecyclerView.ViewHolder {
        UserListItemBinding binding;

        public UserResumeViewHolder(@NonNull UserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserInfoResume user) {
            binding.tvName.setText(user.getName());
            binding.tvUsername.setText(user.getUsername());
        }
    }
}

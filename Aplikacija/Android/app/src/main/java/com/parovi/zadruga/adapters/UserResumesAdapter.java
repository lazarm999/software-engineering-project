package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.data.UserInfoResume;
import com.parovi.zadruga.databinding.UserListItemBinding;
import com.parovi.zadruga.models.entityModels.User;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class UserResumesAdapter extends RecyclerView.Adapter<UserResumesAdapter.UserResumeViewHolder> {
    private List<User> users;

    public UserResumesAdapter() {
        users = new ArrayList<User>();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
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

        public void bind(User user) {
            binding.tvName.setText(user.getFirstName() + " " + user.getLastName());
            binding.tvUsername.setText(user.getUsername());
            binding.imageView3.setImageBitmap(user.getProfileImage());
        }
    }
}

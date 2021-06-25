package com.parovi.zadruga.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.databinding.UserListItemBinding;
import com.parovi.zadruga.models.entityModels.User;

import org.jetbrains.annotations.NotNull;

public class UserAdapter extends ListAdapter<User, UserAdapter.UserViewHolder> {
    private UserListListener fragment;
    public UserAdapter(UserListListener fragment) {
        super(DIFF_CALLBACK);
        this.fragment = fragment;
    }
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserListItemBinding binding = UserListItemBinding.inflate(inflater, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserViewHolder holder, int position) {
        holder.bindTo(getItem(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onUserSelected(getItem(position));
            }
        });
    }

    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull User oldItem, @NonNull @NotNull User newItem)
        {
            Log.d("DEBUG", "areItemsTheSame: entered");
            return oldItem.getUserId() == newItem.getUserId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull User oldItem, @NonNull @NotNull User newItem) {
            Log.d("DEBUG", "areContentsTheSame: entered");
            return oldItem.getUsername().equals(newItem.getUsername()) &&
                    oldItem.getFirstName().equals(newItem.getFirstName()) &&
                    oldItem.getLastName().equals(newItem.getLastName()) &&
                    oldItem.getProfileImage() == newItem.getProfileImage();
        }
    };

    class UserViewHolder extends RecyclerView.ViewHolder {
        UserListItemBinding binding;

        public UserViewHolder(@NonNull UserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(User user) {
            binding.tvName.setText(user.getFirstName() + " " + user.getLastName());
            binding.tvUsername.setText(user.getUsername());
            binding.imageView3.setImageBitmap(user.getProfileImage());
        }
    }
    public interface UserListListener {
        void onUserSelected(User user);
    }
}

package com.parovi.zadruga.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.ChatResumeLayoutBinding;
import com.parovi.zadruga.models.entityModels.Chat;

import org.jetbrains.annotations.NotNull;

public class ChatsAdapter extends ListAdapter<Chat, ChatsAdapter.ChatViewHolder> {
    ChatListListener fragment;
    public ChatsAdapter(ChatListListener fragment) {
        super(DIFF_CALLBACK);
        this.fragment = fragment;
    }
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ChatResumeLayoutBinding binding = ChatResumeLayoutBinding.inflate(inflater, parent, false);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatsAdapter.ChatViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onChatResumeSelected(getItem(position));
            }
        });
    }

    public static final DiffUtil.ItemCallback<Chat> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Chat oldItem, @NonNull @NotNull Chat newItem)
        {
            Log.d("DEBUG", "areItemsTheSame: entered");
            return oldItem.getChatId().equals(newItem.getChatId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Chat oldItem, @NonNull @NotNull Chat newItem) {
            Log.d("DEBUG", "areContentsTheSame: entered");
            return oldItem.getChatId().equals(newItem.getChatId()) &&
                    oldItem.getProfileImage() == oldItem.getProfileImage() &&
                    oldItem.getLastSenderName() == oldItem.getLastSenderName() &&
                    oldItem.getLastMessage() == newItem.getLastMessage() &&
                    oldItem.getLastMessageDateSent() == oldItem.getLastMessageDateSent() &&
                    oldItem.getProfileImage() == newItem.getProfileImage();
        }
    };

    class ChatViewHolder extends RecyclerView.ViewHolder {
        ChatResumeLayoutBinding binding;

        public ChatViewHolder(@NonNull ChatResumeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chatResume) {
            binding.tvChatTitle.setText(chatResume.getChatTitle());
            String subtitle = chatResume.getFkLastSenderId() == Utility.getUserId(App.getAppContext()) ? "You" : chatResume.getLastSenderName() + ": ";
            subtitle += chatResume.getLastMessage();
            binding.tvLastMessage.setText(subtitle);
            binding.tvTimeOfLastMsg.setText(Long.toString(chatResume.getLastMessageDateSent()));//.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)));
            binding.imgChatImage.setImageBitmap(chatResume.getProfileImage());
        }
    }
    public interface ChatListListener {
        void onChatResumeSelected(Chat chat);
    }
}

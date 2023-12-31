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
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.ChatResumeLayoutBinding;
import com.parovi.zadruga.models.entityModels.Chat;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                    oldItem.getLastSenderUsername() == oldItem.getLastSenderUsername() &&
                    oldItem.getLastMessage() == newItem.getLastMessage() &&
                    oldItem.getLastMessageDateSent() == oldItem.getLastMessageDateSent() &&
                    oldItem.getProfileImage() == newItem.getProfileImage();
        }
    };

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private ChatResumeLayoutBinding binding;
        private final static int SUBTITLE_MAX_LENGTH = 40;

        public ChatViewHolder(@NonNull ChatResumeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Chat chatResume) {
            binding.tvChatTitle.setText(chatResume.getChatTitle());
            String subtitle;
            if (chatResume.getLastMessage() != null) {
                if (chatResume.getLastSenderUsername() == null)
                    subtitle = chatResume.getLastMessage();
                else {
                    subtitle = chatResume.getLastSenderUsername().equals(Utility.getLoggedInUser(App.getAppContext()).getUsername()) ? "You" : chatResume.getLastSenderUsername();
                    subtitle += ": " + chatResume.getLastMessage();
                }
                if (subtitle.length() > SUBTITLE_MAX_LENGTH) {
                    subtitle = subtitle.substring(0, SUBTITLE_MAX_LENGTH);
                    subtitle += "...";
                }
                binding.tvLastMessage.setText(subtitle);
            }
            else
                binding.tvLastMessage.setText(R.string.noMessages);

            if (chatResume.getLastMessageDateSent() != 0) {
                LocalDateTime time = LocalDateTime.ofEpochSecond(chatResume.getLastMessageDateSent(), 0, ZoneOffset.UTC);
                binding.tvTimeOfLastMsg.setText(time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)));
            }
            else
                binding.tvTimeOfLastMsg.setVisibility(View.INVISIBLE);

            if (chatResume.getFkAdId() != null)
                binding.imgChatImage.setImageBitmap(chatResume.getProfileImage());
            else
                binding.imgChatImage.setImageResource(R.drawable.job);
        }
    }
    public interface ChatListListener {
        void onChatResumeSelected(Chat chat);
    }
}

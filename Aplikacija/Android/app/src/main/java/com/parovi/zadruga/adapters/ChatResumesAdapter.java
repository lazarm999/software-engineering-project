package com.parovi.zadruga.adapters;

import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.data.ChatResume;
import com.parovi.zadruga.databinding.ChatResumeLayoutBinding;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ChatResumesAdapter extends RecyclerView.Adapter<ChatResumesAdapter.ChatResumeViewHolder> {
    private List<ChatResume> chats;
    private ChatListListener chatListListener;

    public ChatResumesAdapter(ChatListListener chatListListener) {
        this.chatListListener = chatListListener;
    }

    public void setChats(List<ChatResume> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ChatResumeLayoutBinding binding = ChatResumeLayoutBinding.inflate(inflater, parent, false);
        return new ChatResumeViewHolder(binding);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChatResumeViewHolder holder, int position) {
        ChatResume chatResume = chats.get(position);
        holder.bind(chatResume);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListListener.onChatResumeSelected(chatResume.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ChatResumeViewHolder extends RecyclerView.ViewHolder {
        ChatResumeLayoutBinding binding;

        public ChatResumeViewHolder(@NonNull ChatResumeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(ChatResume chatResume) {
            binding.tvChatTitle.setText(chatResume.getChatTitle());
            binding.tvLastMessage.setText(chatResume.getLastMessage());
            binding.tvTimeOfLastMsg.setText(chatResume.getTimeOfLastMessage().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)));
            binding.tvLastMessage.setTypeface(null,
                    chatResume.isSeen() ? Typeface.NORMAL : Typeface.BOLD);
        }
    }

    public interface ChatListListener {
        void onChatResumeSelected(long id);
    }
}

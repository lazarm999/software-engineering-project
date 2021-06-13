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
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.data.ChatResume;
import com.parovi.zadruga.databinding.ChatResumeLayoutBinding;
import com.parovi.zadruga.models.entityModels.Chat;
import com.quickblox.chat.model.QBChatDialog;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class ChatResumesAdapter extends RecyclerView.Adapter<ChatResumesAdapter.ChatResumeViewHolder> {
    private List<Chat> chats;
    private ChatListListener chatListListener;

    public ChatResumesAdapter(ChatListListener chatListListener) {
        this.chatListListener = chatListListener;
        chats = new ArrayList<Chat>();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
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
        Chat chatResume = chats.get(position);
        holder.bind(chatResume);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListListener.onChatResumeSelected(chatResume.getQbChat());
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
        public void bind(Chat chatResume) {
            if (chatResume.getType() == Utility.ChatType.PRIVATE)
                binding.tvChatTitle.setText(chatResume.getLastSenderName());
            else if (chatResume.getType() == Utility.ChatType.GROUP)
                binding.tvChatTitle.setText("Group chat");
            else
                binding.tvChatTitle.setText(chatResume.getChatId());
            binding.tvLastMessage.setText(chatResume.getLastMessage());
            binding.tvTimeOfLastMsg.setText(Long.toString(chatResume.getLastMessageDateSent()));//.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)));
            //binding.tvLastMessage.setTypeface(null,
             //       chatResume.get ? Typeface.NORMAL : Typeface.BOLD);
            binding.imgChatImage.setImageBitmap(chatResume.getProfileImage());
        }
    }

    public interface ChatListListener {
        void onChatResumeSelected(QBChatDialog chat);
    }
}

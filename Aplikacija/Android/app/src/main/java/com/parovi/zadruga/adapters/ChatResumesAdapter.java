package com.parovi.zadruga.adapters;

import android.app.Application;
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

import com.parovi.zadruga.App;
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
                chatListListener.onChatResumeSelected(chatResume);
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

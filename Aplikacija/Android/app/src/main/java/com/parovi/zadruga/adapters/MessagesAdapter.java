package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.FragmentChatMessagesBinding;
import com.parovi.zadruga.databinding.MyMessageLayoutBinding;
import com.parovi.zadruga.databinding.OthersMessageLayoutBinding;
import com.parovi.zadruga.models.entityModels.Message;

import java.util.ArrayList;
import java.util.List;
// TODO: probaj sa diffutil-om

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private static final int MY_MESSAGE = 1;
    private static final int OTHERS_MESSAGE = 0;
    private int userId;
    private List<Message> messages;

    public MessagesAdapter() {
        messages = new ArrayList<Message>();
        this.userId = Utility.getLoggedInUserQbId(App.getAppContext());
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void onMessageAdded() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getFkSenderQbId() == userId ? MY_MESSAGE : OTHERS_MESSAGE;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MY_MESSAGE) {
            MyMessageLayoutBinding binding = MyMessageLayoutBinding.inflate(inflater, parent, false);
            return new MyMessageViewHolder(binding);
        }
        else {
            OthersMessageLayoutBinding binding = OthersMessageLayoutBinding.inflate(inflater, parent, false);
            return new OthersMessageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindTo(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View view) {
            super(view);
        }
        abstract public void bindTo(Message message);
    }

    class MyMessageViewHolder extends MessageViewHolder {
        private MyMessageLayoutBinding binding;
        public MyMessageViewHolder(MyMessageLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @Override
        public void bindTo(Message message) {
            binding.tvMessageContent.setText(message.getMessage());
        }
    }

    class OthersMessageViewHolder extends MessageViewHolder {
        private OthersMessageLayoutBinding binding;

        public OthersMessageViewHolder(OthersMessageLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @Override
        public void bindTo(Message message) {
            //binding.tvUsername.setText("@" + message.getFkSenderId());
            binding.tvUsername.setText(message.getSenderUsername());
            binding.tvMessageContent.setText(message.getMessage());
        }
    }

}

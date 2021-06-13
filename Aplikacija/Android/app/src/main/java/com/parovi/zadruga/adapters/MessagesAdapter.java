package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private static final int MY_MESSAGE = 1;
    private static final int OTHERS_MESSAGE = 0;
    private int userId;
    private List<Message> messages;

    public MessagesAdapter() {
        messages = new ArrayList<Message>();
        this.userId = Utility.getUserQbId(App.getAppContext());
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
        return messages.get(position).getFkSenderId() == userId ? MY_MESSAGE : OTHERS_MESSAGE;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType == MY_MESSAGE ? R.layout.my_message_layout : R.layout.others_message_layout,
                parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvMessageContent);
        }

        public void bind(Message message) {
            textView.setText(message.getMessage());
        }
    }

}

package com.parovi.zadruga.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.MyMessageLayoutBinding;
import com.parovi.zadruga.databinding.OthersMessageLayoutBinding;
import com.parovi.zadruga.models.entityModels.Message;

import org.jetbrains.annotations.NotNull;

public class MessageBubbleAdapter extends ListAdapter<Message, MessageBubbleAdapter.MessageViewHolder> {
    private static final int MY_MESSAGE = 1;
    private static final int OTHERS_MESSAGE = 0;
    private int userId;

    public MessageBubbleAdapter() {
        super(DIFF_CALLBACK);
        this.userId = Utility.getLoggedInUserQbId(App.getAppContext());
    }

    public static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Message oldItem, @NonNull @NotNull Message newItem) {
            Log.d("DEBUG", "areItemsTheSame: entered");
            return oldItem.getMsgId().equals(newItem.getMsgId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Message oldItem, @NonNull @NotNull Message newItem) {
            Log.d("DEBUG", "areContentsTheSame: entered");
            return oldItem.getFkSenderQbId() == newItem.getFkSenderQbId() &&
                    oldItem.getMessage().equals(newItem.getMessage()) &&
                    oldItem.getDateSent().equals(newItem.getDateSent());
        }
    };

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getFkSenderQbId() == userId ? MY_MESSAGE : OTHERS_MESSAGE;
    }

    @NonNull
    @NotNull
    @Override
    public MessageBubbleAdapter.MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MY_MESSAGE) {
            MyMessageLayoutBinding binding = MyMessageLayoutBinding.inflate(inflater, parent, false);
            return new MyMessageViewHolder(binding);
        } else {
            OthersMessageLayoutBinding binding = OthersMessageLayoutBinding.inflate(inflater, parent, false);
            return new OthersMessageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageBubbleAdapter.MessageViewHolder holder, int position) {
        holder.bindTo(getItem(position));
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

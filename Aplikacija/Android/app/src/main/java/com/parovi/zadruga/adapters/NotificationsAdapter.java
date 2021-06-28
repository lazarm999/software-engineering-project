package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.Constants;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ItemNotificationBinding;
import com.parovi.zadruga.models.entityModels.Notification;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private ArrayList<Notification> notificationList;

    private NotificationListener fragment;

    public NotificationsAdapter(NotificationListener fragment)
    {
        super();
        this.fragment = fragment;
        notificationList = new ArrayList<>();
    }

    public NotificationsAdapter(ArrayList<Notification> notificationList){
        this.notificationList = notificationList;
    }

    public Notification getNotification(int i){
        return notificationList.get(i);
    }

    public void setNotificationList(ArrayList<Notification> notif)
    {
        this.notificationList = notif;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(inflater, parent, false);
        return new  NotificationsAdapter.NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.NotificationViewHolder holder, int position) {
        Notification currNotification = notificationList.get(position);
        if(currNotification.getType().equals(Constants.NOTIF_ACCEPTED)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.accepted);
            holder.binding.tvTitle.setText("You have been accepted for a job");
            holder.binding.tvDesc.setText(currNotification.getAd().getTitle());
        } else if(currNotification.getType().equals(Constants.NOTIF_DECLINED)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.declined);
            holder.binding.tvTitle.setText("Your application for a job has been declined");
            holder.binding.tvDesc.setText(currNotification.getAd().getTitle());
        } else if(currNotification.getType().equals(Constants.NOTIF_TAGGED)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.comment);
            holder.binding.tvTitle.setText("User @" + currNotification.getComment().getUser().getUsername() + "has mentioned you in a comment");
            holder.binding.tvDesc.setText(currNotification.getComment().getComment());
        } else if(currNotification.getType().equals(Constants.NOTIF_RATING)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.rating);
            holder.binding.tvTitle.setText("User @" + currNotification.getComment().getUser() + "has mentioned you in a comment");
            holder.binding.tvDesc.setText(currNotification.getComment().getComment());
        } else if(currNotification.getType().equals(Constants.NOTIF_AD_COMMENT)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.comment);
            holder.binding.tvTitle.setText("User @" + currNotification.getRating().getRater() + "has rated you");
            if(currNotification.getRating().getRating() == 5) {
                holder.binding.tvDesc.setText("You received " + currNotification.getRating().getRating() + ", bravo!");
            }
            else
                holder.binding.tvDesc.setText("You receiverd " + currNotification.getRating().getRating() + "!");
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        public ItemNotificationBinding binding;

        public NotificationViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Notification notification) {
//            binding.tvTitle.setText(notification.getTitle());
//            binding.tvDesc.setText(notification.getDesc());
        }

        public ImageView getIvNotificationIcon() {
            return binding.ivNotificationIcon;
        }

        public TextView getTvTitle() {
            return binding.tvTitle;
        }

        public TextView getTvDesc() {
            return binding.tvDesc;
        }
    }

    public interface NotificationListener {
        void onNotificationSelected(Notification notification);
    }
}

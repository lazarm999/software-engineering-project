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
        if(currNotification.getType().equals(Constants.ACCEPTED)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.accepted);
//            holder.binding.tvTitle.setText(currNotification.getTitle());
//            holder.binding.tvDesc.setText(currNotification.getDesc());
        } else if(currNotification.getType().equals(Constants.DECLINED)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.declined);
//            holder.binding.tvTitle.setText("Niste odabrani za oglas sa id-em" + Integer.toString(currNotification.getFkAdId()));
//            holder.binding.tvDesc.setText("vise srece drugi put");
        } else if(currNotification.getType().equals(Constants.COMMENT)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.comment);
//            holder.binding.tvTitle.setText("Korisnik taj i taj je komentarisao oglas sa id-em" + Integer.toString(currNotification.getFkAdId()));
//            holder.binding.tvDesc.setText("prozivao te tamo nesto po oglasi");
        } else if(currNotification.getType().equals(Constants.RATING)){
            holder.binding.ivNotificationIcon.setImageResource(R.drawable.rating);
//            holder.binding.tvTitle.setText("Korisnik taj i taj vas je ocenio" + Integer.toString(currNotification.getFkSenderId()));
//            holder.binding.tvDesc.setText("dao ti keca");
        }
        holder.binding.tvTitle.setText(currNotification.getTitle());
        holder.binding.tvDesc.setText(currNotification.getDesc());
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
                binding.tvTitle.setText(notification.getTitle());
                binding.tvDesc.setText(notification.getDesc());
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

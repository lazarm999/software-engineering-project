package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.Constants;
import com.parovi.zadruga.R;
import com.parovi.zadruga.models.entityModels.Notification;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private ArrayList<Notification> notificationsList;
    private OnNotificationItemClickListener onClickListener;

    public NotificationsAdapter(ArrayList<Notification> notificationsList, OnNotificationItemClickListener onClickListener){
        this.notificationsList = notificationsList;
        this.onClickListener = onClickListener;
    }

    public Notification getNotification(int i){
        return notificationsList.get(i);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        NotificationViewHolder nvh = new NotificationViewHolder(v, onClickListener);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.NotificationViewHolder holder, int position) {
        Notification currNotification = notificationsList.get(position);
        if(currNotification.getType().equals(Constants.NOTIF_ACCEPTED)){
            holder.ivNotificationIcon.setImageResource(R.drawable.accepted);
            holder.tvTitle.setText("Odabrani ste za oglas sa id-em" + Integer.toString(currNotification.getFkAdId()));
            holder.tvDesc.setText("brao prifacen si");
        } else if(currNotification.getType().equals(Constants.NOTIF_DECLINED)){
            holder.ivNotificationIcon.setImageResource(R.drawable.declined);
            holder.tvTitle.setText("Niste odabrani za oglas sa id-em" + Integer.toString(currNotification.getFkAdId()));
            holder.tvDesc.setText("vise srece drugi put");
        } else if(currNotification.getType().equals(Constants.NOTIF_TAGGED)){
            holder.ivNotificationIcon.setImageResource(R.drawable.comment);
            holder.tvTitle.setText("Korisnik taj i taj je komentarisao oglas sa id-em" + Integer.toString(currNotification.getFkAdId()));
            holder.tvDesc.setText("prozivao te tamo nesto po oglasi");
        } else if(currNotification.getType().equals(Constants.NOTIF_RATING)){
            holder.ivNotificationIcon.setImageResource(R.drawable.rating);
            holder.tvTitle.setText("Korisnik taj i taj vas je ocenio" + Integer.toString(currNotification.getFkSenderId()));
            holder.tvDesc.setText("dao ti keca");
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivNotificationIcon;
        private TextView tvTitle;
        private TextView tvDesc;

        public NotificationViewHolder(@NonNull  View itemView, OnNotificationItemClickListener onClickListener) {
            super(itemView);
            ivNotificationIcon = itemView.findViewById(R.id.imgAdItem);
            tvTitle = itemView.findViewById(R.id.txtAdItemHeader);
            tvDesc = itemView.findViewById(R.id.txtLocationAd);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }

        public ImageView getIvNotificationIcon() {
            return ivNotificationIcon;
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvDesc() {
            return tvDesc;
        }
    }

    public interface OnNotificationItemClickListener{
        public void onClick(int position);
    }
}

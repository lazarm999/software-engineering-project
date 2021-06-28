package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.Constants;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ItemNotificationBinding;
import com.parovi.zadruga.models.entityModels.Notification;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private static final int accepted = 1, declined = 2, rating = 3, comment = 4, tagged = 5;

    private ArrayList<Notification> notificationList;

    private NotificationListener fragment;

    public NotificationsAdapter(NotificationListener fragment) {
        super();
        this.fragment = fragment;
        notificationList = new ArrayList<>();
    }

    public NotificationsAdapter(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public Notification getNotification(int i) {
        return notificationList.get(i);
    }

    public void setNotificationList(ArrayList<Notification> notif) {
        this.notificationList = notif;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(inflater, parent, false);
        switch (viewType) {
            case accepted:
                return new AcceptedViewHolder(binding);
            case declined:
                return new DeclinedViewHolder(binding);
            case rating:
                return new RatingViewHolder(binding);
            case comment:
                return new TaggedViewHolder(binding);
            default:
                return new CommentViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.NotificationViewHolder holder, int position) {
//        Notification currNotification = notificationList.get(position);
//        if(currNotification.getType().equals(Constants.NOTIF_ACCEPTED)){
//            holder.binding.ivNotificationIcon.setImageResource(R.drawable.accepted);
//            holder.binding.tvTitle.setText("You have been accepted for a job");
//            holder.binding.tvDesc.setText(currNotification.getAd().getTitle());
//        } else if(currNotification.getType().equals(Constants.NOTIF_DECLINED)){
//            holder.binding.ivNotificationIcon.setImageResource(R.drawable.declined);
//            holder.binding.tvTitle.setText("Your application for a job has been declined");
//            holder.binding.tvDesc.setText(currNotification.getAd().getTitle());
//        } else if(currNotification.getType().equals(Constants.NOTIF_TAGGED)){
//            holder.binding.ivNotificationIcon.setImageResource(R.drawable.comment);
//            holder.binding.tvTitle.setText("User @" + currNotification.getComment().getUser().getUsername() + "has mentioned you in a comment");
//            holder.binding.tvDesc.setText(currNotification.getComment().getComment());
//        } else if(currNotification.getType().equals(Constants.NOTIF_RATING)){
//            holder.binding.ivNotificationIcon.setImageResource(R.drawable.rating);
//            holder.binding.tvTitle.setText("User @" + currNotification.getComment().getUser() + "has mentioned you in a comment");
//            holder.binding.tvDesc.setText(currNotification.getComment().getComment());
//        } else if(currNotification.getType().equals(Constants.NOTIF_AD_COMMENT)){
//            holder.binding.ivNotificationIcon.setImageResource(R.drawable.comment);
//            holder.binding.tvTitle.setText("User @" + currNotification.getRating().getRater() + "has rated you");
//            if(currNotification.getRating().getRating() == 5) {
//                holder.binding.tvDesc.setText("You received " + currNotification.getRating().getRating() + ", bravo!");
//            }
//            else
//                holder.binding.tvDesc.setText("You receiverd " + currNotification.getRating().getRating() + "!");
//        }

        holder.bindTo(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (notificationList.get(position).getType()) {
            case Constants.NOTIF_ACCEPTED:
                return accepted;
            case Constants.NOTIF_DECLINED:
                return declined;
            case Constants.NOTIF_RATING:
                return rating;
            case Constants.NOTIF_TAGGED:
                return comment;
            default:
                return tagged;
        }
    }

    abstract public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public ItemNotificationBinding binding;

        public NotificationViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        abstract void bindTo(Notification notification);
    }

    public class AcceptedViewHolder extends NotificationViewHolder {
        public AcceptedViewHolder(ItemNotificationBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Notification notification) {
            binding.ivNotificationIcon.setImageResource(R.drawable.accepted);
            binding.tvTitle.setText("You have been accepted for a job");
            binding.tvDesc.setText(notification.getAd().getTitle());
        }

    }

    public class DeclinedViewHolder extends NotificationViewHolder {
        public DeclinedViewHolder(ItemNotificationBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Notification notification) {
            binding.ivNotificationIcon.setImageResource(R.drawable.declined);
            binding.tvTitle.setText("Your application for a job has been declined");
            binding.tvDesc.setText(notification.getAd().getTitle());
        }

    }

    public class RatingViewHolder extends NotificationViewHolder {
        public RatingViewHolder(ItemNotificationBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Notification notification) {
            binding.ivNotificationIcon.setImageResource(R.drawable.rating);
            binding.tvTitle.setText("User @" + notification.getComment().getUser() + "has mentioned you in a comment");
            binding.tvDesc.setText(notification.getComment().getComment());
        }

    }

    public class CommentViewHolder extends NotificationViewHolder {
        public CommentViewHolder(ItemNotificationBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Notification notification) {
            binding.ivNotificationIcon.setImageResource(R.drawable.comment);
            binding.tvTitle.setText("User @" + notification.getRating().getRater() + "has rated you");
            if (notification.getRating().getRating() == 5) {
                binding.tvDesc.setText("You received " + notification.getRating().getRating() + ", bravo!");
            } else
                binding.tvDesc.setText("You receiverd " + notification.getRating().getRating() + "!");
        }

    }

    public class TaggedViewHolder extends NotificationViewHolder {
        public TaggedViewHolder(ItemNotificationBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Notification notification) {
            binding.ivNotificationIcon.setImageResource(R.drawable.comment);
            binding.tvTitle.setText("User @" + notification.getComment().getUser().getUsername() + "has mentioned you in a comment");
            binding.tvDesc.setText(notification.getComment().getComment());
        }
    }
    public interface NotificationListener {
    void onNotificationSelected(Notification notification);
}
}

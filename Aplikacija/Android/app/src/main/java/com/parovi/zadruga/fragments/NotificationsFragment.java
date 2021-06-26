package com.parovi.zadruga.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.Constants;
import com.parovi.zadruga.adapters.NotificationsAdapter;
import com.parovi.zadruga.databinding.FragmentNotificationsFragmentBinding;
import com.parovi.zadruga.models.entityModels.Notification;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements  NotificationsAdapter.NotificationListener{
    FragmentNotificationsFragmentBinding binding;
    private ArrayList<Notification> notificationList;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsFragmentBinding.inflate(inflater, container, false);

        RecyclerView rvNotifications = binding.rvNotifications;
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        NotificationsAdapter adapter = new NotificationsAdapter((NotificationsAdapter.NotificationListener) this);
        rvNotifications.setAdapter(adapter);
        notificationList = new ArrayList<>();
//        notificationList.add(new Notification(1,2,3,4,Constants.ACCEPTED,
//                "You have been accepted for a job", "Math 1 class"));
//        notificationList.add(new Notification(1,2,3,4,Constants.DECLINED,
//                "You have been declined for a job", "Helping the elderly with their groceries"));
//        notificationList.add(new Notification(1,2,3,4,Constants.COMMENT,
//                "User @markom has tagged you in a comment", "\"This looks like a job for you @vuk "));
//        notificationList.add(new Notification(1,2,3,4,Constants.RATING,
//                "User @bestEmployer123 has rated you", "You received an A, bravo!"));
//        notificationList.add(new Notification(i, i, i, i, Constants.DECLINED));
//        notificationList.add(new Notification(i, i, i, i, Constants.COMMENT));
//        notificationList.add(new Notification(i, i, i, i, Constants.RATING));
        adapter.setNotificationList(notificationList);

//        for (int i = 0; i < 30; i++){
//            if(i % 4 == 0)
//                notificationList.add(new Notification(i, i, i, i, Constants.ACCEPTED));
//            if(i % 4 == 1)
//                notificationList.add(new Notification(i, i, i, i, Constants.DECLINED));
//            if(i % 4 == 2)
//                notificationList.add(new Notification(i, i, i, i, Constants.COMMENT));
//            if(i % 4 == 3)
//                notificationList.add(new Notification(i, i, i, i, Constants.RATING));
//        }
        rvNotifications.setHasFixedSize(true);
        /*adapter = new NotificationsAdapter(notificationList, new NotificationsAdapter.OnNotificationItemClickListener() {
            @Override
            public void onClick(int position) {
                Notification notification = rvaNotifications.getNotification(position);
                if(notification.getType().equals(Constants.RATING)){
                    //intent koji vodi do tvog profila
                } else {
                    //intent koji vodi do ad aktivitija
                }
            }
        });*/

        return binding.getRoot();
    }

    @Override
    public void onNotificationSelected(Notification notification) {

    }
}
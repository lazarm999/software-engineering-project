package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.activities.UsersAchievementsActivity;
import com.parovi.zadruga.adapters.NotificationsAdapter;
import com.parovi.zadruga.databinding.FragmentNotificationsFragmentBinding;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.NotificationsViewModel;

import java.util.ArrayList;

import static com.parovi.zadruga.Constants.NOTIF_ACCEPTED;
import static com.parovi.zadruga.Constants.NOTIF_RATING;

public class NotificationsFragment extends Fragment implements  NotificationsAdapter.NotificationListener{
    NotificationsViewModel model;
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
        model = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        RecyclerView rvNotifications = binding.rvNotifications;
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        NotificationsAdapter adapter = new NotificationsAdapter((NotificationsAdapter.NotificationListener) this);
        rvNotifications.setAdapter(adapter);

        notificationList = new ArrayList<>();

        adapter.setNotificationList(notificationList);

        rvNotifications.setHasFixedSize(true);

        model.getNotifications().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setNotificationList((ArrayList<Notification>) customResponse.getBody());
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onNotificationSelected(Notification notification) {
        if(notification.getType().equals(NOTIF_ACCEPTED))
        {
            Intent intent = new Intent(requireActivity(), JobAdActivity.class);
            intent.putExtra("AdNotifID", notification.getFkAdId());
            startActivity(intent);
        }
        else if(notification.getType().equals((NOTIF_RATING)))
        {
            Intent intent = new Intent(requireActivity(), UsersAchievementsActivity.class);
            intent.putExtra("UserAchID", notification.getFkRatingId());
            startActivity(intent);
        }
    }
}
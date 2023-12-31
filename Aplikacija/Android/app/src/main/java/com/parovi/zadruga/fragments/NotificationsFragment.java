package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.activities.UsersAchievementsActivity;
import com.parovi.zadruga.adapters.NotificationsAdapter;
import com.parovi.zadruga.databinding.FragmentNotificationsBinding;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.NotificationsViewModel;

import java.util.ArrayList;

import static com.parovi.zadruga.Constants.NOTIF_ACCEPTED;
import static com.parovi.zadruga.Constants.NOTIF_AD_COMMENT;
import static com.parovi.zadruga.Constants.NOTIF_RATING;
import static com.parovi.zadruga.Constants.NOTIF_TAGGED;

public class NotificationsFragment extends Fragment implements  NotificationsAdapter.NotificationListener{
    NotificationsViewModel model;
    FragmentNotificationsBinding binding;
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
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
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
                binding.progressBarNotif.setVisibility(View.GONE);
            }
        });

        binding.refreshNotif.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.loadNotifications(true);
                binding.refreshNotif.setRefreshing(false);
            }
        });

        binding.nestedNotif.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    model.loadNotifications(false);
                    binding.progressBarNotif.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        model.loadNotifications(true);
    }

    @Override
    public void onNotificationSelected(Notification notification) {
        if(notification.getType().equals(NOTIF_ACCEPTED))
        {
            Intent intent = new Intent(requireActivity(), JobAdActivity.class);
            intent.putExtra(JobAdActivity.AD_ID, notification.getFkAdId());
            startActivity(intent);
        }
        else if(notification.getType().equals((NOTIF_RATING)))
        {
            Intent intent = new Intent(requireActivity(), UsersAchievementsActivity.class);
            intent.putExtra("UserAchID", notification.getFkRatingId());
            startActivity(intent);
        }
        else if(notification.getType().equals((NOTIF_AD_COMMENT)) || notification.getType().equals((NOTIF_TAGGED)))
        {
            Intent intent = new Intent(requireActivity(), JobAdActivity.class);
            intent.putExtra(JobAdActivity.AD_ID, notification.getComment().getAd());
            startActivity(intent);
        }
    }
}
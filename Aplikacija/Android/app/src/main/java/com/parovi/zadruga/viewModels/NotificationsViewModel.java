package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.repository.NotificationRepository;

import java.util.ArrayList;

public class NotificationsViewModel extends AndroidViewModel {

    private MutableLiveData<CustomResponse<?>> isNotified;
    private NotificationRepository notificationRepository;
    private ArrayList<Notification> notifications;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        notificationRepository = new NotificationRepository();
        isNotified = new MutableLiveData<>();
        loadNotifications();

    }

    public MutableLiveData<CustomResponse<?>> getIsNotified(){

        if(isNotified == null)
            isNotified = new MutableLiveData<CustomResponse<?>>();
        return isNotified;
    }

    private void loadNotifications() {
       // notificationRepository.getAllNotifications(token, notifications);
    }
}

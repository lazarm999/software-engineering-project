package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.repository.NotificationRepository;

public class NotificationsViewModel extends AndroidViewModel {

    private MutableLiveData<CustomResponse<?>> isNotified;
    private NotificationRepository notificationRepository;
    private  MutableLiveData<CustomResponse<?>> notifications;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        notificationRepository = new NotificationRepository();
        notifications = new MutableLiveData<>();
        isNotified = new MutableLiveData<>();
        loadNotifications();
    }

    public MutableLiveData<CustomResponse<?>> getIsNotified(){
        if(isNotified == null)
            isNotified = new MutableLiveData<CustomResponse<?>>();
        return isNotified;
    }

    private void loadNotifications() {
       notificationRepository.getNotifications(notifications);
    }

    public  MutableLiveData<CustomResponse<?>> getNotifications() {
        return notifications;
    }
}

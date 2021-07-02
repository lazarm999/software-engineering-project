package com.parovi.zadruga.api;

import com.parovi.zadruga.ChatNotification;
import com.parovi.zadruga.models.entityModels.Notification;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApi {
    @POST("chatNotification/")
    Call<ResponseBody> sendChatNotification(@Header("Authorization") String token, @Body ChatNotification notification);

    @GET("notifications/")
    Call<List<Notification>> getNotifications(@Header("Authorization") String token, @Query("pageSize") Integer pageSize, @Query("pageSkip") Integer pageSkip);
}

package com.parovi.zadruga.retrofit;

import com.google.firebase.messaging.RemoteMessage;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.PushNotification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi {
    @Headers({"Authorization: key="+Constants.FB_SERVER_KEY
            , "Content-Type:" + Constants.CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RemoteMessage message);

    @Headers({"Authorization: key="+Constants.FB_SERVER_KEY
            , "Content-Type:" + Constants.CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> sendPushNotification(@Body PushNotification notification);
}

package com.parovi.zadruga.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.activities.MainActivity;
import com.parovi.zadruga.R;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.retrofit.UserApi;

import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationService extends FirebaseMessagingService {

    private final String channelId = "zadrugaChannel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.i("izServisa", "uso sam");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        boolean isChannelCreated = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE).getBoolean(Constants.IS_CHANNEL_CREATED_TAG, false);
        if(!isChannelCreated){
            createChannel(manager);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        message.getNotification();
        manager.notify((new Random()).nextInt(), notification);
    }

    private void createChannel(NotificationManager manager){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, "Zadruga channel", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            manager.createNotificationChannel(channel);
            SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE).edit();
            editor.putBoolean(Constants.IS_CHANNEL_CREATED_TAG, true);
            editor.apply();
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.FCM_TOKEN_TAG, s);
        editor.apply();

        Retrofit backend = new Retrofit.Builder()
                .baseUrl(Constants.ZADRUGA_API_BASE_URL)
                .build();
        UserApi userApi;
        userApi = backend.create(UserApi.class);
        User user = new User();
        user.setFcmToken(s);
        userApi.updateUser(sp.getString(Constants.ACCESS_TOKEN, ""), sp.getInt(Constants.LOGGED_USER_ID, -1), user);
    }
}

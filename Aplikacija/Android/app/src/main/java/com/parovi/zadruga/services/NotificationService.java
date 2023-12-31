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
import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.R;
import com.parovi.zadruga.activities.MainEmployerActivity;
import com.parovi.zadruga.activities.MainStudentActivity;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.models.requestModels.AddFcmTokenRequest;
import com.parovi.zadruga.ui.ChatActivity;
import com.parovi.zadruga.ui.JobAdActivity;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class NotificationService extends FirebaseMessagingService {

    private final String channelId = "zadrugaChannel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.i("izServisa", "uso sam");
        Intent intent = new Intent();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        boolean isChannelCreated = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE).getBoolean(Constants.IS_CHANNEL_CREATED_TAG, false);
        if(!isChannelCreated){
            createChannel(manager);
        }
        String title = "", body = "";
        Map<String, String> data = message.getData();
        String type = data.get("type");
        if(type != null){
            switch (type) {
                case Constants.NOTIF_ACCEPTED:
                    title = getString(R.string.accepted);
                    body = data.get("adTitle");
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(ChatActivity.CHAT_QB_ID, data.get("chatQbId"));
                    break;
                case Constants.NOTIF_DECLINED:
                    title = getString(R.string.declined);
                    body = data.get("adTitle");
                    intent = new Intent(this, JobAdActivity.class);
                    break;
                case Constants.NOTIF_TAGGED:
                    title = getString(R.string.tagged, "@" + data.get("username"));
                    body = data.get("comment");
                    intent = new Intent(this, JobAdActivity.class);
                    break;
                case Constants.NOTIF_AD_COMMENT:
                    title = getString(R.string.adComment, "@" + data.get("username"));
                    body = data.get("comment");
                    intent = new Intent(this, JobAdActivity.class);
                    break;
                case Constants.NOTIF_RATING:
                    title = getString(R.string.notifRating, "@" + data.get("rater"));
                    body = getString(R.string.rating, data.get("rating")) + " " + data.get("comment");
                    if (Utility.getLoggedInUser(App.getAppContext()).isEmployer())
                        intent = new Intent(this, MainEmployerActivity.class);
                    else
                        intent = new Intent(this, MainStudentActivity.class);
                    break;
                case Constants.NOTIF_CHAT:
                    title = getString(R.string.notifChat, "@" + data.get("username"));
                    body = data.get("message");
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(ChatActivity.CHAT_QB_ID, data.get("chatQbId"));
                    break;
                case Constants.NOTIF_APPLIED:
                    title = getString(R.string.notifApplied, "@" + data.get("username"));
                    body = data.get("title");
                    intent = new Intent(this, JobAdActivity.class);
                    intent.putExtra(JobAdActivity.AD_ID, data.get("adId"));
                    break;
            }
        }
        if(data.get("adId") != null)
            intent.putExtra(JobAdActivity.AD_ID, Integer.valueOf(data.get("adId")));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.app_logo)
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
        Utility.getExecutorService().execute(() -> {
            String accessToken = "";
            String oldFcmToken = Utility.getFcmToken(App.getAppContext());
            if(!(accessToken = Utility.getAccessToken(App.getAppContext())).equals("")){
                try {
                    Response<ResponseBody> res = ApiFactory.getUserApi().postFcmToken(accessToken, new AddFcmTokenRequest(oldFcmToken, s)).execute();
                    if(res.isSuccessful())
                        Log.i("onNewToken", "Fcm token successfully changed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

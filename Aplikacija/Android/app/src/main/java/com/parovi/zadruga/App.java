package com.parovi.zadruga;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.LogLevel;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App extends Application {
    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        initQuickBlox();
        appContext = getApplicationContext();
        getFcmToken();
    }

    private void getFcmToken() {

    }

    private void initQuickBlox(){
        QBSettings.getInstance().init(getApplicationContext(), Constants.APPLICATION_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
        QBSettings.getInstance().setLogLevel(LogLevel.DEBUG);
        QBChatService.setDebugEnabled(true);

        QBChatService.ConfigurationBuilder configurationBuilder = new QBChatService.ConfigurationBuilder();
        configurationBuilder.setAutojoinEnabled(true);
        configurationBuilder.setReconnectionAllowed(true);

        QBChatService.setConfigurationBuilder(configurationBuilder);

    }

    public static Context getAppContext(){
        return appContext;
    }
}

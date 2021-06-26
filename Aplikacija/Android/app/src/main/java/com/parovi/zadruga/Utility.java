package com.parovi.zadruga;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.type.DateTime;
import com.parovi.zadruga.models.entityModels.User;
import com.quickblox.auth.session.QBSettings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Utility {
    public enum ChatType{ PRIVATE, GROUP }
    private static ExecutorService executorService;
    private static Executor executor;

    static public int getLoggedInUserId(Context c){
        User u;
        if((u = getLoggedInUser(c)) != null)
            return u.getUserId();
        return -1;
    }

    static public int getLoggedInUserQbId(Context c){
        User u;
        if((u = getLoggedInUser(c)) != null)
            return u.getUserQbId();
        return -1;
    }

    static public String getLoggedInUserPassword(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getString(Constants.PASSWORD_TOKEN, "");
    }

    static public String getFcmToken(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getString(Constants.FCM_TOKEN_TAG, null);
    }

    static public void setLoggedInUserPassword(Context c, String newPass){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.PASSWORD_TOKEN, newPass);
        editor.apply();
    }

    static public String getAccessToken(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getString(Constants.ACCESS_TOKEN, "");
    }

    static public void saveLoggedUserInfo(Context c, String accessToken, User user, String pass, String fcmToken){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.ACCESS_TOKEN, accessToken);
        editor.putString(Constants.PASSWORD_TOKEN, pass);
        editor.putString(Constants.FCM_TOKEN_TAG, fcmToken);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constants.LOGGED_IN_USER, json);
        editor.apply();
    }

    static public void removeLoggedUserInfo(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Constants.FCM_TOKEN_TAG);
        editor.remove(Constants.ACCESS_TOKEN);
        editor.remove(Constants.PASSWORD_TOKEN);
        editor.remove(Constants.LOGGED_IN_USER);
        editor.apply();
    }


    static public User getLoggedInUser(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Constants.LOGGED_IN_USER, "");
        return gson.fromJson(json, User.class);
    }

    public static ExecutorService getExecutorService(){
        if(executorService == null){
            executorService = Executors.newFixedThreadPool(4);
        }
        return executorService;
    }

    public static Executor getExecutor(){
        if(executor == null){
            executor = MoreExecutors.directExecutor();
        }
        return executor;
    }

    /*static private void checkIfFcmTokenTagIsChanged(Context c){
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String token = "";
        if((token = sp.getString(Constants.FCM_TOKEN_TAG, null)) != null){
            //TODO: Ovu funkciju treba da poziva neki viewModel
            User u = new User();
            u.setUserId(1);
            u.setFcmToken(token);
            rep.updateUserFcmToken(u).observe(this, new Observer<Integer>() { //TODO: ovde treba da stoji ulogovani user, a ne ovaj
                @Override
                public void onChanged(Integer rowsChanged) {
                    if(rowsChanged != -1){
                        Log.w("fcmTokenWarning", "Token nije promenjen");
                    }
                }
            });
            editor.putString(Constants.FCM_TOKEN_TAG, null);
        }
        editor.apply();
    }*/
    public static String dateTimeToTimeAgo(DateTime dt){
        /*DateTime now = new DateTime();
        long minutes= TimeUnit.MILLISECONDS.toMinutes(LocalDate.now() - past.getTime());
        long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

        if(seconds<60)
        {
            System.out.println(seconds+" seconds ago");
        }
        else if(minutes<60)
        {
            System.out.println(minutes+" minutes ago");
        }
        else if(hours<24)
        {
            System.out.println(hours+" hours ago");
        }
        else
        {
            System.out.println(days+" days ago");
        }*/
        return "";
    }
}

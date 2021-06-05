package com.parovi.zadruga;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.google.type.DateTime;
import com.parovi.zadruga.models.entityModels.User;
import com.quickblox.auth.session.QBSettings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Utility {
    public enum ChatType{ PRIVATE, GROUP }
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

    static public int getUserId(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(Constants.LOGGED_USER_ID, -1);
    }

    static public String getAccessToken(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getString(Constants.ACCESS_TOKEN, "");
    }

    static public void saveLoggedUserInfo(Context c, int id, int qbId, String token){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constants.LOGGED_USER_ID, id);
        editor.putInt(Constants.LOGGED_USER_QB_ID, qbId);
        editor.putString(Constants.ACCESS_TOKEN, token);
        editor.apply();
    }

    static public void removeLoggedUserInfo(Context c){
        SharedPreferences sp = c.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Constants.LOGGED_USER_ID);
        editor.remove(Constants.LOGGED_USER_QB_ID);
        editor.remove(Constants.ACCESS_TOKEN);
        editor.apply();
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
}

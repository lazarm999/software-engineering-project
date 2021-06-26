package com.parovi.zadruga.factories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.api.AdApi;
import com.parovi.zadruga.api.CommentApi;
import com.parovi.zadruga.api.LookupApi;
import com.parovi.zadruga.api.NotificationApi;
import com.parovi.zadruga.api.RatingApi;
import com.parovi.zadruga.api.UserApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static Retrofit backend;
    private static UserApi userApi;
    private static AdApi adApi;
    private static LookupApi lookupApi;
    private static CommentApi commentApi;
    private static RatingApi ratingApi;
    private static NotificationApi notificationApi;

    private static Retrofit getBackend(){
        if(backend == null){
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
            backend = new Retrofit.Builder()
                    .baseUrl(Constants.ZADRUGA_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return backend;
    }

    public static UserApi getUserApi(){
        if(userApi == null){
            userApi = getBackend().create(UserApi.class);
        }
        return userApi;
    }

    public static AdApi getAdApi(){
        if(adApi == null){
            adApi = getBackend().create(AdApi.class);
        }
        return adApi;
    }

    public static LookupApi getLookupApi(){
        if(lookupApi == null){
            lookupApi = getBackend().create(LookupApi.class);
        }
        return lookupApi;
    }

    public static CommentApi getCommentApi(){
        if(commentApi == null){
            commentApi = getBackend().create(CommentApi.class);
        }
        return commentApi;
    }

    public static RatingApi getRatingApi(){
        if(ratingApi == null){
            ratingApi = getBackend().create(RatingApi.class);
        }
        return ratingApi;
    }
}

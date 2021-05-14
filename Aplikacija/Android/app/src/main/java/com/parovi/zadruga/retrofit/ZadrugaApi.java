package com.parovi.zadruga.retrofit;

import androidx.lifecycle.LiveData;

import com.parovi.zadruga.Constants;
import com.parovi.zadruga.PushNotification;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.TmpPost;
import com.parovi.zadruga.models.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*
ako hoces da neki parametar ne prosledis samo na njegovo mesto prosledi null,
zbog toga ne treba koristiti primitivne tipove jer oni ne mogu da budu null*/
public interface ZadrugaApi{

    @GET("user/{id}")
    LiveData<User> getUserByIdPath(@Path("id") int id);

    @GET("posts")
    Call<List<TmpPost>> getPosts();

    @GET("ads")
    Call<List<Ad>> getAllAds();

    @GET("user")
    Call<User> getUserByIdQuery(@Query("id") int id);//user?id=id

    @GET("user")
    Call<User> getUsersByListOfIdsQuery(@Query("id") List<Integer> id);

    @GET("user")
    Call<User> getUsersWithMap(@QueryMap Map<String, String> parameters);//key, value parovi parametara

    @GET("user")
    Call<List<User>> getAllUsers();

    @POST()
    Call<User> createUser(@Body User u);

    @PATCH
    Call<Integer> patchUser(@Body User u);
}

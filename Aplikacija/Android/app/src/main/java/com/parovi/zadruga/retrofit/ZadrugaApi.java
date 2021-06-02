package com.parovi.zadruga.retrofit;


import com.parovi.zadruga.models.entityModels.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*
ako hoces da neki parametar ne prosledis samo na njegovo mesto prosledi null,
zbog toga ne treba koristiti primitivne tipove jer oni ne mogu da budu null*/
public interface ZadrugaApi extends UserApi {

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
}

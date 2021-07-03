package com.parovi.zadruga.api;

import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RatingApi {

    @POST("rate/{id}/")
    Call<RatingResponse> postRating(@Header("Authorization") String token, @Path("id") int rateeId, @Body Rating rating);

    @PUT("rate/{id}/")
    Call<RatingResponse> editRating(@Header("Authorization") String token, @Path("id") int id, @Body Rating rating);

    @GET("ratings/{id}/")
    Call<List<RatingResponse>> getRatingByRateeId(@Header("Authorization") String token, @Path("id") int id);

    @DELETE("rate/{id}/")
    Call<ResponseBody> deleteRating(@Header("Authorization") String token, @Path("id") int id);

    @GET("hasRated/{id}/")
    Call<Boolean> hasRated(@Header("Authorization") String token, @Path("id") int rateeId);
}

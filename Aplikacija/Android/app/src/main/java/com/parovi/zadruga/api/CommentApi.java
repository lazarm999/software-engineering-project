package com.parovi.zadruga.api;

import com.parovi.zadruga.models.requestModels.CommentRequest;
import com.parovi.zadruga.models.responseModels.AdResponse;
import com.parovi.zadruga.models.responseModels.CommentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApi {
    @GET("comments/{id}/")
    Call<List<CommentResponse>> getComments(@Header("Authorization") String token, @Path("id") int id);

    @POST("comments/{id}/")
    Call<CommentResponse> postComment(@Header("Authorization") String token, @Path("id") int adId, @Body CommentRequest comment);

    @DELETE("comment/{id}/")
    Call<AdResponse> deleteComment(@Header("Authorization") String token, @Path("id") int commentId);
}

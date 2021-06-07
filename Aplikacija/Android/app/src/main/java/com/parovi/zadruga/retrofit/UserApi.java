package com.parovi.zadruga.retrofit;

import androidx.room.Delete;

import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.requestModels.BanRequest;
import com.parovi.zadruga.models.requestModels.ChangePasswordRequest;
import com.parovi.zadruga.models.responseModels.LoginResponse;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {
    @POST("register/")
    Call<User> registerUser(@Body User user);

    @POST("login/")
    Call<LoginResponse> loginUser(@Body User user);

    @GET("user/{id}/")
    Call<User> getUserById(@Header("Authorization") String token, @Path("id") int id);

    @PATCH("user/{id}/")
    Call<Void> updateUser(@Header("Authorization") String token, @Path("id") int id, @Body User user);

    @GET("profilePicture/{id}/")
    Call<ResponseBody> getProfileImage(@Header("Authorization") String token, @Path("id") int id);

    @PUT("passwordChange/")
    Call<ResponseBody> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequest request);

    @Multipart
    @POST("profilePicture/{id}/")
    Call<ResponseBody> postProfilePicture(@Header("Authorization") String token, @Path("id") int id, @Part MultipartBody.Part image);

    @POST("ban/{id}/")
    Call<ResponseBody> banUser(@Header("Authorization") String token, @Path("id") int id, @Body BanRequest banRequest);

    @POST("unban/{id}/")
    Call<ResponseBody> unBanUser(@Header("Authorization") String token, @Path("id") int id);


    @GET("/apply/{id}")
    Call<List<User>> getAppliedUsers(@Header("Authorization") String token, @Path("id") int adId);
}

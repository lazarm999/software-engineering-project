package com.parovi.zadruga.api;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.requestModels.BanRequest;
import com.parovi.zadruga.models.requestModels.ChangePasswordRequest;
import com.parovi.zadruga.models.requestModels.ChatMembersRequest;
import com.parovi.zadruga.models.requestModels.AddFcmTokenRequest;
import com.parovi.zadruga.models.requestModels.LoginRequest;
import com.parovi.zadruga.models.requestModels.ReportRequest;
import com.parovi.zadruga.models.responseModels.LoginResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @POST("register/")
    Call<User> registerUser(@Body User user);

    @POST("login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("user/{id}/")
    Call<User> getUserById(@Header("Authorization") String token, @Path("id") int id);

    @GET("userByQbUserId/{id}/")
    Call<User> getUserByQbUserId(@Header("Authorization") String token, @Path("id") int qbId);

    @PATCH("user/{id}/")
    Call<Void> updateUser(@Header("Authorization") String token, @Path("id") int id, @Body User user);

    @GET("profilePicture/{id}/")
    Call<ResponseBody> getProfileImage(@Header("Authorization") String token, @Path("id") int id);

    @GET("profilePictureQb/{id}/")
    Call<ResponseBody> getProfilePictureByUserQbId(@Header("Authorization") String token, @Path("id") int userQbId);

    @PUT("passwordChange/")
    Call<ResponseBody> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequest request);

    @Headers({
            "Content-Disposition: attachment; filename=default.png"
    })
    @POST("profilePicture/{id}/")
    Call<ResponseBody> postProfilePicture(@Header("Authorization") String token, @Path("id") int id, @Body RequestBody image);

    @POST("ban/{id}/")
    Call<ResponseBody> banUser(@Header("Authorization") String token, @Path("id") int id, @Body BanRequest banRequest);

    @POST("unban/{id}/")
    Call<ResponseBody> unBanUser(@Header("Authorization") String token, @Path("id") int id);

    @GET("apply/{id}/")
    Call<List<User>> getAppliedUsers(@Header("Authorization") String token, @Path("id") int adId);

    @POST("chatMembers/")
    Call<List<User>> getChatMembers(@Header("Authorization") String token, @Body ChatMembersRequest userQbIds);

    @GET("userAds/{id}/")
    Call<List<Ad>> getUserAds(@Header("Authorization") String token, @Path("id") int userId);

    @POST("userFcm/")
    Call<ResponseBody> postFcmToken(@Header("Authorization") String token, @Body AddFcmTokenRequest request);

    @DELETE("userFcm/{fcmToken}/")
    Call<ResponseBody> deleteFcmToken(@Header("Authorization") String token, @Path("fcmToken") String fcmToken);

    @POST("report/")
    Call<ResponseBody> postReport(@Header("Authorization") String token, @Body ReportRequest request);
}

package com.parovi.zadruga.retrofit;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.requestModels.EditAdRequest;
import com.parovi.zadruga.models.requestModels.FilterAndSortRequest;
import com.parovi.zadruga.models.responseModels.AdResponse;
import com.parovi.zadruga.models.requestModels.ChooseApplicantsRequest;
import com.parovi.zadruga.models.requestModels.PostAdRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AdApi {
    @GET("ad/{id}/")
    Call<Ad> getAd(@Header("Authorization") String token, @Path("id") int id);

    @GET("adByQbChatId/{id}/")
    Call<Ad> getAdByQbChatId(@Header("Authorization") String token, @Path("id") String qbChatId);

    @POST("ad/")
    Call<Ad> postAd(@Header("Authorization") String token, @Body PostAdRequest ad);

    @GET("ad/")
    Call<List<Ad>> getAds(@Header("Authorization") String token);

    @GET("ad/")
    Call<List<Ad>> getAds(@Header("Authorization") String token,
                          @Query("filterLocationId") Integer locId, @Query("filterCompensationMin") Integer compensationMin,
                          @Query("filterCompensationMax") Integer compensationMax, @Query("filterTagIds") List<Integer> tagIds,
                          @Query("sortLocationLatitude") Double locLatitude, @Query("sortLocationLongitude") Double locLongitude);

    @POST("apply/{id}/")
    Call<ResponseBody> applyForAd(@Header("Authorization") String token, @Path("id") int adId);

    @DELETE("apply/{id}/")
    Call<ResponseBody> unApplyForAd(@Header("Authorization") String token, @Path("id") int adId);

    @POST("choose/{id}/")
    Call<ResponseBody> chooseApplicants(@Header("Authorization") String token, @Path("id") int adId, @Body ChooseApplicantsRequest chosenApplicants);

    @PATCH("ad/{id}/")
    Call<ResponseBody> editAd(@Header("Authorization") String token, @Path("id") int adId, @Body EditAdRequest ad);

    @DELETE("ad/{id}/")
    Call<ResponseBody> deleteAd(@Header("Authorization") String token, @Path("id") int adId);

}

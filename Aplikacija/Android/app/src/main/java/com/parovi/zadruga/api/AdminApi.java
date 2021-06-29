package com.parovi.zadruga.api;

import com.parovi.zadruga.models.entityModels.Report;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface AdminApi {

    @GET("report/")
    Call<List<Report>> getReports(@Header("Authorization") String token);

    @DELETE("report/{id}/")
    Call<ResponseBody> deleteReport(@Header("Authorization") String token, @Path("id") int reportId);

}

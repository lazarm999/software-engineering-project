package com.parovi.zadruga.api;

import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.University;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LookupApi {

    @GET("universityList/")
    Call<List<University>> getAllUniversities(@Header("Authorization") String token);

    @GET("facultyList/")
    Call<List<Faculty>> getAllFaculties(@Header("Authorization") String token);

    @GET("badgeList/")
    Call<List<Badge>> getAllBadges(@Header("Authorization") String token);

    @GET("locationList/")
    Call<List<Location>> getAllLocations(@Header("Authorization") String token);

    @GET("tagList/")
    Call<List<Tag>> getAllTags(@Header("Authorization") String token);
}

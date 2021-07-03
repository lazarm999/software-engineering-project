package com.parovi.zadruga.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingRepository extends BaseRepository {
    public RatingRepository(){ super(); }

    public void postRating(String token, MutableLiveData<CustomResponse<?>> isRated, Rating rating){
        ApiFactory.getRatingApi().postRating(token, rating.getFkRateeId(), rating).enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(@NotNull Call<RatingResponse> call, @NotNull Response<RatingResponse> response) {
                if(response.isSuccessful()){
                    Log.i("postRating", "onResponse:");
                    isRated.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            DaoFactory.getRatingDao().insertOrUpdate(rating);
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), isRated);
            }

            @Override
            public void onFailure(@NotNull Call<RatingResponse> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isRated);
            }
        });
    }

    public void editRating(String token, MutableLiveData<CustomResponse<?>> res, Rating rating){
        ApiFactory.getRatingApi().editRating(token, rating.getFkRateeId(), rating).enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(@NotNull Call<RatingResponse> call, @NotNull Response<RatingResponse> response) {
                if(response.isSuccessful()){
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            DaoFactory.getRatingDao().insertOrUpdate(rating);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<RatingResponse> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), res);
            }
        });
    }

    public void getRatingByUserId(String token, MutableLiveData<CustomResponse<?>> ratings, int rateeId){
        final Boolean[] isSynced = {false};
        getRatingByUserIdLocal(ratings, rateeId, isSynced);
        ApiFactory.getRatingApi().getRatingByRateeId(token, rateeId).enqueue(new Callback<List<RatingResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<RatingResponse>> call, @NotNull Response<List<RatingResponse>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        synchronized (isSynced[0]){
                            ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                            isSynced[0] = true;
                        }
                        Utility.getExecutorService().execute(() -> {
                            for (RatingResponse r: response.body()) {
                                saveRatingLocally(r);
                            }
                        });
                    } else {
                        ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<RatingResponse>()));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RatingResponse>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), ratings);
            }
        });
    }

    private void getRatingByUserIdLocal(MutableLiveData<CustomResponse<?>> ratings, int rateeId, Boolean[] isSynced) {
        Futures.addCallback(DaoFactory.getRatingDao().getRatingByRatedId(rateeId), new FutureCallback<List<RatingResponse>>() {
            @Override
            public void onSuccess(@Nullable List<RatingResponse> result) {
                synchronized (isSynced[0]){
                    if(result == null){
                        ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<RatingResponse>()));
                    } else {
                        ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, result));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
            }
        }, Utility.getExecutor());
    }

    public void deleteRating(String token, MutableLiveData<CustomResponse<?>> isDeleted, int raterId, int rateeId){
        ApiFactory.getRatingApi().deleteRating(token, rateeId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    DaoFactory.getRatingDao().deleteRating(raterId, rateeId);
                } else
                    responseNotSuccessful(response.code(), isDeleted);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isDeleted);
            }
        });
    }

    public void hasRated(MutableLiveData<CustomResponse<?>> hasRated, Integer rateeId){
        String token = Utility.getAccessToken(App.getAppContext());
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Boolean> hasRatedRes = ApiFactory.getRatingApi().hasRated(token, rateeId).execute();
                    if(hasRatedRes.isSuccessful()){
                        hasRated.postValue(new CustomResponse<>(CustomResponse.Status.OK, hasRatedRes.body()));
                    } else
                        responseNotSuccessful(hasRatedRes.code(), hasRated);
                } catch (IOException e) {
                    e.printStackTrace();
                    apiCallOnFailure(e.getMessage(), hasRated);
                }
            }
        });
    }
}

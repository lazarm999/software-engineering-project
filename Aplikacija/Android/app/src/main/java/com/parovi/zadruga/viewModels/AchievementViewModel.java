package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.repository.RatingRepository;

public class AchievementViewModel extends AndroidViewModel {

    private int userId = Utility.getLoggedInUser(App.getAppContext()).getUserId();

    MutableLiveData<CustomResponse<?>> ratings;
    MutableLiveData<CustomResponse<?>> isRated;
    MutableLiveData<CustomResponse<?>> hasRated;

    RatingRepository ratingRepository;

    public AchievementViewModel(@NonNull Application application) {
        super(application);
        ratingRepository = new RatingRepository();
        ratings = new MutableLiveData<>();
        isRated = new MutableLiveData<>();
        hasRated = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> getRatings() {
        return ratings;
    }

    public MutableLiveData<CustomResponse<?>> getIsRated() {
        return isRated;
    }

    public MutableLiveData<CustomResponse<?>> getHasRated() {
        return hasRated;
    }

    public void loadRatings() {
        ratingRepository.getRatingByUserId(Utility.getAccessToken(App.getAppContext()), ratings, userId);
    }

    public void rateUser(Rating rating) {
        ratingRepository.postRating(Utility.getAccessToken(App.getAppContext()), isRated, rating);
    }

    public void loadUser(int userId) {
        if (userId < 0)
            this.userId = Utility.getLoggedInUserId(App.getAppContext());
        else
            this.userId = userId;
    }

    public void hasRated(Integer rateeId){
        ratingRepository.hasRated(hasRated, rateeId);
    }
}

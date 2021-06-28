package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.repository.AdRepository;

public class RecommendViewModel extends AndroidViewModel {
    MutableLiveData<CustomResponse<?>> ads;//All ads

    AdRepository adRepository;

    public RecommendViewModel(@NonNull Application application) {
        super(application);
        adRepository = new AdRepository();
        ads = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> getAds() {  return ads; }

    public void loadRecommended() { adRepository.getRecommendedAds(ads); }
}

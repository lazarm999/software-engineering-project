package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.repository.BaseRepository;

import org.jetbrains.annotations.NotNull;

public class NewAdViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isPosted;
    private BaseRepository rep;
    public NewAdViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<CustomResponse<?>> getIsPosted(){
        return isPosted;
    }

    public void postAd(String token, PostAdRequest ad){
    }
}

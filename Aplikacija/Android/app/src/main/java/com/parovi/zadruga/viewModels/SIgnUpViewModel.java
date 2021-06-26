package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.repository.BaseRepository;

public class SIgnUpViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isSignedUp;
    private BaseRepository rep;

    public SIgnUpViewModel(@NonNull Application app) {
        super(app);
        isSignedUp = new MutableLiveData<>();
    }

    public LiveData<Boolean> observeIsSignedUp() {
        return isSignedUp;
    }
}

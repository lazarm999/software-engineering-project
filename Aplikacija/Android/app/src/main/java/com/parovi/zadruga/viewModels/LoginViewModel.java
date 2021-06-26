package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.repository.BaseRepository;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoggedIn;
    private BaseRepository rep;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        isLoggedIn = new MutableLiveData<>();
    }

    public LiveData<Boolean> observeIsLoggedIn() {
        return isLoggedIn;
    }
}

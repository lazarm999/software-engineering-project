package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoggedIn;
    private ZadrugaRepository rep;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        rep = ZadrugaRepository.getInstance(app);
        isLoggedIn = new MutableLiveData<>();
    }

    public LiveData<Boolean> observeIsLoggedIn() {
        return isLoggedIn;
    }

    public void logInQBUser(User u, String pass) {
        rep.logInQBUser(isLoggedIn, u, pass);
    }
}

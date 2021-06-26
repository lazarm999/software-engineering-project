package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isEmployer;
    private ZadrugaRepository rep;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        rep = ZadrugaRepository.getInstance(app);
        isEmployer = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> getIsEmployer() {
        return isEmployer;
    }

    public void loginUser(String email, String pass)
    {
        //rep.loginUser(isEmployer, email, pass);
        rep.loginUser(isEmployer, new User(email, pass));
    }
}

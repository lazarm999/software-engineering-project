package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isEmployer;
    private UserRepository userRepository;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        userRepository = new UserRepository();
        isEmployer = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> getIsEmployer() {
        return isEmployer;
    }

    public void loginUser(String email, String pass)
    {
        userRepository.loginUser(isEmployer, email, pass);
    }
}

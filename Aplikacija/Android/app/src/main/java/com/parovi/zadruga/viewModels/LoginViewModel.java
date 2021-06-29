package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> userType;
    private UserRepository userRepository;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        userRepository = new UserRepository();
        userType = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> getUserType() {
        return userType;
    }

    public void loginUser(String email, String pass)
    {
        userRepository.loginUser(userType, email, pass);
    }
}

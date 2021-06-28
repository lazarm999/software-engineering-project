package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.UserRepository;

public class SIgnUpViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isSignedUp;
    private UserRepository userRepository;

    public SIgnUpViewModel(@NonNull Application app) {
        super(app);
        isSignedUp = new MutableLiveData<>();
        userRepository = new UserRepository();
    }

    public MutableLiveData<CustomResponse<?>> getIsSignedUp() {
        return isSignedUp;
    }

    public void signUp(User user, String  pass)
    {
        userRepository.registerUser(isSignedUp, user, pass);
    }
}

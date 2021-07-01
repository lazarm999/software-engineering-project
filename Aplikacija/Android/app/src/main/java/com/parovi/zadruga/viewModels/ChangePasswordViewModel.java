package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.repository.UserRepository;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordViewModel extends AndroidViewModel {

    private MutableLiveData<CustomResponse<?>> isPassChanged;
    private UserRepository userRepository;

    public ChangePasswordViewModel(@NonNull @NotNull Application application) {
        super(application);
        isPassChanged = new MutableLiveData<>();
        userRepository = new UserRepository();
    }

    public MutableLiveData<CustomResponse<?>> getIsPassChanged() {
        return isPassChanged;
    }

    public void changePass(String newPass)
    {
        userRepository.changePassword(Utility.getAccessToken(App.getAppContext()), isPassChanged,
                Utility.getLoggedInUserQbId(App.getAppContext()),
                Utility.getLoggedInUserPassword(App.getAppContext()), newPass);
    }
}

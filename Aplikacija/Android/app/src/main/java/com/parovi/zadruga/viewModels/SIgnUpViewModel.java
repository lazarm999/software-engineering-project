package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.quickblox.chat.model.QBChatDialog;

import java.util.List;

public class SIgnUpViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isSignedUp;
    private ZadrugaRepository rep;

    public SIgnUpViewModel(@NonNull Application app) {
        super(app);
        rep = ZadrugaRepository.getInstance(app);
        isSignedUp = new MutableLiveData<>();
    }

    public LiveData<Boolean> observeIsSignedUp() {
        return isSignedUp;
    }
}

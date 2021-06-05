package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;

import java.util.List;

public class TmpViewModel extends AndroidViewModel {
    private MutableLiveData<List<Ad>> ads;
    private LiveData<List<Location>> locations;
    private ZadrugaRepository rep;

    private MutableLiveData<User> newUser;

    public TmpViewModel(@NonNull Application app) {
        super(app);
        rep = ZadrugaRepository.getInstance(app);
        newUser = new MutableLiveData<>();
    }

    public LiveData<User> observeNewUser() {
        return newUser;
    }
}

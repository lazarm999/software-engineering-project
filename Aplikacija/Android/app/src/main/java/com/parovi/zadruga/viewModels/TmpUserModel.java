package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.parovi.zadruga.models.entityModels.AdWithLocation;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.BaseRepository;

import java.util.List;

public class TmpUserModel extends AndroidViewModel {
    private LiveData<List<Badge>> badges;
    private LiveData<List<AdWithLocation>> adsWithLocation;
    private LiveData<User> user;
    private BaseRepository rep;

    public TmpUserModel(@NonNull Application application) {
        super(application);
    }
}

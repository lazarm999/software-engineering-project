package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;

public class StudentProfileViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> badges;
    public StudentProfileViewModel(@NonNull Application application) {
        super(application);
    }
}

package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.data.UserInfo;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserProfileViewModel extends AndroidViewModel {
    private final int id = 3;
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g";
    private MutableLiveData<CustomResponse<?>> userInfo;
    private MutableLiveData<CustomResponse<?>> profileImage;
    private MutableLiveData<CustomResponse<?>> isUpdated;
    private ZadrugaRepository repository;

    public UserProfileViewModel(@NonNull Application app) {
        super(app);
        repository = ZadrugaRepository.getInstance(app);
        userInfo = new MutableLiveData<>();
        profileImage = new MutableLiveData<>();
        isUpdated = new MutableLiveData<>();
        loadUserInfo();
        loadUserProfileImage();
    }

    public MutableLiveData<CustomResponse<?>> getIsUpdated() {
        return isUpdated;
    }

    public MutableLiveData<CustomResponse<?>> getProfileImage() {
        return profileImage;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public MutableLiveData<CustomResponse<?>> getUserInfo() {
        return userInfo;
    }

    public void updateUser() {
        repository.updateUser(token, isUpdated, (User)userInfo.getValue().getBody());
    }

    private void loadUserInfo() {
        repository.getUserById(token, userInfo, id);
    }
    private void loadUserProfileImage() {
        repository.getProfilePicture(token, profileImage, id);
    }
}

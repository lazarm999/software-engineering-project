package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class EmployerProfileViewModel extends AndroidViewModel {
    private final int userId = 1;
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";

    private MutableLiveData<CustomResponse<?>> user;
    private MutableLiveData<CustomResponse<?>> badges;
    private MutableLiveData<CustomResponse<?>> userInfo;
    private MutableLiveData<CustomResponse<?>> profilePicture;
    private UserRepository userRepository;
    private LookUpRepository lookUpRepository;

    public EmployerProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        lookUpRepository = new LookUpRepository();
        userInfo = new MutableLiveData<>();
        user = new MutableLiveData<>();
        badges = new MutableLiveData<>();
        profilePicture = new MutableLiveData<>();
        loadUserInfo();
        loadUserProfileImage();
        loadBadges();
    }

    public MutableLiveData<CustomResponse<?>> getBadges() {
        return badges;
    }

    public MutableLiveData<CustomResponse<?>> getProfilePicture() {
        return profilePicture;
    }

    public MutableLiveData<CustomResponse<?>> getThisUser() {
        return user;
    }

    private void loadBadges() {
        lookUpRepository.getAllBadges(token, badges);
    }

    public void loadUserProfileImage() {
        userRepository.getProfilePicture(profilePicture, userId);
    }

    public void loadUserInfo() {
        userRepository.getUserById(token, userInfo, userId);
    }

    public MutableLiveData<CustomResponse<?>> getUserInfo() {
        return userInfo;
    }

    public List<String> getBadgeNames()
    {
        List<Badge> badge = (List<Badge>)badges.getValue().getBody();
        List<String> strings = new ArrayList<String>();
        if (badge == null)
            return strings;
        for (Badge b : badge)
            strings.add(b.getDescription());
        return strings;
    }

}
package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class EmployerProfileViewModel extends AndroidViewModel {
    private int userId;
    private String TOKEN = Utility.getAccessToken(App.getAppContext());

    private MutableLiveData<CustomResponse<?>> badges;
    private MutableLiveData<CustomResponse<?>> isBanned;
    private MutableLiveData<CustomResponse<?>> user;
    private MutableLiveData<CustomResponse<?>> isLogedOut;
    private MutableLiveData<CustomResponse<?>> profilePicture;
    private UserRepository userRepository;
    private LookUpRepository lookUpRepository;

    public EmployerProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        lookUpRepository = new LookUpRepository();
        user = new MutableLiveData<>();
        isLogedOut = new MutableLiveData<>();
        badges = new MutableLiveData<>();
        profilePicture = new MutableLiveData<>();
        isBanned = new MutableLiveData<>();
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

    public MutableLiveData<CustomResponse<?>> getIsLogedOut() {
        return isLogedOut;
    }

    public MutableLiveData<CustomResponse<?>> getIsBanned() {
        return isBanned;
    }

    public void logOut() { userRepository.logOutUser(isLogedOut, false); }

    public int getId() {
        return userId;
    }

    public String getToken() {
        return TOKEN;
    }

    public void banUser(){
        userRepository.banUser(TOKEN, isBanned, ((User)user.getValue().getBody()).getUserId());
    }

    public void loadUser(int userId) {
        if (userId > -1)
            this.userId = userId;
        else
            this.userId = Utility.getLoggedInUser(App.getAppContext()).getUserId();
        loadUserInfo();
        loadBadges();
        loadUserProfileImage();
    }

    private void loadBadges() {
        lookUpRepository.getAllBadges(TOKEN, badges);
    }

    public void loadUserProfileImage() {
        userRepository.getProfilePicture(profilePicture, userId);
    }

    public void loadUserInfo() {
        userRepository.getUserById(TOKEN, user, userId);
    }

    public MutableLiveData<CustomResponse<?>> getUser() {
        return user;
    }

    public List<Integer> getBadgeIds(List<Badge> badge)
    {
        List<Integer> ids = new ArrayList<>();
        if (badge == null)
            return ids;
        for (Badge b : badge)
            ids.add(b.getBadgeId());
        return ids;
    }

    public String getBadgeDesc(int id)
    {
        List<Badge> badge = (List<Badge>)badges.getValue().getBody();
        return badge.get(id).getDescription();
    }

    public boolean gotTheBadge(int id)
    {
        boolean bool = false;
        List<Badge> badge = (List<Badge>)badges.getValue().getBody();

        if(badge.get(id) != null)

        bool = true;

        return bool;
    }
//    public void load(int userId) {
//        USER_ID = userId;
//        loadUserInfo();
//    }
//
//    public void loadCredentials() {
//        USER_ID = Utility.getLoggedInUserId(App.getAppContext());
//        TOKEN = Utility.getAccessToken(App.getAppContext());
//    }
}

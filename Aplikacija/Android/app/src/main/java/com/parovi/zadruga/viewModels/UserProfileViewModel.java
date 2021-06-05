package com.parovi.zadruga.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parovi.zadruga.data.UserInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {
    private final static long id = 3;
    private MutableLiveData<UserInfo> userInfo;

    public MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo == null) {
            userInfo = new MutableLiveData<UserInfo>();
            loadUserInfo();
        }
        return userInfo;
    }

    private void loadUserInfo() {
        UserInfo userInfo = new UserInfo(id,"@urosst_" );
        userInfo.setFirstName("Uros");
        userInfo.setLastName("Stojkovic");
        userInfo.setEmail("uros.stojkovich@elfak.rs");
        userInfo.setPhoneNo("+381349309");
        List<Long> jobTypes = new LinkedList<Long>();
        jobTypes.add((long) 2);
        jobTypes.add((long) 5);
        userInfo.setJobTypes(jobTypes);
        this.userInfo.setValue(userInfo);
    }


}

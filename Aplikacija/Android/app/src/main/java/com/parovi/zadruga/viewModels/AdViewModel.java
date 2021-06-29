package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.data.JobAdInfo;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.AdRepository;
import com.parovi.zadruga.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;

public class AdViewModel extends AndroidViewModel {
    private int adId = 32; //4
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NH0.0SXnyerqRnP7HxklZwQ5k0q0XGYiMeEE2eTNixAxNrU";// "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
    private int userId = 4;//Utility.getLoggedInUserId(App.getAppContext());

    MutableLiveData<JobAdInfo> jobAd;

    MutableLiveData<CustomResponse<?>> ad;//AdWithTags
    MutableLiveData<CustomResponse<?>> comments;//List<CommentResponse>
    MutableLiveData<CustomResponse<?>> isPosted;//Boolean
    MutableLiveData<CustomResponse<?>> isDeleted;//Boolean
    MutableLiveData<CustomResponse<?>> isReported;//Report for ads
    MutableLiveData<CustomResponse<?>> isReportedComment;//Report for comment
    MutableLiveData<CustomResponse<?>> applicants;
    MutableLiveData<CustomResponse<?>> appliedTo;
    MutableLiveData<CustomResponse<?>> applicantsSelected;

    private List<User> selectedUsers;
    AdRepository adRepository;
    UserRepository userRepository;

    public AdViewModel(@NonNull Application app) {
        super(app);
        adRepository = new AdRepository();
        userRepository = new UserRepository();
        selectedUsers = new LinkedList<User>();
        ad = new MutableLiveData<>();
        comments = new MutableLiveData<>();
        isPosted = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        isReported = new MutableLiveData<>();
        isReportedComment = new MutableLiveData<>();
        applicants = new MutableLiveData<>();
        appliedTo = new MutableLiveData<>();
        applicantsSelected = new MutableLiveData<>();
    }

    public void loadCredentials() {
        userId = Utility.getLoggedInUserId(App.getAppContext());
        token = Utility.getAccessToken(App.getAppContext());
    }



    public boolean isAdClosed() {
        return ((Ad)ad.getValue().getBody()).isClosed();
    }
    public boolean isAdMine() {
        return ((Ad)ad.getValue().getBody()).getEmployer().getUserId() == userId;
    }
//    public boolean isUserAppliedToAd() {
//        return (boolean)getAppliedTo().getValue().getBody();
//    }
    public int getNoOfApplicants() {
        return ((Ad)ad.getValue().getBody()).getNumberOfApplied();
    }

    public int getAdId() {
        return adId;
    }

    public int getUserId() {
        return userId;
    }

    public void clearSelectedUsersList() {
        selectedUsers.clear();
    }

    public MutableLiveData<CustomResponse<?>> getAd() {
        return ad;
    }

    public MutableLiveData<CustomResponse<?>> getComments() {
        return comments;
    }

    public MutableLiveData<CustomResponse<?>> getIsPosted() {
        return isPosted;
    }

    public MutableLiveData<CustomResponse<?>> getIsDeleted() {
        return isDeleted;
    }

    public MutableLiveData<CustomResponse<?>> getIsReported() { return isReported; }

    public MutableLiveData<CustomResponse<?>> getIsReportedComment() { return isReportedComment; }

    public MutableLiveData<CustomResponse<?>> getApplicants() { return applicants; }

    public MutableLiveData<CustomResponse<?>> getAppliedTo() {
        return appliedTo;
    }

    public boolean commentResponseOK() {
        return comments.getValue().getStatus() == CustomResponse.Status.OK;
    }
    public boolean adResponseOK() {
        return ad.getValue().getStatus() == CustomResponse.Status.OK;
    }

    public void load(int adId) {
        this.adId = adId;
        loadAd();
        loadComments();
        if (!Utility.getLoggedInUser(App.getAppContext()).isEmployer())
            loadIsApplied();
    }

    private void loadAd() {
        adRepository.getAd(token, ad, adId);
    }

    private void loadIsApplied() { adRepository.isApplied(appliedTo, adId);}

    private void loadComments() {
        adRepository.getComments(comments, adId);
    }

    public void loadApplicants() {
        adRepository.getAppliedUsers(token, applicants, adId);}

    public void postAComment(String comment) {
        if (comment != null && !comment.isEmpty())
            adRepository.postComment(token, comments, adId, comment);
    }
    public void applyForAd() {
        adRepository.applyForAd(token, appliedTo, userId, adId);
    }

    public void withdrawApplication() {
        adRepository.unApplyForAd(token, appliedTo, userId, adId);
    }

    public void selectWorkers() {
        adRepository.chooseApplicants(applicantsSelected, adId, selectedUsers);
    }
    public void addUserToSelected(User user) {
        selectedUsers.add(user);
    }

    public boolean removeUserFromSelected(User user) {
        return selectedUsers.remove(user);
    }

    public void reportAd(String elaboration) { //adRepository.reportUser(elaboration, isReported, adId, null);
    }

    public void reportComment(String elaboration) { //adRepository.reportUser(elaboration, isReportedComment, null, commentId);
    }
}

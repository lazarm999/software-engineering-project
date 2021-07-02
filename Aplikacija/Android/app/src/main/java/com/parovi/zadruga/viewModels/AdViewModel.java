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
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.requestModels.EditAdRequest;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.repository.AdRepository;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.ui.SelectPreferencesFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdViewModel extends AndroidViewModel implements SelectPreferencesFragment.TagsTracker {
    private int adId = 32; //4
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NH0.0SXnyerqRnP7HxklZwQ5k0q0XGYiMeEE2eTNixAxNrU";// "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
    private int userId = 4;//Utility.getLoggedInUserId(App.getAppContext());

    MutableLiveData<JobAdInfo> jobAd;

    MutableLiveData<CustomResponse<?>> ad;//AdWithTags
    MutableLiveData<CustomResponse<?>> comments;//List<CommentResponse>
    MutableLiveData<CustomResponse<?>> isPosted;//Boolean
    MutableLiveData<CustomResponse<?>> isUpdated;
    MutableLiveData<CustomResponse<?>> isDeleted;//Boolean
    MutableLiveData<CustomResponse<?>> isDeletedComment;
    MutableLiveData<CustomResponse<?>> isReported;//Report for ads
    MutableLiveData<CustomResponse<?>> isReportedComment;//Report for comment
    MutableLiveData<CustomResponse<?>> applicants;
    MutableLiveData<CustomResponse<?>> appliedTo;
    MutableLiveData<CustomResponse<?>> applicantsSelected;
    MutableLiveData<CustomResponse<?>> tags;

    private List<User> selectedUsers;
    private List<Integer> currTags;
    private List<Integer> newTags;
    AdRepository adRepository;
    UserRepository userRepository;
    LookUpRepository lookUpRepository;

    public AdViewModel(@NonNull Application app) {
        super(app);
        adRepository = new AdRepository();
        userRepository = new UserRepository();
        lookUpRepository = new LookUpRepository();
        selectedUsers = new LinkedList<User>();
        ad = new MutableLiveData<>();
        comments = new MutableLiveData<>();
        isPosted = new MutableLiveData<>();
        isUpdated = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        isReported = new MutableLiveData<>();
        isReportedComment = new MutableLiveData<>();
        applicants = new MutableLiveData<>();
        appliedTo = new MutableLiveData<>();
        applicantsSelected = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        isDeletedComment = new MutableLiveData<>();
        tags = new MutableLiveData<>();
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

    public List<Integer> getCurrTags() {
        return currTags;
    }

    public List<Integer> getNewTags() {
        return newTags;
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

    public MutableLiveData<CustomResponse<?>> getIsUpdated() {
        return isUpdated;
    }

    public MutableLiveData<CustomResponse<?>> getIsDeleted() {
        return isDeleted;
    }

    public MutableLiveData<CustomResponse<?>> getIsDeletedComment() {
        return isDeletedComment;
    }

    public MutableLiveData<CustomResponse<?>> getIsReported() { return isReported; }

    public MutableLiveData<CustomResponse<?>> getIsReportedComment() { return isReportedComment; }

    public MutableLiveData<CustomResponse<?>> getApplicants() { return applicants; }

    public MutableLiveData<CustomResponse<?>> getAppliedTo() {
        return appliedTo;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public MutableLiveData<CustomResponse<?>> getApplicantsSelected() {
        return applicantsSelected;
    }

    public void load(int adId) {
        this.adId = adId;
        load();
    }
    public void load() {
        loadAd();
        loadComments();
        if (!Utility.getLoggedInUser(App.getAppContext()).isEmployer())
            loadIsApplied();
    }

    public void loadAd() {
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

    public void updateAd(EditAdRequest editAdRequest) {
        adRepository.editAd(isUpdated, adId, editAdRequest);
    }

    public boolean removeUserFromSelected(User user) {
        return selectedUsers.remove(user);
    }

    public void reportAd(String elaboration) {
        userRepository.postReport(isReported, adId, null, elaboration);
    }
    public void reportComment(CommentResponse comment, String elaboration) {
        userRepository.postReport(isReported, null, comment.getId(), elaboration);
    }
    public void deleteAd() {
        adRepository.deleteAd(token, isDeleted, adId);
    }
    public void deleteComment(CommentResponse comment, int pos) {
        adRepository.deleteComment(token, isDeletedComment, comment.getId(), pos);
    }

    public void initializeTagLists() {
        if (getAd().getValue() == null)
            return;
        currTags = new ArrayList<Integer>();
        newTags = new ArrayList<Integer>();
        Ad ad = (Ad)getAd().getValue().getBody();
        for(Tag tag: ad.getTags()) {
            currTags.add(tag.getTagId());
            newTags.add(tag.getTagId());
        }
    }


    @Override
    public List<Integer> getNewSelectedTags() {
        return newTags;
    }

    @Override
    public List<Integer> getCurrentlySelectedTags() {
        return currTags;
    }

    @Override
    public void loadTags() {
        lookUpRepository.getAllTags(token, tags);
    }

    @Override
    public MutableLiveData<CustomResponse<?>> getTags() {
        return tags;
    }
}

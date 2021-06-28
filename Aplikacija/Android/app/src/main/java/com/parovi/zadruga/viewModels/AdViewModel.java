package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.data.JobAdInfo;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.repository.AdRepository;

public class AdViewModel extends AndroidViewModel {
    private final int adId = 32; //4
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
    private final int userId = 1;

    MutableLiveData<JobAdInfo> jobAd;

    MutableLiveData<CustomResponse<?>> ad;//AdWithTags
    MutableLiveData<CustomResponse<?>> comments;//List<CommentResponse>
    MutableLiveData<CustomResponse<?>> isPosted;//Boolean
    MutableLiveData<CustomResponse<?>> isDeleted;//Boolean
    MutableLiveData<CustomResponse<?>> applicants;
    MutableLiveData<CustomResponse<?>> appliedTo;

    AdRepository adRepository;

    public AdViewModel(@NonNull Application app) {
        super(app);
        adRepository = new AdRepository();
        ad = new MutableLiveData<>();
        comments = new MutableLiveData<>();
        isPosted = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        applicants = new MutableLiveData<>();
        appliedTo = new MutableLiveData<>();

        loadAd();
        loadComments();
    }

    public boolean isAdMine() {
        return ((Ad)ad.getValue().getBody()).getEmployer().getUserId() == userId;
    }

    public int getAdId() {
        return adId;
    }

    public int getUserId() {
        return userId;
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
    private void loadAd() {
        adRepository.getAd(token, ad, adId);
    }
    private void loadComments() {
        adRepository.getComments(comments, adId);
    }
    public void loadApplicants() {
        adRepository.getAppliedUsers(token, applicants, adId);}
    public void postAComment(String comment) {
        adRepository.postComment(token, comments, adId, comment);
    }
    public void applyForAd() {
        adRepository.applyForAd(token, appliedTo, userId, adId);
    }
    public void withdrawApplication() {
        adRepository.unApplyForAd(token, appliedTo, userId, adId);
    }
}

package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.data.JobAdInfo;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.repository.ZadrugaRepository;

public class JobAdViewModel extends AndroidViewModel {
    private final int adId = 4;
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
    private final int userId = 1;

    MutableLiveData<JobAdInfo> jobAd;

    MutableLiveData<CustomResponse<?>> ad;//AdWithTags
    MutableLiveData<CustomResponse<?>> comments;//List<CommentResponse>
    MutableLiveData<CustomResponse<?>> isPosted;//Boolean
    MutableLiveData<CustomResponse<?>> isDeleted;//Boolean
    MutableLiveData<CustomResponse<?>> appliedTo;

    ZadrugaRepository zadrugaRepository;

    public JobAdViewModel(@NonNull Application app) {
        super(app);
        zadrugaRepository = ZadrugaRepository.getInstance(app);
        ad = new MutableLiveData<>();
        comments = new MutableLiveData<>();
        isPosted = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
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
        zadrugaRepository.getAd(token, ad, adId);
    }
    private void loadComments() {
        zadrugaRepository.getComments(comments, adId);
    }
    public void postAComment(String comment) {
        zadrugaRepository.postComment(token, comments, adId, userId, comment);
    }
    public void applyForAd() {
        zadrugaRepository.applyForAd(token, appliedTo, userId, adId);
    }
    public void unapplyForAd() {
        zadrugaRepository.unApplyForAd(token, appliedTo, userId, adId);
    }
}

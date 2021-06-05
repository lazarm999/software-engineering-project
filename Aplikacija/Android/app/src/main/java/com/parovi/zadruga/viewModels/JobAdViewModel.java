package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.data.JobAdInfo;
import com.parovi.zadruga.models.nonEntityModels.AdWithTags;
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
    ZadrugaRepository zadrugaRepository;

    public JobAdViewModel(@NonNull Application app) {
        super(app);
        zadrugaRepository = ZadrugaRepository.getInstance(app);
        ad = new MutableLiveData<>();
        comments = new MutableLiveData<>();
        isPosted = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        loadAd();
        loadComments();
    }

    /*public MutableLiveData<JobAd> getJobAd() {
        if (jobAd == null) {
            jobAd = new MutableLiveData<JobAd>();
            loadJobAd();
        }
        return jobAd;
    }*/

    public boolean isAdMine() {
        return ((AdWithTags)ad.getValue().getBody()).adEmployerLocation.getEmployer().getUserId() == userId;
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

    /*private void loadJobAd() {
        JobAd jobAd = new JobAd(id);
        jobAd.setTitle("Istovar robe");
        jobAd.setDescription("Description.....");
        jobAd.setLocation("Nis");
        jobAd.setCompensationFrom(10);
        jobAd.setCompensationTo(20);
        jobAd.setDate(LocalDate.now());
        jobAd.setDuration(120);
        List<ApplicantResume> applicants = new LinkedList<ApplicantResume>();
        applicants.add(new ApplicantResume(1, "Uros", "@urostt_", true));
        applicants.add(new ApplicantResume(2, "Lazar", "@lazarminic", false));
        applicants.add(new ApplicantResume(3, "Vuk Bibic", "@vukbibic", false));
        jobAd.setApplicants(applicants);
        jobAd.setMine(false);
        this.jobAd.setValue(jobAd);
    }*/

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
        zadrugaRepository.getComments(token, comments, adId);
    }
    private boolean postAComment(String comment) {
        zadrugaRepository.postComment(token, comments, adId, userId, comment);
        return comments.getValue().getStatus() == CustomResponse.Status.OK;
    }
}

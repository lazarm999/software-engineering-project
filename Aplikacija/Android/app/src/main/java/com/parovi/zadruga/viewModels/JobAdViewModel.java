package com.parovi.zadruga.viewModels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.data.ApplicantResume;
import com.parovi.zadruga.data.JobAd;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class JobAdViewModel extends ViewModel {
    private final static long id = 4;
    int userId = 1;
    MutableLiveData<JobAd> jobAd;

    MutableLiveData<CustomResponse<?>> user;//AdWithTags
    MutableLiveData<CustomResponse<?>> ad;//AdWithTags
    MutableLiveData<CustomResponse<?>> comments;//List<CommentResponse>
    MutableLiveData<CustomResponse<?>> isPosted;//Boolean
    MutableLiveData<CustomResponse<?>> isDeleted;//Boolean

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MutableLiveData<JobAd> getJobAd() {
        if (jobAd == null) {
            jobAd = new MutableLiveData<JobAd>();
            loadJobAd();
        }
        return jobAd;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadJobAd() {
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
    }

}

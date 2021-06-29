package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.repository.AdminRepository;

import org.jetbrains.annotations.NotNull;

public class ReportViewModel extends AndroidViewModel {

    public MutableLiveData<CustomResponse<?>> reports;
    public MutableLiveData<CustomResponse<?>> isDeleted;
    public AdminRepository adminRepository;

    public ReportViewModel(@NonNull @NotNull Application application) {
        super(application);
        reports = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        adminRepository = new AdminRepository();
        loadReports();
    }

    public MutableLiveData<CustomResponse<?>> getReports() {
        return reports;
    }

    public MutableLiveData<CustomResponse<?>> getIsDeleted() {
        return isDeleted;
    }

    public void loadReports(){
        adminRepository.getReports(reports);
    }

    public void deleteReport(int reportId, int pos){
        adminRepository.deleteReport(isDeleted, reportId, pos);
    }
}

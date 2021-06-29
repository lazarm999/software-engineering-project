package com.parovi.zadruga.repository;

import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Report;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.OutputKeys;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class AdminRepository extends BaseRepository {

    private void remoteReportToReport(Report report){
        report.setFkReportedId(report.getReported().getUserId());
        report.setFkReporterId(report.getReporter().getUserId());
    }

    public void getReports(MutableLiveData<CustomResponse<?>> reports){
        String token = Utility.getAccessToken(App.getAppContext());
        Utility.getExecutorService().execute(() -> {
            try {
                Response<List<Report>> reportsResponse = ApiFactory.getAdminApi().getReports(token).execute();
                if(reportsResponse.isSuccessful()){
                    if(reportsResponse.body() == null) return;
                    for (Report report : reportsResponse.body()) {
                        if(report.getCommentText() != null)
                            report.setType(Constants.COMMENT_REPORT);
                        else
                            report.setType(Constants.AD_REPORT);
                    }
                    reports.postValue(new CustomResponse<>(CustomResponse.Status.OK, reportsResponse.body()));
                    saveReportsLocally(reportsResponse.body());
                } else
                    responseNotSuccessful(reportsResponse.code(), reports);
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), reports);
            }
        });
    }

    private void saveReportsLocally(List<Report> reportsResponse) {
        Utility.getExecutorService().execute(()->{
            for (Report report : reportsResponse) {
                remoteReportToReport(report);
                DaoFactory.getReportDao().insertOrUpdate(report);
            }
        });
    }

    public void deleteReport(MutableLiveData<CustomResponse<?>> isDeleted, int reportId, int pos){
        String token = Utility.getAccessToken(App.getAppContext());
        Utility.getExecutorService().execute(() -> {
            try {
                Response<ResponseBody> res = ApiFactory.getAdminApi().deleteReport(token, reportId).execute();
                if(res.isSuccessful()){
                    isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.OK, pos));
                    DaoFactory.getReportDao().deleteReport(reportId);
                } else
                    responseNotSuccessful(res.code(), isDeleted);
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), isDeleted);
            }
        });
    }
}

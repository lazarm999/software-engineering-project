package com.parovi.zadruga.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.AdRepository;
import com.parovi.zadruga.repository.UserRepository;

public class JobHistoryViewModel extends ViewModel {
    MutableLiveData<CustomResponse<?>> jobList;
    UserRepository userRepository;
    User loggedUser;

    public JobHistoryViewModel() {
        loggedUser = Utility.getLoggedInUser(App.getAppContext());
        userRepository = new UserRepository();
        jobList = new MutableLiveData<>();
    }

    public MutableLiveData<CustomResponse<?>> getJobList() {
        return jobList;
    }
    public void loadJobs() {
        if (loggedUser.isEmployer())
            userRepository.getPostedAdsByUserIdLocal(jobList);
        else
            userRepository.getFinishedJobsByUserId(jobList);
    }
}

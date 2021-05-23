package com.parovi.zadruga.viewModels;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.AdWithLocation;
import com.parovi.zadruga.models.entityModels.TmpPost;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.nonEntityModels.CommentUser;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.quickblox.auth.QBAuth;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import de.measite.minidns.record.A;

public class TmpViewModel extends AndroidViewModel {
    private MutableLiveData<List<Ad>> ads;
    private LiveData<List<Location>> locations;
    private MutableLiveData<List<TmpPost>> posts;
    private ZadrugaRepository rep;

    public TmpViewModel(@NonNull Application app) {
        super(app);
        rep = ZadrugaRepository.getInstance(app);
        posts = new MutableLiveData<>();

        /*chats.setValue(new ArrayList<>());
        chat.setValue(new QBChatDialog());*/
    }

    /*public getAds() {
        rep.getAllAds();
    }*/

    public LiveData<List<Ad>> observeAds() {
        ads = rep.getAllAds();
        return ads;
    }

    public LiveData<List<Location>> getAllLocations() {
        locations = rep.getAllLocations();
        return locations;
    }

    public void insertAd(Ad ad){
        rep.insertAd(ad);
    }

    public void insertLocation(Location location){
        rep.insertLocation(location);
    }

    public void getAllPosts(){
        rep.getAllPosts(posts);
    }

    public  LiveData<List<TmpPost>> observePosts(){
        return posts;
    }
}

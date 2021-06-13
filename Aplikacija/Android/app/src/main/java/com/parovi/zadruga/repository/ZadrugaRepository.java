package com.parovi.zadruga.repository;


import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.PushNotification;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.AppliedDao;
import com.parovi.zadruga.daos.ChatDao;
import com.parovi.zadruga.daos.CommentDao;
import com.parovi.zadruga.daos.LookupDao;
import com.parovi.zadruga.daos.MessageDao;
import com.parovi.zadruga.daos.RatingDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.databases.ZadrugaDatabase;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.University;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Applied;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserBadge;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserChat;
import com.parovi.zadruga.models.nonEntityModels.AdWithTags;
import com.parovi.zadruga.models.nonEntityModels.CommentWithUser;
import com.parovi.zadruga.models.nonEntityModels.UserWithFaculty;
import com.parovi.zadruga.models.requestModels.BanRequest;
import com.parovi.zadruga.models.requestModels.ChangePasswordRequest;
import com.parovi.zadruga.models.requestModels.ChatMembersRequest;
import com.parovi.zadruga.models.requestModels.ChooseApplicantsRequest;
import com.parovi.zadruga.models.requestModels.CommentRequest;
import com.parovi.zadruga.models.requestModels.EditAdRequest;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.models.responseModels.AdResponse;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.models.responseModels.LoginResponse;
import com.parovi.zadruga.models.responseModels.RatingResponse;
import com.parovi.zadruga.retrofit.AdApi;
import com.parovi.zadruga.retrofit.CommentApi;
import com.parovi.zadruga.retrofit.LookupApi;
import com.parovi.zadruga.retrofit.NotificationApi;
import com.parovi.zadruga.retrofit.RatingApi;
import com.parovi.zadruga.retrofit.UserApi;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//VUKOV token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
//TEIN token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g";
//Marko car token = eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTl9.x6y6ONrdWKne_hU11h_tJJUQYQFfBoM6R10o6Ji3ajE
/*
* grupni chat: vuk - 128330407, tea - 128586493,
markocar - 128304620
priv chat: vuk i tea*/
public class ZadrugaRepository  {
    private static ZadrugaRepository instance;
    private final ZadrugaDatabase localDb;
    private final AdDao adDao;
    private final UserDao userDao;
    private final RatingDao ratingDao;
    private final LookupDao lookupDao;
    private final AppliedDao appliedDao;
    private final CommentDao commentDao;
    private final ChatDao chatDao;
    private final MessageDao messageDao;
    private final UserApi userApi;
    private final AdApi adApi;
    private final LookupApi lookupApi;
    private final CommentApi commentApi;
    private final RatingApi ratingApi;
    private final NotificationApi notificationApi;

    private final Executor executor;

    public synchronized static ZadrugaRepository getInstance(Application app) {
        if (instance == null) {
            instance = new ZadrugaRepository(app);
        }
        return instance;
    }

    private ZadrugaRepository(Application app) {
        localDb = ZadrugaDatabase.getInstance(app);
        adDao = localDb.adDao();
        userDao = localDb.userDao();
        ratingDao = localDb.ratingDao();
        lookupDao = localDb.lookupDao();
        appliedDao = localDb.appliedDao();
        commentDao = localDb.commentDao();
        chatDao = localDb.chatDao();
        messageDao = localDb.messageDao();
        executor = MoreExecutors.directExecutor();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();

        Retrofit backend = new Retrofit.Builder()
                .baseUrl(Constants.ZADRUGA_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit notifications = new Retrofit.Builder()
                .baseUrl(Constants.FCM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = backend.create(UserApi.class);
        adApi = backend.create(AdApi.class);
        lookupApi = backend.create(LookupApi.class);
        commentApi = backend.create(CommentApi.class);
        ratingApi = backend.create(RatingApi.class);
        notificationApi = notifications.create(NotificationApi.class);
    }

    //Notification
    public Call<ResponseBody> sendPushNotification(PushNotification notification){
        return notificationApi.sendPushNotification(notification);
    }

    //Ad
    public void postAd(String token, MutableLiveData<CustomResponse<?>> isPosted, PostAdRequest ad){
        adApi.postAd(token, ad).enqueue(new Callback<Ad>() {
            @Override
            public void onResponse(@NotNull Call<Ad> call, @NotNull Response<Ad> response) {
                if(response.isSuccessful() && response.body() != null) {
                    isPosted.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    saveAdLocally(response.body());
                } else
                    responseNotSuccessful(response.code(), isPosted);
            }

            @Override
            public void onFailure(@NotNull Call<Ad> call, @NotNull Throwable t) {
                isPosted.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getAd(String token, MutableLiveData<CustomResponse<?>> ad, int id){
        final Boolean[] isSynced = {false};
        getAdLocal(ad, id, isSynced);
        adApi.getAd(token, id).enqueue(new Callback<Ad>() {
            @Override
            public void onResponse(@NotNull Call<Ad> call, @NotNull Response<Ad> response) {
                if(response.isSuccessful() && response.body() != null){
                    synchronized (isSynced[0]) {
                        ad.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                    }
                    saveAdLocally(response.body());
                } else
                    responseNotSuccessful(response.code(), ad);
            }

            @Override
            public void onFailure(@NotNull Call<Ad> call, @NotNull Throwable t) {
                ad.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    private void saveAdsLocally(List<Ad> ads){
        for (Ad a: ads) {
            if(a.getLocation() != null)
                a.setFkLocationId(a.getLocation().getLocId());
            a.setFkEmployerId(a.getEmployer().getUserId());

            lookupDao.insertLocation(a.getLocation());
            lookupDao.insertTags(a.getTags());

            userDao.insertUser(a.getEmployer());
            if(a.getTags() != null) {
                for (Tag tag : a.getTags()) {
                    lookupDao.insertAdTag(new AdTag(a.getAdId(), tag.getTagId()));
                }
            }
            adDao.insertAd(a);
        }
    }

    private void saveAdLocally(Ad ad){
        if(ad.getLocation() != null)
            ad.setFkLocationId(ad.getLocation().getLocId());
        ad.setFkEmployerId(ad.getEmployer().getUserId());

        lookupDao.insertLocation(ad.getLocation());
        lookupDao.insertTags(ad.getTags());

        userDao.insertUser(ad.getEmployer());
        if(ad.getTags() != null){
            for (Tag tag: ad.getTags()) {
                lookupDao.insertAdTag(new AdTag(ad.getAdId(), tag.getTagId()));
            }
        }
        adDao.insertAd(ad);
    }

    private Ad adWithTagsToAd(AdWithTags adWithTags){
        Ad tmpAd;
        tmpAd = adWithTags.adEmployerLocation.getAd();
        tmpAd.setEmployer(adWithTags.adEmployerLocation.getEmployer());
        tmpAd.setLocation(adWithTags.adEmployerLocation.getLocation());
        tmpAd.setTags(adWithTags.tags);
        return tmpAd;
    }

    public void getAdLocal(MutableLiveData<CustomResponse<?>> ad, int id,  Boolean[] isSynced){
        Futures.addCallback(adDao.getAd(id), new FutureCallback<AdWithTags>() {
            @Override
            public void onSuccess(@org.jetbrains.annotations.Nullable AdWithTags result) {
                if(result != null){
                    Log.i("getAd", "onSuccess: radiii");
                    Ad tmpAd = adWithTagsToAd(result);
                    synchronized (isSynced[0]) {
                        if (!isSynced[0])
                            ad.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpAd));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Throwable t) {

            }
        }, executor);
    }

    //TODO: kad da dodajem ove podatke iz lookup tebela u lokalnu bazu?
    public void getAds(String token, MutableLiveData<CustomResponse<?>> ads) {
        final Boolean[] isSynced = {false};
        getAdsLocal(ads, isSynced);
        adApi.getAds(token).enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(@NotNull Call<List<Ad>> call, @NotNull Response<List<Ad>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Ad> tmpAdList;
                    if(ads.getValue() != null && ads.getValue().getBody() != null)
                        tmpAdList = (List<Ad>) ads.getValue().getBody();
                    else
                        tmpAdList = new ArrayList<>();
                    saveAdsLocally(response.body());
                    tmpAdList.addAll(response.body());
                    synchronized (isSynced[0]) {
                        ads.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpAdList));
                        isSynced[0] = true;
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<Ad>> call, @NotNull Throwable t) {
                ads.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getAdsLocal(MutableLiveData<CustomResponse<?>> ads, Boolean[] isSynced){
        Futures.addCallback(adDao.getAds(), new FutureCallback<List<AdWithTags>>() {
            @Override
            public void onSuccess(@Nullable List<AdWithTags> result) {
                if(result != null){
                    Log.i("getAd", "onSuccess: radiii");
                    List<Ad> tmpList = new ArrayList<>();
                    for (AdWithTags ad : result) {
                        tmpList.add(adWithTagsToAd(ad));
                    }
                    synchronized (isSynced[0]) {
                        if(!isSynced[0])
                            ads.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpList));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Throwable t) {

            }
        }, executor);
    }

    public void applyForAd(String token, MutableLiveData<CustomResponse<?>> isApplied, int userId, int adId){
        adApi.applyForAd(token, adId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    isApplied.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    appliedDao.insertApplied(new Applied(userId, adId, false));
                }else {
                    isApplied.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, "Pogresno uneti podaci"));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isApplied.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void chooseApplicants(MutableLiveData<CustomResponse<?>> isSucc, int adId, List<User> chosenUsers){
        final String token = Utility.getAccessToken(App.getAppContext());
        int employerQbUserId = Utility.getUserQbId(App.getAppContext());
        List<Integer> qbUserIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        for (User u : chosenUsers) {
            userIds.add(u.getUserId());
            qbUserIds.add(u.getUserQbId());
        }
        //kad kreiram chat u listi id-eva treba da bude i id employera
        qbUserIds.add(employerQbUserId);
        MutableLiveData<CustomResponse<?>> newQbChatId = new MutableLiveData<>();
        Observer<CustomResponse<?>> observer = new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    //TODO: treba da se upise kod vuka i qbChatId vezan za oglas
                    adApi.chooseApplicants(token, adId, new ChooseApplicantsRequest(userIds, (String)customResponse.getBody())).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                isSucc.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                                for (Integer i: userIds) {
                                    appliedDao.insertApplied(new Applied(i, adId, true));
                                }
                            } else
                                responseNotSuccessful(response.code(), isSucc);
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            isSucc.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
                        }
                    });
                }
                newQbChatId.removeObserver(this);
            }
        };
        newQbChatId.observeForever(observer);
        ZadrugaRepository.this.createChat(newQbChatId, qbUserIds, adId);
    }

    public void unApplyForAd(String token, MutableLiveData<CustomResponse<?>> isUnApplied, int userId, int adId){
        adApi.unApplyForAd(token, adId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    isUnApplied.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    appliedDao.deleteApplied(new Applied(userId, adId));
                } else {
                    isUnApplied.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, "Pogresno uneti podaci"));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isUnApplied.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void editAd(String token, MutableLiveData<CustomResponse<?>> isSucc, int adId, EditAdRequest ad){
        adApi.editAd(token, adId, ad).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isSucc.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    updateAdLocally(ad, adId);
                }
                else
                    responseNotSuccessful(response.code(), isSucc);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isSucc.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    private void updateAdLocally(EditAdRequest ad, int adId){
        Ad tmpLocalAd = new Ad(adId, ad.getTitle(), ad.getDescription(),
                ad.getCompensationMin(),
                ad.getCompensationMax(),
                ad.getNumberOfEmployees());
        adDao.updateAd(tmpLocalAd);
        if(ad.getRemoveTags() != null){
            for (Integer i: ad.getRemoveTags()) {
                lookupDao.deleteAdTag(i, adId);
            }
        }
        if(ad.getAddTags() != null){
            for (Integer i: ad.getAddTags()) {
                lookupDao.insertAdTag(new AdTag(adId, i));
            }
        }
    }

    public void deleteAd(String token, MutableLiveData<CustomResponse<?>> isSucc, int adId){
        adApi.deleteAd(token, adId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isSucc.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    adDao.deleteAdById(adId);
                }  responseNotSuccessful(response.code(), isSucc);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isSucc.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    //Comments
    public void postComment(String token, MutableLiveData<CustomResponse<?>> res, int adId, int userId, String comment){
        CommentRequest c = new CommentRequest(comment);
        commentApi.postComment(token, adId, c).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(@NotNull Call<CommentResponse> call, @NotNull Response<CommentResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    CommentWithUser tmpComment = new CommentWithUser();
                    tmpComment.setUser(response.body().getUser());
                    Comment tmpLocalComment = new Comment(response.body().getId(), userId, adId, comment, response.body().getPostTime());
                    tmpComment.setComment(tmpLocalComment);
                    res.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    commentDao.insertComment(tmpLocalComment);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CommentResponse> call, @NotNull Throwable t) {
                res.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getComments(MutableLiveData<CustomResponse<?>> comments, int adId){
        final String token = Utility.getAccessToken(App.getAppContext());
        Boolean[] isSynced = {false};
        getCommentsLocal(comments, adId, isSynced);
        commentApi.getComments(token, adId).enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<CommentResponse>> call, @NotNull Response<List<CommentResponse>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        synchronized (isSynced[0]){
                            comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                            isSynced[0] = true;
                        }
                        for (CommentResponse c: response.body()) {
                            int commentId = c.getId();
                            userDao.insertUser(c.getUser());
                            commentDao.insertComment(new Comment(c.getId(), c.getUser().getUserId(), adId, c.getComment(), c.getPostTime()));
                            MutableLiveData<CustomResponse<?>> profileImg = new MutableLiveData<>();
                            Observer<CustomResponse<?>> imageObserver = new Observer<CustomResponse<?>>() {
                                @Override
                                public void onChanged(CustomResponse<?> customResponse) {
                                    if(customResponse.getStatus() == CustomResponse.Status.OK){
                                        Bitmap bmImage = (Bitmap) customResponse.getBody();
                                        List<CommentResponse> commentList = (List<CommentResponse>) comments.getValue().getBody();
                                        if(commentList != null){
                                            for (int i = 0; i < commentList.size(); i++) {
                                                if(commentList.get(i).getId() == commentId){
                                                    if(bmImage != null) commentList.get(i).setUserImage(bmImage);
                                                    break;
                                                }
                                            }
                                        }
                                        comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, commentList));
                                    }
                                    if(!customResponse.isLocal())
                                        profileImg.removeObserver(this);
                                }
                            };
                            profileImg.observeForever(imageObserver);
                            ZadrugaRepository.this.getProfilePicture(profileImg, c.getUser().getUserId());
                        }
                    } else {
                        synchronized (isSynced[0]){
                            comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<CommentResponse>()));
                            isSynced[0] = true;
                        }
                    }

                } else
                    responseNotSuccessful(response.code(), comments);
            }

            @Override
            public void onFailure(@NotNull Call<List<CommentResponse>> call, @NotNull Throwable t) {
                CustomResponse<?> tmp = comments.getValue();
                if(tmp != null){
                    tmp.setMessage("Greska kod servera.");
                    comments.postValue(tmp);
                }
                else
                    comments.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, "Greska kod servera."));
            }
        });
    }

    private void getCommentsLocal(MutableLiveData<CustomResponse<?>> comments, int adId, Boolean[] isSynced){
        Futures.addCallback(commentDao.getCommentsByAdId(adId), new FutureCallback<List<CommentWithUser>>() {
            @Override
            public void onSuccess(@Nullable List<CommentWithUser> result) {
                if(result != null && result.size() > 0){
                    List<CommentResponse> tmpList = new ArrayList<>();
                    for (CommentWithUser c: result) {
                        int commentId = c.getComment().getCommentId();
                        Comment tmpComment = c.getComment();
                        tmpList.add(new CommentResponse(tmpComment.getCommentId(), tmpComment.getFkAdId(),
                                tmpComment.getComment(), tmpComment.getPostTime(), c.getUser()));
                        MutableLiveData<CustomResponse<?>> profileImg = new MutableLiveData<>();
                        Observer<CustomResponse<?>> imageObserver = new Observer<CustomResponse<?>>() {
                            @Override
                            public void onChanged(CustomResponse<?> customResponse) {
                                if(customResponse.getStatus() == CustomResponse.Status.OK){
                                    Bitmap bmImage = (Bitmap) customResponse.getBody();
                                    List<CommentResponse> commentList = (List<CommentResponse>) comments.getValue().getBody();
                                    if(commentList != null){
                                        for (int i = 0; i < commentList.size(); i++) {
                                            if(commentList.get(i).getId() == commentId){
                                                if(bmImage != null) commentList.get(i).setUserImage(bmImage);
                                                break;
                                            }
                                        }
                                    }
                                    comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, commentList));
                                }
                                profileImg.removeObserver(this);
                            }
                        };
                        profileImg.observeForever(imageObserver);
                        ZadrugaRepository.this.getProfilePictureLocally(profileImg, c.getUser().getUserId(), new Boolean[]{false});
                    }
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpList));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Throwable t) {

            }
        }, executor);

    }

    public void deleteComment(String token, MutableLiveData<CustomResponse<?>> isDeleted, int commentId){
        commentApi.deleteComment(token, commentId).enqueue(new Callback<AdResponse>() {
            @Override
            public void onResponse(@NotNull Call<AdResponse> call, @NotNull Response<AdResponse> response) {
                if(response.isSuccessful()){
                    commentDao.deleteComment(commentId);
                    isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                }
                else
                    responseNotSuccessful(response.code(), isDeleted);
            }

            @Override
            public void onFailure(@NotNull Call<AdResponse> call, @NotNull Throwable t) {
                isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    //User
    public void setFcmToken(String token, int userId, String fcmToken){
        User user = new User();
        user.setUserId(userId);
        user.setFcmToken(fcmToken);
        userApi.updateUser(token, userId, user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    Log.i("updateUser", "onResponse: ");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("updateUser", "crkoo: ");
            }
        });
    }

    public void removeFcmToken(String token, int userId){
        User user = new User();
        user.setUserId(userId);
        user.setFcmToken("");
        userApi.updateUser(token, userId, user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    Log.i("updateUser", "onResponse: ");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void registerUser(MutableLiveData<CustomResponse<?>> newUser, User u){
        //TODO: da l treba da se obestava korisnik sta je tacno problem
        QBUser qbUser = new QBUser();
        qbUser.setLogin(u.getUsername());
        qbUser.setEmail(u.getEmail());
        qbUser.setPassword(u.getPassword());

        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Log.i("qbSignIn", "braoooo");
                u.setUserQbId(user.getId());
                userApi.registerUser(u).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Log.i("signIn", "braoooo");
                            newUser.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                            qbUser.setExternalId(Integer.toString(response.body().getUserId()));
                            QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                    Log.i("sadfa", "onSuccess: ");
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    Log.i("sadfa", "onError: ");
                                }
                            });
                            userDao.insertUser(response.body());
                        }
                        else
                            responseNotSuccessful(response.code(), newUser);
                    }

                    @Override
                    public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                        newUser.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
                    }
                });
            }

            @Override
            public void onError(QBResponseException error) {
                Log.i("qbSignIn", "NE braoooo");
                newUser.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, error.getMessage()));
            }
        });

    }

    public void loginUser(MutableLiveData<CustomResponse<?>> isLoginSuccessful, User u){
        //TODO: da l treba da se obestava korisnik sta je tacno problem
        //TODO: ovde treba da vuku posaljes fcmToken, kako bi znao kome da salje notif
        userApi.loginUser(u).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    QBUser qbUser = new QBUser();
                    qbUser.setEmail(u.getEmail());
                    qbUser.setPassword(u.getPassword());

                    QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser user, Bundle args) {
                            Log.i("logIn", "braoooo");
                            isLoginSuccessful.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                            /*FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("FETCH_FCM_TOKEN_FAILED", "Fetching FCM registration token failed", task.getException());
                                        return;
                                    }
                                }
                            });*/
                            response.body().getUser().setPassword(u.getPassword());
                            Utility.saveLoggedUserInfo(App.getAppContext(), response.body().getToken(), response.body().getUser());
                            userDao.insertUser(response.body().getUser());
                        }

                        @Override
                        public void onError(QBResponseException error) {
                            Log.i("logIn", "ne braoooo");
                            isLoginSuccessful.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, error.getMessage()));
                        }
                    });
                }
                else
                    responseNotSuccessful(response.code(), isLoginSuccessful);
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Log.i("logIn", "ne braoooo");
                isLoginSuccessful.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void logOutUser(MutableLiveData<CustomResponse<?>> isLoggedOut){
        if (QBSessionManager.getInstance().getSessionParameters() == null) {
            return;
        }
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.i("logOut", "braoooo");
                isLoggedOut.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                Utility.removeLoggedUserInfo(App.getAppContext());
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("logOut", "ne braoooo");
                isLoggedOut.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, e.getMessage()));
            }
        });
    }

    public void changePassword(String token, MutableLiveData<CustomResponse<?>> isChanged, int qbId, String oldPass, String newPass){
        userApi.changePassword(token, new ChangePasswordRequest(oldPass, newPass)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    final QBUser user = new QBUser();
                    user.setId(qbId);
                    user.setPassword(newPass);
                    user.setOldPassword(oldPass);

                    QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            isChanged.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            responseNotSuccessful(e.getHttpStatusCode(), isChanged);
                        }
                    });
                }
                else
                    responseNotSuccessful(response.code(), isChanged);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isChanged.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getUserById(String token, MutableLiveData<CustomResponse<?>> user, int id){
        final Boolean[] isSynced = {false};
        getUserByIdLocal(user, id, isSynced);
        userApi.getUserById(token, id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    User tmpUser = response.body();
                    synchronized (isSynced[0]){
                        user.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                    }
                    if(tmpUser.getFaculty() != null){
                        tmpUser.setFkFacultyId(tmpUser.getFaculty().getFacultyId());
                        tmpUser.getFaculty().setFkUniversityId(tmpUser.getFaculty().getUniversity().getUniversityId());
                        if (response.body().getFaculty().getUniversity() != null)
                            lookupDao.insertUniversity(tmpUser.getFaculty().getUniversity());
                        lookupDao.insertFaculty(tmpUser.getFaculty());
                    }
                    userDao.insertUser(tmpUser);
                    if(tmpUser.getBadges() != null) {
                        lookupDao.insertBadges(tmpUser.getBadges());
                        List<UserBadge> tmpUserBadge = new ArrayList<>();
                        for (Badge b : tmpUser.getBadges()) {
                            tmpUserBadge.add(new UserBadge(id, b.getBadgeId()));
                        }
                        lookupDao.insertUserBadges(tmpUserBadge);
                    }
                } else
                    responseNotSuccessful(response.code(), user);
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                user.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void populateData(){
        /*ArrayList<University> universities = new ArrayList<>();
        universities.add(new University(1, "univerzitet nis"));
        universities.add(new University(2, "univerzitet novi sad"));
        universities.add(new University(3, "univerzitet bege"));
        userDao.insertUniversities(universities);
        ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1, "faks nis", 1));
        faculties.add(new Faculty(2, "faks elfak", 1));
        faculties.add(new Faculty(3, "faks bege", 2));
        userDao.insertFaculties(faculties);
        userDao.insertUser(new User(3, "dorjde", 2));*/
        userDao.insertUser(new User(19, "nebitno"));
    }

    private void getUserByIdLocal(MutableLiveData<CustomResponse<?>> user, int id, Boolean[] isSynced){
        Futures.addCallback(lookupDao.getUserBadges(id), new FutureCallback<List<Badge>>() {
            @Override
            public void onSuccess(List<Badge> badges) {
                Futures.addCallback(userDao.getUserWithFaculty(id), new FutureCallback<UserWithFaculty>() {
                    @Override
                    public void onSuccess(UserWithFaculty userWithFaculty) {
                        if(userWithFaculty != null){
                            Log.i("getUserWithFaculty", "onSuccess:");
                            User tmpUser = userWithFaculty.getUser();
                            if(userWithFaculty.getFaculty() != null){
                                userWithFaculty.getFaculty().setUniversity(userWithFaculty.getUniversity());
                                tmpUser.setFaculty(userWithFaculty.getFaculty());
                            }
                            if(badges != null){
                                tmpUser.setBadges(badges);
                            }
                            synchronized (isSynced[0]){
                                if(!isSynced[0]){
                                    user.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpUser));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Throwable t) {

                    }
                }, executor);
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
            }
        }, executor);
    }

    public void updateUser(String token, MutableLiveData<CustomResponse<?>> isUpdated, User user){
        userApi.updateUser(token, user.getUserId(), user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful()){
                    isUpdated.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    userDao.updateUser(user);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                isUpdated.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getProfilePicture(MutableLiveData<CustomResponse<?>> profilePicture, int userId){
        final String token = Utility.getAccessToken(App.getAppContext());
        Boolean[] isSynced = {false};
        getProfilePictureLocally(profilePicture, userId, isSynced);
        userApi.getProfileImage(token, userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        synchronized (isSynced[0]) {
                            profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.OK, bmp, false));
                            isSynced[0] = true;
                        }
                        Log.i("saveImageLocally", saveImageLocally(bmp, userId));
                    } else {
                        responseNotSuccessful(response.code(), profilePicture);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage(), false));
            }
        });
    }

    private void getProfilePictureLocally(MutableLiveData<CustomResponse<?>> profilePicture, int userId, Boolean[] isSynced){
        if(App.getAppContext() == null){
            return;
        }
        ContextWrapper cw = new ContextWrapper(App.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        try {
            File f = new File(directory.getAbsolutePath(), userId + ".jpg");
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(f));
            synchronized (isSynced[0]){
                if(!isSynced[0])
                    profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.OK, image, true));
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.LOCAL_IMAGE_NOT_FOUND, e.getMessage(), true));
        }
    }

    private String saveImageLocally(Bitmap profileImage, int userId){
        if(App.getAppContext() == null){
            return "";
        }
        ContextWrapper cw = new ContextWrapper(App.getAppContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();
        if(files != null && files.length > 30){
            File oldestFile = null;
            long oldestDate = Long.MAX_VALUE;
            for(File f: files){
                if(f.lastModified() < oldestDate){
                    oldestFile = f;
                    oldestDate = f.lastModified();
                }
            }
            if(oldestFile != null){
                oldestFile.delete();
            }
        }
        File imagePath = new File(directory,userId + ".jpg");
        try(FileOutputStream fos = new FileOutputStream(imagePath)) {
            profileImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public void postProfilePicture(String token, MutableLiveData<CustomResponse<?>> isPosted, int id){
        /*;
        zadrugaApi.postProfilePicture(token, id, tmpSlicka).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isPosted.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isPosted.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });*/
    }

    public void banUser(String token, MutableLiveData<CustomResponse<?>> isBanned, BanRequest b, int id){
        userApi.banUser(token, id, b).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isBanned.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isBanned.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void unBanUser(String token, MutableLiveData<CustomResponse<?>> isUnBanned, int id){
        userApi.unBanUser(token, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isUnBanned.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isUnBanned.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    private void responseNotSuccessful(int code, MutableLiveData<CustomResponse<?>> res){
        if (code / 100 == 4){
            CustomResponse<?> tmp = res.getValue();
            if(tmp != null){
                tmp.setMessage("Pogresno uneti podaci.");
                res.postValue(tmp);
            }
            else
                res.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, "Pogresno uneti podaci.", false));
        } else if (code / 100 == 5){
            CustomResponse<?> tmp = res.getValue();
            if(tmp != null){
                tmp.setMessage("Greska kod servera.");
                res.postValue(tmp);
            }
            else
                res.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, "Greska kod servera.", false));
        }
    }

    public void getAppliedUsers(String token, MutableLiveData<CustomResponse<?>> applied, int adId){
        Boolean[] isSynced = {false};

        getAppliedUsersLocal(applied, adId, isSynced);
        userApi.getAppliedUsers(token, adId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NotNull Call<List<User>> call, @NotNull Response<List<User>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        applied.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        userDao.insertUsers(response.body());
                        for (User u : response.body()) {
                            appliedDao.insertApplied(new Applied(u.getUserId(), adId));
                        }

                    } else
                        applied.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<User>()));

                } else
                    responseNotSuccessful(response.code(), applied);
            }

            @Override
            public void onFailure(@NotNull Call<List<User>> call, @NotNull Throwable t) {

            }
        });
    }

    private void getAppliedUsersLocal(MutableLiveData<CustomResponse<?>> applied, int adId, Boolean[] isSynced){
        Futures.addCallback(userDao.getAppliedUsers(adId), new FutureCallback<List<User>>() {
            @Override
            public void onSuccess(@Nullable List<User> result) {
                if(result != null){
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            applied.postValue(new CustomResponse<>(CustomResponse.Status.OK, result));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Throwable t) {

            }
        }, executor);
    }

    //Rating
    public void postRating(String token, MutableLiveData<CustomResponse<?>> res, Rating rating){
        //TODO: ne znam sta ce tacno da nam bude response

        ratingApi.postRating(token, rating.getFkRateeId(), rating).enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(@NotNull Call<RatingResponse> call, @NotNull Response<RatingResponse> response) {
                if(response.isSuccessful()){
                    Log.i("postRating", "onResponse: radiiii");
                    ratingDao.insertRating(rating);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RatingResponse> call, @NotNull Throwable t) {
                Log.i("postRating", "onResponse: crklooo");
            }
        });
    }

    public void editRating(String token, MutableLiveData<CustomResponse<?>> res, Rating rating){
        ratingApi.editRating(token, rating.getFkRateeId(), rating).enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(@NotNull Call<RatingResponse> call, @NotNull Response<RatingResponse> response) {
                if(response.isSuccessful()){
                    ratingDao.insertRating(rating);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RatingResponse> call, @NotNull Throwable t) {
                res.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getRatingByUserId(String token, MutableLiveData<CustomResponse<?>> ratings, int rateeId){
        final Boolean[] isSynced = {false};

        getRatingByUserIdLocal(ratings, rateeId, isSynced);
        ratingApi.getRatingByRateeId(token, rateeId).enqueue(new Callback<List<RatingResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<RatingResponse>> call, @NotNull Response<List<RatingResponse>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        synchronized (isSynced[0]){
                            ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                            isSynced[0] = true;
                        }
                        for (RatingResponse r: response.body()) {
                            userDao.insertUser(r.getRater());
                            ratingDao.insertRating(new Rating(r.getRater().getUserId(), r.getRatee(), r.getRating(), r.getComment(), r.getPostTime()));
                        }
                    } else {
                        ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<RatingResponse>()));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RatingResponse>> call, @NotNull Throwable t) {
                ratings.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    private void getRatingByUserIdLocal(MutableLiveData<CustomResponse<?>> ratings, int rateeId, Boolean[] isSynced) {
        Futures.addCallback(ratingDao.getRatingByRatedId(rateeId), new FutureCallback<List<RatingResponse>>() {
            @Override
            public void onSuccess(@Nullable List<RatingResponse> result) {
                synchronized (isSynced[0]){
                    if(result == null){
                        ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<RatingResponse>()));
                    } else {
                        ratings.postValue(new CustomResponse<>(CustomResponse.Status.OK, result));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
                ratings.postValue(new CustomResponse<>(CustomResponse.Status.LOCAL_DB_ERROR, t.getMessage()));
            }
        }, executor);
    }

    public void deleteRating(String token, MutableLiveData<CustomResponse<?>> isDeleted, int raterId, int rateeId){

        ratingApi.deleteRating(token, rateeId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    ratingDao.deleteRating(raterId, rateeId);
                } else
                    responseNotSuccessful(response.code(), isDeleted);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    //Lookup
    public void getAllLocations(String token, MutableLiveData<CustomResponse<?>> locations){
        lookupApi.getAllLocations(token).enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(@NotNull Call<List<Location>> call, @NotNull Response<List<Location>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(locations != null){
                        locations.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    lookupDao.insertLocations(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Location>> call, @NotNull Throwable t) {
                locations.postValue(new CustomResponse<>(CustomResponse.Status.OK, t.getMessage()));
            }
        });
    }

    public void getAllBadges(String token, MutableLiveData<CustomResponse<?>> badges){

        lookupApi.getAllBadges(token).enqueue(new Callback<List<Badge>>() {
            @Override
            public void onResponse(@NotNull Call<List<Badge>> call, @NotNull Response<List<Badge>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(badges != null){
                        badges.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    lookupDao.insertBadges(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Badge>> call, @NotNull Throwable t) {
                badges.postValue(new CustomResponse<>(CustomResponse.Status.OK, t.getMessage()));
            }
        });
    }

    public void getAllTags(String token, MutableLiveData<CustomResponse<?>> tags){

        lookupApi.getAllTags(token).enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(@NotNull Call<List<Tag>> call, @NotNull Response<List<Tag>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(tags != null){ //ako je null onda samo hocemo da napunimo lokalnu bazu, a ko nije null onda hocemo da napunimo i liveData-u
                        tags.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    lookupDao.insertTags(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Tag>> call, @NotNull Throwable t) {
                tags.postValue(new CustomResponse<>(CustomResponse.Status.OK, t.getMessage()));
            }
        });
    }

    public void getFacultiesAndUniversities(String token){
        lookupApi.getAllUniversities(token).enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(@NotNull Call<List<University>> call, @NotNull Response<List<University>> response) {
                if(response.isSuccessful() && response.body() != null){
                    lookupDao.insertUniversities(response.body());
                    lookupApi.getAllFaculties(token).enqueue(new Callback<List<Faculty>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<Faculty>> call, @NotNull Response<List<Faculty>> response) {
                            if(response.isSuccessful() && response.body() != null){
                                List<Faculty> tmpFaculties = response.body();
                                for (Faculty f: tmpFaculties) {
                                    f.setFkUniversityId(f.getUniversity().getUniversityId());
                                }
                                lookupDao.insertFaculties(tmpFaculties);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<Faculty>> call, @NotNull Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<University>> call, @NotNull Throwable t) {
            }
        });
    }

    public void getAllFaculties(String token, MutableLiveData<CustomResponse<?>> faculties){
        lookupApi.getAllFaculties(token).enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(@NotNull Call<List<Faculty>> call, @NotNull Response<List<Faculty>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(faculties != null){
                        faculties.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    List<Faculty> tmpFaculties = response.body();
                    for (Faculty f: tmpFaculties) {
                        f.setFkUniversityId(f.getUniversity().getUniversityId());
                    }
                    lookupDao.insertFaculties(tmpFaculties);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Faculty>> call, @NotNull Throwable t) {
                faculties.postValue(new CustomResponse<>(CustomResponse.Status.OK, t.getMessage()));
            }
        });
    }

    public void getAllUniversities(String token, MutableLiveData<CustomResponse<?>> universities){
        lookupApi.getAllUniversities(token).enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(@NotNull Call<List<University>> call, @NotNull Response<List<University>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(universities != null){
                        universities.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    }
                    lookupDao.insertUniversities(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<University>> call, @NotNull Throwable t) {
                universities.postValue(new CustomResponse<>(CustomResponse.Status.OK, t.getMessage()));
            }
        });
    }

    //Chat
    public void connectToChatServer(MutableLiveData<CustomResponse<?>> isConnected){
        User u = Utility.getLoggedInUser(App.getAppContext());
        QBUser qbUser = new QBUser();
        qbUser.setEmail(u.getEmail());
        qbUser.setPassword(u.getPassword());
        qbUser.setId(u.getUserQbId());
        QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Log.i("connectToChatServer", "braoooo");
                isConnected.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("connectToChatServer", "NE braoooo");
                isConnected.postValue(new CustomResponse<>(CustomResponse.Status.OK, e.getMessage()));
            }
        });
    }

    public void createChat(MutableLiveData<CustomResponse<?>> newChatId, List<Integer> memberIds, int adId){
        if(memberIds.size() < 2){
            newChatId.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, null, "Chat morati imati najmanje 2 clana"));
            return;
        }
        QBChatDialog dialog = new QBChatDialog();
        boolean isPrivate = memberIds.size() == 2;
        if(isPrivate){
            dialog.setType(QBDialogType.PRIVATE);
        }
        else{
            dialog.setType(QBDialogType.GROUP);
        }
        dialog.setOccupantsIds(memberIds);
        //CHAT MORA DA IMA IME!
        dialog.setName("ioeeeeeeeeeeeeee");

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                Log.i("insertChat", "braoooo");
                newChatId.postValue(new CustomResponse<>(CustomResponse.Status.OK, result.getDialogId(), null));
                //saveChatsLocally(result);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("insertChat", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), newChatId);
            }
        });
    }

    private Chat saveChatsLocally(QBChatDialog qbChat){
        Chat chat;
        Utility.ChatType type;
        if(qbChat.getType() == QBDialogType.PRIVATE){
            type = Utility.ChatType.PRIVATE;
        }
        else{
            type = Utility.ChatType.GROUP;
        }//qbChat.getCustomData().getInteger("adId"),
        chat = new Chat(qbChat.getDialogId(), type, "treba ide ime", qbChat.getOccupants().size(),
                qbChat.getCreatedAt());
        chatDao.insertChat(chat);
        for (Integer id: qbChat.getOccupants()) {
            chatDao.insertUserChat(new UserChat(qbChat.getDialogId(), id));
        }
        return chat;
    }

    private List<Chat> saveChatsLocally(List<QBChatDialog> qbChats){
        Utility.ChatType type;
        List<Chat> chats = new ArrayList<>();
        Chat chat;
        for (QBChatDialog qbChat : qbChats) {
            if(qbChat.getType() == QBDialogType.PRIVATE){
                type = Utility.ChatType.PRIVATE;
            }
            else{
                type = Utility.ChatType.GROUP;
            }
            chat = new Chat(qbChat.getDialogId(), type, "treba ide ime", qbChat.getOccupants().size(),
                    qbChat.getCreatedAt());
            chat.setQbChat(qbChat);
            chats.add(chat);
            chatDao.insertChat(chat);
            for (Integer id: qbChat.getOccupants()) {
                chatDao.insertUserChat(new UserChat(qbChat.getDialogId(), id));
            }
        }
        return chats;
    }

    public void getUserByQbId(){

    }

    private Chat qbChatDialogToChat(QBChatDialog qbChat){
        //treba ovde api poziv da bih mogaao da izvucem info o korisniku koji je zadnji poslao poruku
        Chat chat;
        Utility.ChatType type;
        if(qbChat.getType() == QBDialogType.PRIVATE)
            type = Utility.ChatType.PRIVATE;
        else
            type = Utility.ChatType.GROUP;
        chat = new Chat(qbChat.getDialogId(), type, "", qbChat.getOccupants().size(),
                qbChat.getLastMessage(),
                qbChat.getLastMessageUserId(),
                qbChat.getLastMessageDateSent(),qbChat.getCreatedAt(), qbChat);
        return chat;
    }

    private Message qbChatMessageToMessage(QBChatMessage qbMess){
        return new Message(qbMess.getId(), qbMess.getSenderId(),
                new Date(qbMess.getDateSent()), qbMess.getBody(), qbMess.getDialogId(), qbMess);
    }

    private int getChatIndexByQbId(List<Chat> chats, String qbChatId){
        if(chats != null){
            for (int i = 0; i < chats.size(); i++) {
                if(chats.get(i).getChatId().equals(qbChatId)){
                    return i;
                }
            }
        }
        return -1;
    }

    public void getAllChats(MutableLiveData<CustomResponse<?>> chats){
        final String token = Utility.getAccessToken(App.getAppContext());
        int userId = Utility.getUserId(App.getAppContext());
        int userQbId = Utility.getUserQbId(App.getAppContext());
        Boolean[] isSynced = {false};
        getAllChatsLocal(chats, userId, isSynced);

        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(50);
        requestBuilder.sortAsc("last_message_date_sent");

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChats, Bundle params) {
                if(qbChats != null){
                    List<Chat> tmpChats = new ArrayList<>();
                    for (QBChatDialog qbChat : qbChats) {
                        String qbChatId = qbChat.getDialogId();
                        Utility.ChatType type = qbChat.getType() == QBDialogType.PRIVATE ? Utility.ChatType.PRIVATE : Utility.ChatType.GROUP;
                        Chat chat = qbChatDialogToChat(qbChat);
                        tmpChats.add(chat);
                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChats));
                        if(type == Utility.ChatType.PRIVATE){
                            //id osobe sa kojom chetujes
                            int chatterQbId = qbChat.getOccupants().get(0) == userQbId ? qbChat.getOccupants().get(1) : qbChat.getOccupants().get(0);
                            userApi.getProfilePictureByUserQbId(token, chatterQbId).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        Bitmap bmImage = BitmapFactory.decodeStream(response.body().byteStream());
                                        int i;
                                        List<Chat> tmpChatList = (List<Chat>) chats.getValue().getBody();
                                        if(bmImage != null && (i = getChatIndexByQbId(tmpChatList, qbChatId)) != -1){
                                            tmpChatList.get(i).setProfileImage(bmImage);
                                        }
                                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChatList));
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                            userApi.getUserByQbUserId(token, chatterQbId).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        int i;
                                        List<Chat> tmpChatList = (List<Chat>) chats.getValue().getBody();
                                        if((i = getChatIndexByQbId(tmpChatList, qbChatId)) != -1){
                                            tmpChatList.get(i).setChatTitle(response.body().getFirstName() + " " + response.body().getLastName());
                                        }
                                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChatList));
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                        else {
                            adApi.getAdByQbChatId(Utility.getAccessToken(App.getAppContext()), qbChatId).enqueue(new Callback<Ad>() {
                                @Override
                                public void onResponse(Call<Ad> call, Response<Ad> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        int i;
                                        List<Chat> tmpChatList = (List<Chat>) chats.getValue().getBody();
                                        if((i = getChatIndexByQbId(tmpChatList, qbChatId)) != -1){
                                            tmpChatList.get(i).setChatTitle(response.body().getTitle());
                                            tmpChatList.get(i).setFkAdId(response.body().getAdId());
                                        }
                                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChatList));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Ad> call, Throwable t) {

                                }
                            });
                        }
                        Utility.ChatType finalType = type;
                        if(qbChat.getLastMessageUserId() != null){
                            userApi.getUserByQbUserId(token, qbChat.getLastMessageUserId()).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        int i;
                                        List<Chat> tmpChatList = (List<Chat>) chats.getValue().getBody();
                                        if((i = getChatIndexByQbId(tmpChatList, qbChatId)) != -1){
                                            tmpChatList.get(i).setLastSenderName(response.body().getUsername());
                                        }
                                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChatList));
                                    } else
                                        responseNotSuccessful(response.code(), chats);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    chats.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
                                }
                            });
                        }
                    }
                }
                else
                    chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<Chat>()));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("insertChat", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), chats);
            }
        });
    }

    public void getAllChatsLocal(MutableLiveData<CustomResponse<?>> chats, int userId, Boolean[] isSynced){
        Futures.addCallback(chatDao.getAllChatsByUserId(userId), new FutureCallback<List<Chat>>() {
            @Override
            public void onSuccess(@org.jetbrains.annotations.Nullable List<Chat> result) {
                if(result != null)
                    chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, result));
            }

            @Override
            public void onFailure(@NotNull Throwable t) {

            }
        }, executor);
    }

    public void updateChat(MutableLiveData<CustomResponse<?>> chats, String chatId){
        final String token = Utility.getAccessToken(App.getAppContext());
        QBRestChatService.getChatDialogById(chatId).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                userApi.getUserByQbUserId(token, qbChatDialog.getLastMessageUserId()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        boolean isUpdated = false;
                        List<Chat> tmpChatList = (List<Chat>) chats.getValue().getBody();
                        int i;
                        if((i = getChatIndexByQbId(tmpChatList, chatId)) != -1){
                            Chat updatedChat = tmpChatList.get(i);
                            updatedChat.setLastMessage(qbChatDialog.getLastMessage());
                            updatedChat.setLastMessageDateSent(qbChatDialog.getLastMessageDateSent());
                            if(response.isSuccessful() && response.body() != null) {
                                updatedChat.setLastSenderName(response.body().getUsername());
                            }
                            tmpChatList.remove(i);
                            tmpChatList.add(tmpChatList.size() - 1, updatedChat);
                            isUpdated = true;
                        }
                        if(!isUpdated){
                            tmpChatList.add(tmpChatList.size() - 1, qbChatDialogToChat(qbChatDialog));
                        }
                        chats.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpChatList));
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                responseNotSuccessful(e.getHttpStatusCode(), chats);
            }
        });
    }

    public void updateMessages(MutableLiveData<CustomResponse<?>> messages, QBChatMessage qbChatMessage){
        List<Message> tmpList = new ArrayList<>();
        if(messages.getValue() != null){
            tmpList = (List<Message>) messages.getValue().getBody();
        }
        tmpList.add(qbChatMessageToMessage(qbChatMessage));
        messages.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpList));
    }

    public void getChatMembers(MutableLiveData<CustomResponse<?>> chatMembers, QBChatDialog chat) {
        final String token = Utility.getAccessToken(App.getAppContext());
        ChatMembersRequest req = new ChatMembersRequest(chat.getOccupants());
        userApi.getChatMembers(token, req).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatMembers.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    for (User user : response.body()) {
                        int userId = user.getUserId();
                        userDao.insertUser(user);
                        MutableLiveData<CustomResponse<?>> profileImg = new MutableLiveData<>();
                        Observer<CustomResponse<?>> imageObserver = new Observer<CustomResponse<?>>() {
                            @Override
                            public void onChanged(CustomResponse<?> customResponse) {
                                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                                    Bitmap bmImage = (Bitmap) customResponse.getBody();
                                    List<User> users = (List<User>) chatMembers.getValue().getBody();
                                    if (users != null) {
                                        for (int i = 0; i < users.size(); i++) {
                                            if (users.get(i).getUserId() == userId) {
                                                if (bmImage != null)
                                                    users.get(i).setProfileImage(bmImage);
                                                break;
                                            }
                                        }
                                    }
                                    chatMembers.postValue(new CustomResponse<>(CustomResponse.Status.OK, users));
                                }
                                if (!customResponse.isLocal())
                                    profileImg.removeObserver(this);
                            }
                        };
                        profileImg.observeForever(imageObserver);
                        ZadrugaRepository.this.getProfilePicture(profileImg, userId);
                    }
                } else
                    responseNotSuccessful(response.code(), chatMembers);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                chatMembers.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void getAdByChatId(MutableLiveData<CustomResponse<?>> ad, String qbChatId){
        final String token = Utility.getAccessToken(App.getAppContext());
        adApi.getAdByQbChatId(token, qbChatId).enqueue(new Callback<Ad>() {
            @Override
            public void onResponse(Call<Ad> call, Response<Ad> response) {
                if(response.isSuccessful() && response.body() != null){
                    ad.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                    saveAdLocally(response.body());
                } else
                    responseNotSuccessful(response.code(), ad);
            }

            @Override
            public void onFailure(Call<Ad> call, Throwable t) {
                    ad.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void sendMessage(MutableLiveData<CustomResponse<?>> isSent, MutableLiveData<CustomResponse<?>> messages, MutableLiveData<CustomResponse<?>> chats,
                            QBChatDialog qbChat, String message){
        User u = Utility.getLoggedInUser(App.getAppContext());
        QBChatMessage qbMessage = new QBChatMessage();
        qbMessage.setBody(message);
        qbMessage.setSaveToHistory(true);
        qbMessage.setProperty("username", u.getUsername());
        qbMessage.setSenderId(u.getUserQbId());
        qbMessage.setDateSent((new Date()).getTime());
        qbMessage.setDialogId(qbChat.getDialogId());
        qbChat.sendMessage(qbMessage, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                isSent.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                updateMessages(messages, qbMessage);
                updateChat(chats, qbChat.getDialogId());
                //saveMessageLocally(qbMessage);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("sendMessage", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), isSent);
            }
        });
    }

    public void getMessages(MutableLiveData<CustomResponse<?>> messages, QBChatDialog chat, int messagesSkipped, boolean shouldAppend){
        Boolean[] isSynced = {false};
        getMessagesLocally(messages, chat.getDialogId(), isSynced);
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(100);
        messageGetBuilder.setSkip(messagesSkipped);
        QBRestChatService.getDialogMessages(chat, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                List<Message> tmpMsgList;
                if(shouldAppend && messages.getValue() != null && messages.getValue().getBody() != null)
                    tmpMsgList = (List<Message>) messages.getValue().getBody();
                else
                    tmpMsgList = new ArrayList<>();
                for (QBChatMessage qbMsg: qbChatMessages) {
                    tmpMsgList.add(qbChatMessageToMessage(qbMsg));
                }
                messages.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpMsgList));
                messageDao.insertMessages(tmpMsgList);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("sendMessage", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), messages);
            }
        });
    }

    private void getMessagesLocally(MutableLiveData<CustomResponse<?>> messages, String qbChatId, Boolean[] isSynced) {

    }

    public void addOnMessageReceivedGlobal(QBChatDialogMessageListener newMessageListener){
        QBChatService.getInstance().getIncomingMessagesManager().addDialogMessageListener(newMessageListener);
    }

    public void removeGlobalMessageReceivedListener(QBChatDialogMessageListener newMessageListener){
        QBChatService.getInstance().getIncomingMessagesManager().removeDialogMessageListrener(newMessageListener);
    }

    //TODO: je l cemo da brisemo chatove kad bude gotov posa?
    public void deleteChat(){

    }
}
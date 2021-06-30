package com.parovi.zadruga.repository;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.parovi.zadruga.App;
import com.parovi.zadruga.ChatNotification;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.activities.MainActivity;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.Tagged;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserBadge;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserChat;
import com.parovi.zadruga.models.nonEntityModels.AdWithTags;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.models.responseModels.RatingResponse;
import com.parovi.zadruga.ui.ChatActivity;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//VUKOV token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
//TEIN token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g";
//Marko car token = eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTl9.x6y6ONrdWKne_hU11h_tJJUQYQFfBoM6R10o6Ji3ajE
/*
* grupni chat: vuk - 128330407, tea - 128586493,
markocar - 128304620
priv chat: vuk i tea*/
public abstract class BaseRepository {

    public BaseRepository() {
    }

    protected void saveAdsLocally(List<Ad> ads){
        if(ads == null) return;
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                for (Ad a: ads) {
                    if(a.getLocation() != null)
                        a.setFkLocationId(a.getLocation().getLocId());
                    a.setFkEmployerId(a.getEmployer().getUserId());
                    
                    DaoFactory.getLocationDao().insertOrUpdate(a.getLocation());
                    DaoFactory.getTagDao().insertOrUpdate(a.getTags());

                    DaoFactory.getUserDao().insertOrUpdate(a.getEmployer());
                    DaoFactory.getAdDao().insertOrUpdate(a);
                    if(a.getTags() != null) {
                        for (Tag tag : a.getTags()) {
                            DaoFactory.getAdTagDao().insertOrUpdate(new AdTag(a.getAdId(), tag.getTagId()));
                        }
                    }
                }
            }
        });
    }

    protected void saveAdLocally(Ad ad){
        if(ad == null) return;
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                if(ad.getLocation() != null)
                    ad.setFkLocationId(ad.getLocation().getLocId());
                ad.setFkEmployerId(ad.getEmployer().getUserId());

                DaoFactory.getLocationDao().insertOrUpdate(ad.getLocation());
                DaoFactory.getTagDao().insertOrUpdate(ad.getTags());

                DaoFactory.getUserDao().insertOrUpdate(ad.getEmployer());
                DaoFactory.getAdDao().insertOrUpdate(ad);
                if(ad.getTags() != null){
                    for (Tag tag: ad.getTags()) {
                        DaoFactory.getAdTagDao().insertOrUpdate(new AdTag(ad.getAdId(), tag.getTagId()));
                    }
                }
            }
        });
    }

    protected void saveRatingLocally(RatingResponse r){
        if(r == null) return;
        DaoFactory.getUserDao().insertOrUpdate(r.getRater());
        DaoFactory.getRatingDao().insertOrUpdate(new Rating(r.getRater().getUserId(), r.getRatee(), r.getRating(), r.getComment(), r.getPostTime()));
    }

    protected int getListSize(MutableLiveData<CustomResponse<?>> list){//TODO: vidi sta vuk kaze za ovo, da l smo stojke i ja selci il smo doktori
        if(list != null && list.getValue() != null && list.getValue().getBody() != null && list.getValue().getBody() instanceof List)
            return ((List<?>)list.getValue().getBody()).size();
        else
            return 0;
    }

    protected Comment commentResponseToComment(CommentResponse commentResponse){
        if(commentResponse == null) return null;
        return new Comment(commentResponse.getId(), commentResponse.getUser().getUserId(),
                commentResponse.getAd(), commentResponse.getComment(), commentResponse.getPostTime());
    }

    protected void saveCommentLocally(CommentResponse commentResponse) {
        if(commentResponse == null) return;
        Comment tmpLocalComment = commentResponseToComment(commentResponse);
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DaoFactory.getUserDao().insertOrUpdate(commentResponse.getUser());
                DaoFactory.getCommentDao().insert(tmpLocalComment);
                for (Integer index : commentResponse.getTaggedIndices()) {
                    DaoFactory.getTaggedDao().insertOrUpdate(new Tagged(commentResponse.getId(), index));

                }
            }
        });
    }

    protected Ad adWithTagsToAd(AdWithTags adWithTags){
        Ad tmpAd;
        tmpAd = adWithTags.adEmployerLocation.getAd();
        tmpAd.setEmployer(adWithTags.adEmployerLocation.getEmployer());
        tmpAd.setLocation(adWithTags.adEmployerLocation.getLocation());
        tmpAd.setTags(adWithTags.tags);
        return tmpAd;
    }

    protected void saveUserLocally(User remoteUser){
        if(remoteUser == null) return;
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                if(remoteUser.getFaculty() != null){
                    remoteUser.setFkFacultyId(remoteUser.getFaculty().getFacultyId());
                    remoteUser.getFaculty().setFkUniversityId(remoteUser.getFaculty().getUniversity().getUniversityId());
                    if (remoteUser.getFaculty().getUniversity() != null)
                        DaoFactory.getUniversityDao().insertOrUpdate(remoteUser.getFaculty().getUniversity());
                    DaoFactory.getFacultyDao().insertOrUpdate(remoteUser.getFaculty());
                }
                DaoFactory.getUserDao().insertOrUpdate(remoteUser);
                if(remoteUser.getBadges() != null) {
                    DaoFactory.getBadgeDao().insertOrUpdate(remoteUser.getBadges());
                    List<UserBadge> tmpUserBadge = new ArrayList<>();
                    for (Badge b : remoteUser.getBadges()) {
                        tmpUserBadge.add(new UserBadge(remoteUser.getUserId(), b.getBadgeId()));
                    }
                    DaoFactory.getUserBadgeDao().insertOrUpdate(tmpUserBadge);
                }
            }
        });
    }

    protected void saveUserLocally(List<User> remoteUsers){
        if(remoteUsers == null) return;
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                for (User remoteUser: remoteUsers) {
                    if(remoteUser.getFaculty() != null){
                        remoteUser.setFkFacultyId(remoteUser.getFaculty().getFacultyId());
                        remoteUser.getFaculty().setFkUniversityId(remoteUser.getFaculty().getUniversity().getUniversityId());
                        if (remoteUser.getFaculty().getUniversity() != null)
                            DaoFactory.getUniversityDao().insertOrUpdate(remoteUser.getFaculty().getUniversity());
                        DaoFactory.getFacultyDao().insertOrUpdate(remoteUser.getFaculty());
                    }
                    DaoFactory.getUserDao().insertOrUpdate(remoteUser);
                    if(remoteUser.getBadges() != null) {
                        DaoFactory.getBadgeDao().insertOrUpdate(remoteUser.getBadges());
                        List<UserBadge> tmpUserBadge = new ArrayList<>();
                        for (Badge b : remoteUser.getBadges()) {
                            tmpUserBadge.add(new UserBadge(remoteUser.getUserId(), b.getBadgeId()));
                        }
                        DaoFactory.getUserBadgeDao().insertOrUpdate(tmpUserBadge);
                    }
                }
            }
        });
    }

    protected void getProfilePicture(MutableLiveData<CustomResponse<?>> profilePicture, int userId){
        final String token = Utility.getAccessToken(App.getAppContext());
        Boolean[] isSynced = {false};
        getProfilePictureLocal(profilePicture, userId, isSynced);
        ApiFactory.getUserApi().getProfileImage(token, userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        synchronized (isSynced[0]) {
                            profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.OK, bmp, false));
                            isSynced[0] = true;
                        }
                        saveImageLocally(bmp, userId);
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

    protected void getProfilePictureLocal(MutableLiveData<CustomResponse<?>> profilePicture, int userId, Boolean[] isSynced){
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

    protected void saveChatLocally(Chat chat){
        if(chat == null) return;
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {

                DaoFactory.getChatDao().insertOrUpdate(chat);
                DaoFactory.getUserChatDao().insertOrUpdate(new UserChat(chat.getChatId(), Utility.getLoggedInUserQbId(App.getAppContext())));
                /*for (Integer id: qbChat.getOccupants()) {
                    DaoFactory.getUserChatDao().insertOrUpdate(new UserChat(qbChat.getDialogId(), id));
                }*/
            }
        });
    }

    protected Chat qbChatToChat(QBChatDialog qbChat){
        Chat chat;
        Utility.ChatType type;
        if (qbChat.getType() == QBDialogType.PRIVATE) {
            type = Utility.ChatType.PRIVATE;
        } else {
            type = Utility.ChatType.GROUP;
        }
        chat = new Chat(qbChat.getDialogId(), type, qbChat.getOccupants().size(), qbChat.getLastMessage(), qbChat.getLastMessageDateSent(),
                qbChat.getCreatedAt(), qbChat);
        return chat;
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
        dialog.setName("ime chat-a");

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                saveChatLocally(qbChatToChat(result));
                newChatId.postValue(new CustomResponse<>(CustomResponse.Status.OK, result.getDialogId()));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("insertChat", "NE braoooo");
                responseNotSuccessful(e.getHttpStatusCode(), newChatId);
            }
        });
    }
    
    protected Bitmap getProfilePictureLocal(int userId){
        if(App.getAppContext() == null){
            return null;
        }
        ContextWrapper cw = new ContextWrapper(App.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        Bitmap image = null;
        try {
            File f = new File(directory.getAbsolutePath(), userId + ".jpg");
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    protected String saveImageLocally(Bitmap profileImage, int userId){
        if(App.getAppContext() == null && profileImage != null){
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

    public void logOutUser(MutableLiveData<CustomResponse<?>> isLoggedOut){
        Utility.getExecutorService().execute(() -> {
            try {
                QBUsers.signOut().perform();
                String fcmToken;
                if((fcmToken = Utility.getFcmToken(App.getAppContext())) != null){
                    Response<ResponseBody> deleteTokenRes = ApiFactory.getUserApi().deleteFcmToken(Utility.getAccessToken(App.getAppContext()),
                            fcmToken).execute();
                    if(deleteTokenRes.isSuccessful()){
                        isLoggedOut.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                        Utility.removeLoggedUserInfo(App.getAppContext());
                    } else
                        responseNotSuccessful(deleteTokenRes.code(), isLoggedOut);
                }
            } catch (QBResponseException e) {
                e.printStackTrace();
                responseNotSuccessful(e.getHttpStatusCode(), isLoggedOut);
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), isLoggedOut);
            }
        });
    }

    protected void responseNotSuccessful(int code, MutableLiveData<CustomResponse<?>> res){
        if (code / 100 == 4){
            if(code == 408){
                MutableLiveData<CustomResponse<?>> isLoggedOut = new MutableLiveData<>();
                Observer<CustomResponse<?>> observer = new Observer<CustomResponse<?>>() {
                    @Override
                    public void onChanged(CustomResponse<?> customResponse) {
                        if(customResponse.getStatus() == CustomResponse.Status.OK){
                            Intent i = new Intent(App.getAppContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getAppContext().startActivity(i);
                        }
                        isLoggedOut.removeObserver(this);
                    }
                };
                isLoggedOut.observeForever(observer);
                logOutUser(isLoggedOut);
            } else {
                CustomResponse<?> tmp = res.getValue();
                if(tmp != null){
                    tmp.setMessage("Pogresno uneti podaci.");
                    res.postValue(tmp);
                }
                else
                    res.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, "Pogresno uneti podaci.", false));
            }
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

    protected void apiCallOnFailure(String message, MutableLiveData<CustomResponse<?>> res){
        CustomResponse<?> tmp = res.getValue();
        if(tmp != null){
            tmp.setMessage(message);
            tmp.setStatus(CustomResponse.Status.SERVER_ERROR);
            res.postValue(tmp);
        }
        else
            res.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, message));
    }

    protected void sendChatNotification(ChatNotification notification){
        String token = Utility.getAccessToken(App.getAppContext());
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> res = ApiFactory.getNotificationApi().sendChatNotification(token, notification).execute();
                    if(res.isSuccessful())
                        Log.i("sendChatNotification", "success");
                    else
                        Log.i("sendChatNotification", res.message());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
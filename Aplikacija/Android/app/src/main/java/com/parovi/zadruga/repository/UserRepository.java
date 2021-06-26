package com.parovi.zadruga.repository;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Applied;
import com.parovi.zadruga.models.nonEntityModels.UserWithFaculty;
import com.parovi.zadruga.models.requestModels.BanRequest;
import com.parovi.zadruga.models.requestModels.ChangePasswordRequest;
import com.parovi.zadruga.models.requestModels.AddFcmTokenRequest;
import com.parovi.zadruga.models.requestModels.LoginRequest;
import com.parovi.zadruga.models.responseModels.LoginResponse;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository extends BaseRepository {
    
    public UserRepository(){
        super();
    }

    public void setFcmToken(String token, int userId, String fcmToken){
        User user = new User();
        user.setUserId(userId);
        user.setFcmToken(fcmToken);
        ApiFactory.getUserApi().updateUser(token, userId, user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful())
                    Log.i("updateUser", "onResponse: ");
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.i("updateUser", "crkoo: ");
            }
        });
    }

    public void removeFcmToken(String token, int userId){
        
        User user = new User();
        user.setUserId(userId);
        user.setFcmToken("");
        ApiFactory.getUserApi().updateUser(token, userId, user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful())
                    Log.i("updateUser", "onResponse: ");
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {

            }
        });
    }

    public void registerUser(MutableLiveData<CustomResponse<?>> newUser, User u, String pass){
        //TODO: da l treba da se obestava korisnik sta je tacno problem
        QBUser qbUser = new QBUser();
        qbUser.setLogin(u.getUsername());
        qbUser.setEmail(u.getEmail());
        qbUser.setPassword(pass);

        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Log.i("qbSignIn", "braoooo");
                u.setUserQbId(user.getId());
                ApiFactory.getUserApi().registerUser(u).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                        if(response.isSuccessful() && response.body() != null){
                            Log.i("signIn", "braoooo");
                            newUser.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                            qbUser.setExternalId(Integer.toString(response.body().getUserId()));
                            QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                    Log.i("updateQbUser", "onSuccess: ");
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    Log.i("sadfa", "onError: ");
                                }
                            });
                            DaoFactory.getUserDao().insertOrUpdate(response.body());
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

    public void loginUser(MutableLiveData<CustomResponse<?>> isEmployer, String email, String pass){
        //TODO: ovde treba da vuku posaljes fcmToken, kako bi znao kome da salje notif
        //TODO: kazi mu ne moz se ulogujes
        ApiFactory.getUserApi().loginUser(new LoginRequest(email, pass)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    User loggedInUser = response.body().getUser();
                    QBUser qbUser = new QBUser();
                    qbUser.setEmail(email);
                    qbUser.setPassword(pass);

                    QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser user, Bundle args) {
                            Log.i("logIn", "braoooo");
                            isEmployer.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body().getUser().isEmployer()));
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("FETCH_FCM_TOKEN_FAILED", "Fetching FCM registration token failed", task.getException());
                                        isEmployer.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, "Fetching FCM registration token failed"));
                                        return;
                                    }
                                    Utility.saveLoggedUserInfo(App.getAppContext(), response.body().getToken(), response.body().getUser(), pass, task.getResult());
                                    ApiFactory.getUserApi().postFcmToken(response.body().getToken(), new AddFcmTokenRequest(task.getResult()))
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                    if(response.isSuccessful())
                                                        Log.i("postFcmToken", "OK");
                                                    else
                                                        Log.i("postFcmToken", String.valueOf(response.code()));
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                    Log.i("postFcmToken", t.getMessage());
                                                }
                                            });
                                }
                            });
                            saveUserLocally(loggedInUser);
                        }

                        @Override
                        public void onError(QBResponseException error) {
                            Log.i("logIn", "ne braoooo");
                            isEmployer.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, error.getMessage()));
                        }
                    });
                }
                else
                    responseNotSuccessful(response.code(), isEmployer);
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Log.i("logIn", "ne braoooo");
                isEmployer.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void logOutUser(MutableLiveData<CustomResponse<?>> isLoggedOut){
        /*if (QBSessionManager.getInstance().getSessionParameters() == null) {
            return;
        }*/
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.i("logOut", "braoooo");
                ApiFactory.getUserApi().deleteFcmToken(Utility.getAccessToken(App.getAppContext()), Utility.getFcmToken(App.getAppContext()))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    isLoggedOut.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                                    Utility.removeLoggedUserInfo(App.getAppContext());
                                } else
                                    responseNotSuccessful(response.code(), isLoggedOut);

                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                isLoggedOut.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
                            }
                        });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("logOut", "ne braoooo");
                isLoggedOut.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, e.getMessage()));
            }
        });
    }

    public void changePassword(String token, MutableLiveData<CustomResponse<?>> isChanged, int qbId, String oldPass, String newPass){
        ApiFactory.getUserApi().changePassword(token, new ChangePasswordRequest(oldPass, newPass)).enqueue(new Callback<ResponseBody>() {
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
                            Utility.setLoggedInUserPassword(App.getAppContext(), newPass);
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
        ApiFactory.getUserApi().getUserById(token, id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    User tmpUser = response.body();
                    synchronized (isSynced[0]){
                        user.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                    }
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveUserLocally(tmpUser);
                        }
                    });
                } else
                    responseNotSuccessful(response.code(), user);
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), user);
            }
        });
    }

    private void getUserByIdLocal(MutableLiveData<CustomResponse<?>> user, int id, Boolean[] isSynced){
        Futures.addCallback(DaoFactory.getBadgeDao().getUserBadges(id), new FutureCallback<List<Badge>>() {
            @Override
            public void onSuccess(List<Badge> badges) {
                Futures.addCallback(DaoFactory.getUserDao().getUserWithFaculty(id), new FutureCallback<UserWithFaculty>() {
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
                }, Utility.getExecutor());
            }

            @Override
            public void onFailure(@NotNull Throwable t) {
            }
        }, Utility.getExecutor());
    }

    //kad mi saljes user-a nek on bude popunjen i novim podacima koje hoces da menjas, a i ostalim podacima koje nisi menjala/o
    public void updateUser(String token, MutableLiveData<CustomResponse<?>> isUpdated, User user){
        ApiFactory.getUserApi().updateUser(token, user.getUserId(), user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful()){
                    isUpdated.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            //User tmpUser = DaoFactory.getUserDao().getUserById(user.getUserId());
                            DaoFactory.getUserDao().insertOrUpdate(user);
                        }
                    });
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
                apiCallOnFailure(t.getMessage(), profilePicture);
            }
        });
    }

    public void getProfilePictureLocal(MutableLiveData<CustomResponse<?>> profilePicture, int userId, Boolean[] isSynced){
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

    public void postProfilePicture(MutableLiveData<CustomResponse<?>> profilePicture, Uri imageUri){
        String token = Utility.getAccessToken(App.getAppContext());
        int id = Utility.getLoggedInUserId(App.getAppContext());
        InputStream in;
        byte[] buff;
        try {
            in = App.getAppContext().getContentResolver().openInputStream(imageUri);
            buff = new byte[in.available()];
            while (in.read(buff) != -1);
        } catch (IOException e) {
            e.printStackTrace();
            profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.EXCEPTION_ERROR, e.getMessage()));
            return;
        }
        RequestBody imageRequest = RequestBody.create(MediaType.parse("application/octet-stream"), buff);
        ApiFactory.getUserApi().postProfilePicture(token, id, imageRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.OK, bmp));
                        saveImageLocally(bmp, Utility.getLoggedInUserId(App.getAppContext()));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                profilePicture.postValue(new CustomResponse<>(CustomResponse.Status.SERVER_ERROR, t.getMessage()));
            }
        });
    }

    public void banUser(String token, MutableLiveData<CustomResponse<?>> isBanned, BanRequest b, int id){
        ApiFactory.getUserApi().banUser(token, id, b).enqueue(new Callback<ResponseBody>() {
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
        ApiFactory.getUserApi().unBanUser(token, id).enqueue(new Callback<ResponseBody>() {
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

    public void getAppliedUsers(String token, MutableLiveData<CustomResponse<?>> applied, int adId){
        Boolean[] isSynced = {false};
        getAppliedUsersLocal(applied, adId, isSynced);
        ApiFactory.getUserApi().getAppliedUsers(token, adId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NotNull Call<List<User>> call, @NotNull Response<List<User>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        applied.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        Utility.getExecutorService().execute(new Runnable() {
                            @Override
                            public void run() {
                                DaoFactory.getUserDao().insertOrUpdate(response.body());
                                for (User u : response.body()) {
                                    DaoFactory.getAppliedDao().insertOrUpdate(new Applied(u.getUserId(), adId));
                                }
                            }
                        });

                    } else
                        applied.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<User>()));

                } else
                    responseNotSuccessful(response.code(), applied);
            }

            @Override
            public void onFailure(@NotNull Call<List<User>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), applied);
            }
        });
    }

    private void getAppliedUsersLocal(MutableLiveData<CustomResponse<?>> applied, int adId, Boolean[] isSynced){
        Futures.addCallback(DaoFactory.getUserDao().getAppliedUsers(adId), new FutureCallback<List<User>>() {
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
        }, Utility.getExecutor());
    }

    private void updateApplied(List<Ad> finishedJobs){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                int id = Utility.getLoggedInUserId(App.getAppContext());
                for (Ad ad : finishedJobs){
                    DaoFactory.getAppliedDao().insertOrUpdate(new Applied(id, ad.getAdId(), true));
                }
            }
        });
    }

    public void getFinishedJobsByUserId(MutableLiveData<CustomResponse<?>> finishedJobs){
        /* adId, adTitle, postTime, ocena*/
        Boolean[] isSynced = new Boolean[]{ false };
        getFinishedJobsByUserIdLocal(finishedJobs, isSynced);
        ApiFactory.getUserApi().getUserAds(Utility.getAccessToken(App.getAppContext()), Utility.getLoggedInUserId(App.getAppContext()))
            .enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(@NotNull Call<List<Ad>> call, @NotNull Response<List<Ad>> response) {
                if(response.isSuccessful() && response.body() != null){
                    synchronized (isSynced[0]){
                        finishedJobs.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                        saveAdsLocally(response.body());
                        updateApplied(response.body());
                    }
                } else
                    responseNotSuccessful(response.code(), finishedJobs);
            }

            @Override
            public void onFailure(@NotNull Call<List<Ad>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), finishedJobs);
            }
        });
    }

    private void getFinishedJobsByUserIdLocal(MutableLiveData<CustomResponse<?>> finishedJobs, Boolean[] isSynced){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<Ad> tmpList;
                if((tmpList = DaoFactory.getUserDao().getFinishedJobsByUserId(Utility.getLoggedInUserId(App.getAppContext()))) != null){
                    synchronized (isSynced){
                        if(!isSynced[0])
                            finishedJobs.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpList));
                    }
                }
            }
        });
    }

    public void getPostedAdsByUserIdLocal(MutableLiveData<CustomResponse<?>> postedAds){
        Boolean[] isSynced = new Boolean[]{ false };
        getPostedAdsByUserIdLocal(postedAds, isSynced);
        ApiFactory.getUserApi().getUserAds(Utility.getAccessToken(App.getAppContext()), Utility.getLoggedInUserId(App.getAppContext()))
            .enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(@NotNull Call<List<Ad>> call, @NotNull Response<List<Ad>> response) {
                if(response.isSuccessful() && response.body() != null){
                    synchronized (isSynced[0]){
                        postedAds.postValue(new CustomResponse<>(CustomResponse.Status.OK, response.body()));
                        isSynced[0] = true;
                        saveAdsLocally(response.body());
                    }
                } else
                    responseNotSuccessful(response.code(), postedAds);
            }

            @Override
            public void onFailure(@NotNull Call<List<Ad>> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), postedAds);
            }
        });
    }

    private void getPostedAdsByUserIdLocal(MutableLiveData<CustomResponse<?>> postedAds, Boolean[] isSynced){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<Ad> tmpList;
                if((tmpList = DaoFactory.getUserDao().getPostedAdsByUserId(Utility.getLoggedInUserId(App.getAppContext()))) != null){
                    synchronized (isSynced){
                        if(!isSynced[0])
                            postedAds.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpList));
                    }
                }
            }
        });
    }
}
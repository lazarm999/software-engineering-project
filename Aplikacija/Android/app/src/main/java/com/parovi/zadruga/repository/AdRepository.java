package com.parovi.zadruga.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.GpsTracker;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.PreferredTag;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Applied;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.nonEntityModels.AdWithTags;
import com.parovi.zadruga.models.nonEntityModels.CommentWithUser;
import com.parovi.zadruga.models.requestModels.ChooseApplicantsRequest;
import com.parovi.zadruga.models.requestModels.CommentRequest;
import com.parovi.zadruga.models.requestModels.EditAdRequest;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.models.responseModels.AdResponse;
import com.parovi.zadruga.models.responseModels.CommentResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdRepository extends BaseRepository {

    public AdRepository(){
        super();
    }

    public void postAd(String token, MutableLiveData<CustomResponse<?>> isPosted, PostAdRequest ad){
        ApiFactory.getAdApi().postAd(token, ad).enqueue(new Callback<Ad>() {
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
                apiCallOnFailure(t.getMessage(), isPosted);
            }
        });
    }

    public void getAd(String token, MutableLiveData<CustomResponse<?>> ad, int id){
        final Boolean[] isSynced = {false};
        getAdLocal(ad, id, isSynced);
        ApiFactory.getAdApi().getAd(token, id).enqueue(new Callback<Ad>() {
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
                apiCallOnFailure(t.getMessage(), ad);
            }
        });
    }

    public void getAdLocal(MutableLiveData<CustomResponse<?>> ad, int id,  Boolean[] isSynced){
        Futures.addCallback(DaoFactory.getAdDao().getAdWithTagsById(id), new FutureCallback<AdWithTags>() {
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
        }, Utility.getExecutor());
    }

    //TODO: kad da dodajem ove podatke iz lookup tebela u lokalnu bazu?
    public void getAds(MutableLiveData<CustomResponse<?>> ads) {
        String token = Utility.getAccessToken(App.getAppContext());
        final Boolean[] isSynced = {false};
        getAdsLocal(ads, isSynced);
        ApiFactory.getAdApi().getAds(token).enqueue(new Callback<List<Ad>>() {
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
                apiCallOnFailure(t.getMessage(), ads);
            }
        });
    }

    public void getAdsLocal(MutableLiveData<CustomResponse<?>> ads, Boolean[] isSynced){
        Futures.addCallback(DaoFactory.getAdDao().getAds(), new FutureCallback<List<AdWithTags>>() {
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
        }, Utility.getExecutor());
    }

    //kad neces da se sortira po nekom parametru samo prosledis null na tom mestu
    public void getAds(String token, MutableLiveData<CustomResponse<?>> ads, Integer locId, Integer compensationMin, Integer compensationMax, List<Integer> tagIds,
                       boolean sortByLocation) {
        if((compensationMin == null && compensationMax != null) || (compensationMin != null && compensationMax == null)) {
            responseNotSuccessful(400, ads);
            return;
        }
        final Boolean[] isSynced = {false};
        if(sortByLocation){
            GpsTracker gpsTracker = new GpsTracker(App.getAppContext());
            MutableLiveData<CustomResponse<?>> location = new MutableLiveData<>();
            Observer<CustomResponse<?>> observer = new Observer<CustomResponse<?>>() {
                @Override
                public void onChanged(CustomResponse<?> customResponse) {
                    Double latitude = null;
                    Double longitude = null;
                    if(customResponse.getStatus() == CustomResponse.Status.OK && customResponse.getBody() != null){
                        latitude = ((android.location.Location) customResponse.getBody()).getLatitude();
                        longitude = ((android.location.Location) customResponse.getBody()).getLongitude();
                        Log.i("location", "Location fetched");
                    }
                    getAdsLocal(ads, isSynced, locId, compensationMin, compensationMax, tagIds, latitude, longitude);
                    ApiFactory.getAdApi().getAds(token, locId, compensationMin, compensationMax, tagIds, latitude, longitude).enqueue(new Callback<List<Ad>>() {
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
                            apiCallOnFailure(t.getMessage(), ads);
                        }
                    });
                }
            };
            location.observeForever(observer);
            gpsTracker.getLocation(location);
        } else {
            getAdsLocal(ads, isSynced, locId, compensationMin, compensationMax, tagIds, null, null);
            ApiFactory.getAdApi().getAds(token, locId, compensationMin, compensationMax, tagIds, null, null).enqueue(new Callback<List<Ad>>() {
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
                    apiCallOnFailure(t.getMessage(), ads);
                }
            });
        }
    }

    public void getAdsLocal(MutableLiveData<CustomResponse<?>> ads, Boolean[] isSynced, Integer locId, Integer compensationMin, Integer compensationMax, List<Integer> tagIds,
                            Double latitude, Double longitude){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                String queryString = "";
                List<Object> args = new ArrayList<>();
                boolean isSorting = false, filterByLoc = false;
                if(latitude != null && longitude != null){
                    queryString += "SELECT Ad.*, user_table.*, Location.*, " +
                            "(Location.latitude - ?)*(Location.latitude - ?) + (Location.longitude - ?)*(Location.longitude - ?) AS distance " +
                            "FROM Ad " +
                            "INNER JOIN user_table ON user_table.userId = Ad.fkEmployerId " +
                            "LEFT JOIN Location ON Location.locId = Ad.fkLocationId";
                    args.add(latitude);
                    args.add(latitude);
                    args.add(longitude);
                    args.add(longitude);
                    isSorting = true;
                } else {
                    queryString += "SELECT Ad.*, user_table.*, Location.* " +
                            "FROM Ad " +
                            "INNER JOIN user_table ON user_table.userId = Ad.fkEmployerId " +
                            "LEFT JOIN Location ON Location.locId = Ad.fkLocationId";
                }
                if(locId != null || compensationMin != null || compensationMax != null)
                    queryString += " WHERE";
                if(locId != null){
                    queryString += " Ad.fkLocationId = ?";
                    args.add(locId);
                    filterByLoc = true;
                }
                if(compensationMin != null && compensationMax != null){
                    if (filterByLoc) {
                        queryString += " AND";
                    }

                    queryString += " Ad.compensationMin >= ? AND Ad.compensationMax <= ?";
                    args.add(compensationMin);
                    args.add(compensationMax);
                }

                if(isSorting) queryString += " ORDER BY distance ASC";
                queryString += ";";
                List<AdWithTags> adWithTagsList = DaoFactory.getAdDao().getAds(new SimpleSQLiteQuery(queryString, args.toArray()));
                List<Ad> finalList = new ArrayList<>();
                boolean shouldRemove;
                if(adWithTagsList != null){
                    for (AdWithTags adWithTags: adWithTagsList) {
                        shouldRemove = true;
                        if(adWithTags.tags != null && tagIds != null){
                            for (Tag tag: adWithTags.tags) {
                                for (Integer tagId: tagIds) {
                                    if(tag.getTagId() == tagId){
                                        shouldRemove = false;
                                        break;
                                    }
                                    if(!shouldRemove) break;
                                }
                            }
                            if(!shouldRemove)
                                finalList.add(adWithTagsToAd(adWithTags));
                        }
                    }
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            ads.postValue(new CustomResponse<>(CustomResponse.Status.OK, finalList));
                    }
                }
            }
        });
    }

    public void applyForAd(String token, MutableLiveData<CustomResponse<?>> isApplied, int userId, int adId){
        ApiFactory.getAdApi().applyForAd(token, adId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    isApplied.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    Utility.getExecutorService().execute(new Runnable() {
                        @Override
                        public void run() {
                            //DaoFactory.getAppliedDao().insertOrUpdate(new Applied(userId, adId, false));
                        }
                    });
                }else {
                    isApplied.postValue(new CustomResponse<>(CustomResponse.Status.BAD_REQUEST, "Pogresno uneti podaci"));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isApplied);
            }
        });
    }

    public void chooseApplicants(MutableLiveData<CustomResponse<?>> isSucc, int adId, List<User> chosenUsers){
        final String token = Utility.getAccessToken(App.getAppContext());
        int employerQbUserId = Utility.getLoggedInUserQbId(App.getAppContext());
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
                    ApiFactory.getAdApi().chooseApplicants(token, adId, new ChooseApplicantsRequest(userIds, (String)customResponse.getBody()))
                            .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                isSucc.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                                Utility.getExecutorService().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (Integer i: userIds) {
                                            DaoFactory.getAppliedDao().insertOrUpdate(new Applied(i, adId, false));
                                        }
                                        DaoFactory.getAdDao().updateQbChatId((String)customResponse.getBody(), adId);
                                    }
                                });
                            } else
                                responseNotSuccessful(response.code(), isSucc);
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            apiCallOnFailure(t.getMessage(), isSucc);
                        }
                    });
                }
                newQbChatId.removeObserver(this);
            }
        };
        newQbChatId.observeForever(observer);
        createChat(newQbChatId, qbUserIds, adId);
    }

    public void unApplyForAd(String token, MutableLiveData<CustomResponse<?>> isUnApplied, int userId, int adId){
        ApiFactory.getAdApi().unApplyForAd(token, adId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    isUnApplied.postValue(new CustomResponse<>(CustomResponse.Status.OK, false));
                    DaoFactory.getAppliedDao().deleteApplied(new Applied(userId, adId));
                } else
                    responseNotSuccessful(response.code(), isUnApplied);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isUnApplied);
            }
        });
    }

    public void editAd(String token, MutableLiveData<CustomResponse<?>> isSucc, int adId, EditAdRequest ad){
        ApiFactory.getAdApi().editAd(token, adId, ad).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isSucc.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    editAdLocal(ad, adId);
                }
                else
                    responseNotSuccessful(response.code(), isSucc);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isSucc);
            }
        });
    }

    private void editAdLocal(EditAdRequest ad, int adId){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Ad tmpLocalAd = new Ad(adId, ad.getTitle(), ad.getDescription(),
                        ad.getCompensationMin(),
                        ad.getCompensationMax(),
                        ad.getNumberOfEmployees());
                DaoFactory.getAdDao().insertOrUpdate(tmpLocalAd);
                if(ad.getRemoveTags() != null){
                    for (Integer i: ad.getRemoveTags()) {
                        DaoFactory.getAdTagDao().deleteAdTag(i, adId);
                    }
                }
                if(ad.getAddTags() != null){
                    for (Integer i: ad.getAddTags()) {
                        DaoFactory.getAdTagDao().insertOrUpdate(new AdTag(adId, i));
                    }
                }
            }
        });
    }

    public void deleteAd(String token, MutableLiveData<CustomResponse<?>> isSucc, int adId){
        ApiFactory.getAdApi().deleteAd(token, adId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isSucc.postValue(new CustomResponse<>(CustomResponse.Status.OK, true));
                    DaoFactory.getAdDao().deleteAdById(adId);
                } else
                    responseNotSuccessful(response.code(), isSucc);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isSucc);
            }
        });
    }

    public void postComment(String token, MutableLiveData<CustomResponse<?>> comments, int adId, String comment){
        int userId = Utility.getLoggedInUserId(App.getAppContext());
        CommentRequest c = new CommentRequest(comment);
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<CommentResponse> commentResponse = ApiFactory.getCommentApi().postComment(token, adId, c).execute();
                    if (commentResponse.isSuccessful() && commentResponse.body() != null) {
                        ArrayList<CommentResponse> tmpCommentList = new ArrayList<>();
                        if(comments.getValue() != null && comments.getValue().getBody() != null)
                            tmpCommentList = (ArrayList<CommentResponse>) comments.getValue().getBody();
                        int userId = commentResponse.body().getUser().getUserId();
                        Bitmap localImage = getProfilePictureLocal(userId);
//                        if(localImage != null){
//                            commentResponse.body().setUserImage(localImage);
//                            comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, commentResponse.body()));
//                        }
                        Response<ResponseBody> responseImage = ApiFactory.getUserApi().getProfileImage(token, userId).execute();
                        if(responseImage.isSuccessful() && responseImage.body() != null) {
                            Bitmap bmImage = BitmapFactory.decodeStream(responseImage.body().byteStream());
                            commentResponse.body().setUserImage(bmImage);
                            saveImageLocally(bmImage, userId);
                        }
                        tmpCommentList.add(commentResponse.body());
                        comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpCommentList));
                    } else
                        responseNotSuccessful(commentResponse.code(), comments);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getComments(MutableLiveData<CustomResponse<?>> comments, int adId){
        final String token = Utility.getAccessToken(App.getAppContext());
        Boolean[] isSynced = {false};
        getCommentsLocal(comments, adId, isSynced);
        Utility.getExecutorService().execute(() -> {
            try {
                Response<List<CommentResponse>> responseComments = ApiFactory.getCommentApi().getComments(token, adId).execute();
                if(responseComments.isSuccessful()) {
                    if (responseComments.body() != null) {
                        for (CommentResponse comment: responseComments.body()) {
                            Response<ResponseBody> responseImage = ApiFactory.getUserApi().getProfileImage(token, comment.getUser().getUserId()).execute();
                            if(responseImage.isSuccessful() && responseImage.body() != null) {
                                Bitmap bmImage = BitmapFactory.decodeStream(responseImage.body().byteStream());
                                comment.setUserImage(bmImage);
                                saveImageLocally(bmImage, comment.getUser().getUserId());
                            }
                            synchronized (isSynced[0]){
                                comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, responseComments.body()));
                                isSynced[0] = true;
                            }
                            saveCommentLocally(comment);
                        }
                    } else {
                        synchronized (isSynced[0]){
                            comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, new ArrayList<CommentResponse>()));
                            isSynced[0] = true;
                        }
                    }
                } else
                    responseNotSuccessful(responseComments.code(), comments);

            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), comments);
            }
        });

    }

    private CommentResponse commentWithUserToCommentResponse(CommentWithUser commentWithUser){
        Comment tmpComment = commentWithUser.getComment();
        return new CommentResponse(tmpComment.getCommentId(), tmpComment.getFkAdId(),
                tmpComment.getComment(), tmpComment.getPostTime(), commentWithUser.getUser());
    }

    private void getCommentsLocal(MutableLiveData<CustomResponse<?>> comments, int adId, Boolean[] isSynced){
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<CommentWithUser> localComments = DaoFactory.getCommentDao().getCommentsByAdId(adId);
                List<CommentResponse> commentResponseList = new ArrayList<>();
                CommentResponse commentResponse = new CommentResponse();
                if(localComments != null){
                    for (CommentWithUser commentWithUser : localComments) {
                        commentResponse = commentWithUserToCommentResponse(commentWithUser);
                        Bitmap profileImage = getProfilePictureLocal(commentWithUser.getUser().getUserId());
                        commentResponse.setUserImage(profileImage);
                        List<Integer> taggedIndices = DaoFactory.getTaggedDao().getTaggedByCommentId(commentWithUser.getComment().getCommentId());
                        commentResponse.setTaggedIndices(taggedIndices);
                        commentResponseList.add(commentResponse);
                    }
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            comments.postValue(new CustomResponse<>(CustomResponse.Status.OK, commentResponseList));
                    }
                }
            }
        });
    }

    public void deleteComment(String token, MutableLiveData<CustomResponse<?>> isDeleted, int commentId, int pos){
        ApiFactory.getCommentApi().deleteComment(token, commentId).enqueue(new Callback<AdResponse>() {
            @Override
            public void onResponse(@NotNull Call<AdResponse> call, @NotNull Response<AdResponse> response) {
                if(response.isSuccessful()){
                    DaoFactory.getCommentDao().deleteComment(commentId);
                    isDeleted.postValue(new CustomResponse<>(CustomResponse.Status.OK, pos));
                }
                else
                    responseNotSuccessful(response.code(), isDeleted);
            }

            @Override
            public void onFailure(@NotNull Call<AdResponse> call, @NotNull Throwable t) {
                apiCallOnFailure(t.getMessage(), isDeleted);
            }
        });
    }

    public void getRecommendedAds(MutableLiveData<CustomResponse<?>> ads){
        Utility.getExecutorService().execute(() -> {
            try {
                List<PreferredTag> preferredTags = DaoFactory.getPreferredTagDao().getPreferredTags();
                List<Integer> tagIds = preferredTagsToListInteger(preferredTags);
                String token = Utility.getAccessToken(App.getAppContext());
                Boolean[] isSynced = {false};
                int pageSkip = getListSize(ads);
                getRecommendedAdsLocal(ads, isSynced, pageSkip, tagIds);
                Response<List<Ad>> adsResponse = ApiFactory.getAdApi().getRecommendedAds(token, tagIds,
                        Constants.pageSize, pageSkip).execute();
                if(adsResponse.isSuccessful()){
                    if(adsResponse.body() != null){
                        synchronized (isSynced[0]){
                            ads.postValue(new CustomResponse<>(CustomResponse.Status.OK, adsResponse.body()));
                            isSynced[0] = true;
                        }
                        saveAdsLocally(adsResponse.body());
                    }
                } else
                    responseNotSuccessful(adsResponse.code(), ads);
                Log.i("ne", "run: ");
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), ads);
            }
        });
    }

    private List<Integer> preferredTagsToListInteger(List<PreferredTag> preferredTags) {
        if(preferredTags == null) return null;
        List<Integer> tagIds = new ArrayList<>();
        for (PreferredTag preferredTag : preferredTags)
            tagIds.add(preferredTag.getTagId());
        return tagIds;
    }

    private void getRecommendedAdsLocal(MutableLiveData<CustomResponse<?>> ads, Boolean[] isSynced, int pageSkip, List<Integer> tagIds) {
        Utility.getExecutorService().execute(() -> {
            List<Ad> localAds = DaoFactory.getAdDao().getAds(Constants.pageSize, pageSkip, tagIds);
            if(localAds != null){
                synchronized (isSynced[0]){
                    if(!isSynced[0])
                        ads.postValue(new CustomResponse<>(CustomResponse.Status.OK, localAds));
                }
            }
        });
    }

    public void isApplied(MutableLiveData<CustomResponse<?>> isApplied, int adId){
        final String token = Utility.getAccessToken(App.getAppContext());
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Boolean> isAppliedRes = ApiFactory.getAdApi().isApplied(token, adId).execute();
                    if(isAppliedRes.isSuccessful()){
                        if(isAppliedRes.body() != null)
                            isApplied.postValue(new CustomResponse<>(CustomResponse.Status.OK, isAppliedRes.body()));
                    } else
                        responseNotSuccessful(isAppliedRes.code(), isApplied);
                } catch (IOException e) {
                    e.printStackTrace();
                    apiCallOnFailure(e.getMessage(), isApplied);
                }
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
}

package com.parovi.zadruga.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.messaging.RemoteMessage;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.PushNotification;
import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.daos.TmpDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.database.ZadrugaDatabase;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.TmpPost;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.models.manyToManyModels.AdWithTags;
import com.parovi.zadruga.models.manyToManyModels.UserWithBadges;
import com.parovi.zadruga.models.oneToManyModels.AdsOnLocation;
import com.parovi.zadruga.models.AdWithLocation;
import com.parovi.zadruga.retrofit.NotificationApi;
import com.parovi.zadruga.retrofit.ZadrugaApi;

import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZadrugaRepository {
    private static ZadrugaRepository instance;
    private AdDao adDao;
    private LocationDao locationDao;
    private TmpDao tmpDao;
    private UserDao userDao;
    private Application app;

    private ZadrugaApi zadrugaApi;
    private NotificationApi notificationApi;

    private LiveData<List<AdsOnLocation>> adsWithLocation;

    private Executor executor;

    public static ZadrugaRepository getInstance(Application app) {
        if (instance == null) {
            instance = new ZadrugaRepository(app);
        }
        return instance;
    }

    private ZadrugaRepository(Application app) {
        ZadrugaDatabase db = ZadrugaDatabase.getInstance(app);
        this.app = app;
        adDao = db.adDao();
        locationDao = db.locationDao();
        tmpDao = db.tmpDao();
        userDao = db.userDao();
        executor = MoreExecutors.directExecutor();

        Retrofit backend = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit notifications = new Retrofit.Builder()
                .baseUrl(Constants.FCM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        zadrugaApi = backend.create(ZadrugaApi.class);
        notificationApi = notifications.create(NotificationApi.class);
    }

    public Call<ResponseBody> sendRemoteMessage(RemoteMessage message){
        return notificationApi.sendNotification(message);
    }

    public Call<ResponseBody> sendPushNotification(PushNotification notification){
        return notificationApi.sendPushNotification(notification);
    }

    //Ad
    public void insertAd(Ad ad) {
        adDao.insertAd(ad);
    }

    public LiveData<List<Ad>> getAllAds() {
        return adDao.getAllAds();
    }

    public LiveData<List<AdWithLocation>> getAllAdsWithLocation() {
        return adDao.getAllAdsWithLocation();
    }

    public LiveData<List<AdWithTags>> getAllAdsWithTags() {
        return adDao.getAllAdsWithTags();
    }

    //tmp
    public void deleteAllPosts() {
        tmpDao.deleteAllPosts();
    }

    public LiveData<List<TmpPost>> getAllPosts() {
        MutableLiveData<List<TmpPost>> posts = new MutableLiveData<>();
        Futures.addCallback(tmpDao.getAllPosts(),
                new FutureCallback<List<TmpPost>>() {
                    public void onSuccess(List<TmpPost> result) {
                        posts.postValue(result);
                        zadrugaApi.getPosts().enqueue(new Callback<List<TmpPost>>() {
                            @Override
                            public void onResponse(Call<List<TmpPost>> call, Response<List<TmpPost>> response) {
                                if (response.isSuccessful()) {
                                    posts.setValue(response.body());
                                    tmpDao.insertPosts(response.body());
                                }
                            }
                            @Override
                            public void onFailure(Call<List<TmpPost>> call, Throwable t) {

                            }
                        });
                    }

                    public void onFailure(Throwable t) {
                    }
                }, executor);
        return posts;
    }

    public LiveData<TmpPost> getPostById(int id){
        MutableLiveData<TmpPost> post = new MutableLiveData<>();
        Futures.addCallback(tmpDao.getPostById(id),
                new FutureCallback<TmpPost>() {
                    public void onSuccess(TmpPost result) {
                        post.postValue(result);
                    }

                    public void onFailure(Throwable t) {
                    }
                }, executor);
        return post;
    }

    //Location
    public void insertLocation(Location location) {
        locationDao.insertLocation(location);
    }

    public LiveData<List<Location>> getAllLocations() {
        return locationDao.getAllLocations();
    }

    //User
    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public LiveData<List<UserWithBadges>> getAllUsersWithBadges() {

        return userDao.getAllUsersWithBadges();
    }

    public LiveData<List<Ad>> getAdById() {
        return adDao.getAdById(520);
    }


    private static class InsertLocationAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDao locationDao;

        private InsertLocationAsyncTask(LocationDao adDao) {
            this.locationDao = adDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.insertLocation(locations[0]);
            return null;
        }
    }

    public void populateDb() {
        /*adDao.insertAd(new Ad("naslov1", "opis1", 1));
        adDao.insertAd(new Ad("naslov2", "opis2", 2));
        adDao.insertAd(new Ad("naslov3", "opis3", 1));
        adDao.insertAd(new Ad("naslov4", "opis4", 2));

        locationDao.insertLocation(new Location("nis"));
        locationDao.insertLocation(new Location("beograd"));

        tmpDao.insertTag(new Tag("basta"));
        tmpDao.insertTag(new Tag("kuca"));
        tmpDao.insertTag(new Tag("informatika"));
        tmpDao.insertTag(new Tag("baustela"));

        tmpDao.insertBadge(new Badge("pcelica"));
        tmpDao.insertBadge(new Badge("omiljeni radnik"));
        tmpDao.insertBadge(new Badge("secikesa"));
        tmpDao.insertBadge(new Badge("indijac"));

        adDao.insertAdTagCrossRef(new AdTagCrossRef(1, 2));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(1, 3));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(2, 4));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(3, 2));
        adDao.insertAdTagCrossRef(new AdTagCrossRef(3, 1));*/
    }
}

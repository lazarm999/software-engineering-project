package com.parovi.zadruga.repository;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.PushNotification;
import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.daos.TmpDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.databases.ZadrugaDatabase;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.TmpPost;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.nonEntityModels.AdsOnLocation;
import com.parovi.zadruga.retrofit.NotificationApi;
import com.parovi.zadruga.retrofit.ZadrugaApi;
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

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZadrugaRepository  {
    private static ZadrugaRepository instance;
    private final ZadrugaDatabase localDb;
    private final FirebaseFirestore firebaseDb;
    private final AdDao adDao;
    private final LocationDao locationDao;
    private final TmpDao tmpDao;
    private final UserDao userDao;
    private final ZadrugaApi zadrugaApi;
    private final NotificationApi notificationApi;
    private QBChatService chatService;
    private QBIncomingMessagesManager incomingMessagesManager;

    private LiveData<List<AdsOnLocation>> adsWithLocation;

    private final Executor executor;

    public synchronized static ZadrugaRepository getInstance(Application app) {
        if (instance == null) {
            instance = new ZadrugaRepository(app);
        }
        return instance;
    }

    private ZadrugaRepository(Application app) {
        localDb = ZadrugaDatabase.getInstance(app);
        firebaseDb = FirebaseFirestore.getInstance();
        adDao = localDb.adDao();
        locationDao = localDb.locationDao();
        tmpDao = localDb.tmpDao();
        userDao = localDb.userDao();
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
        chatService = QBChatService.getInstance();
        incomingMessagesManager = chatService.getIncomingMessagesManager();
    }

    //Notification
    public Call<ResponseBody> sendPushNotification(PushNotification notification){
        return notificationApi.sendPushNotification(notification);
    }

    //Ad
    public void insertAd(Ad ad) {
        adDao.insertAd(ad);
    }

    public MutableLiveData<List<Ad>> getAllAds() {
        MutableLiveData<List<Ad>> ads = new MutableLiveData<>();
        Futures.addCallback(adDao.getAllAds(),
                new FutureCallback<List<Ad>>() {
                    public void onSuccess(List<Ad> result) {
                        ads.postValue(result);
                    }

                    public void onFailure(Throwable t) {

                    }
                }, executor);
        return ads;
    }

    //tmp
    public void deleteAllPosts() {
        tmpDao.deleteAllPosts();
    }

    public void getAllPosts(MutableLiveData<List<TmpPost>> posts) {
        Futures.addCallback(tmpDao.getAllPosts(),
                new FutureCallback<List<TmpPost>>() {
                    public void onSuccess(List<TmpPost> result) {
                        posts.postValue(result);
                        zadrugaApi.getPosts().enqueue(new Callback<List<TmpPost>>() {
                            @Override
                            public void onResponse(Call<List<TmpPost>> call, Response<List<TmpPost>> response) {
                                if (response.isSuccessful()) {
                                    posts.setValue(response.body());
                                    Futures.addCallback(tmpDao.insertPosts(response.body()), new FutureCallback<List<Long>>() {
                                        @Override
                                        public void onSuccess(List<Long> result) {
                                            if(true){
                                                Log.i("radi", "radi");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            if(true){
                                                Log.i("neradi", "neradi");
                                            }
                                        }
                                    }, executor);
                                }
                            }
                            @Override
                            public void onFailure(Call<List<TmpPost>> call, Throwable t) {
                                if(true){
                                    Log.i("radi", "radi");
                                }
                            }
                        });
                    }

                    public void onFailure(Throwable t) {
                        if(true){
                            Log.i("radi", "radi");
                        }
                    }
                }, executor);
    }

    //Location
    public void insertLocation(Location location) {
        locationDao.insertLocation(location);
    }

    public LiveData<List<Location>> getAllLocations() {
        MutableLiveData<List<Location>> locations = new MutableLiveData<>();
        Futures.addCallback(locationDao.getAllLocations(), new FutureCallback<List<Location>>() {
            @Override
            public void onSuccess(List<Location> result) {
                locations.postValue(result);
            }

            @Override
            public void onFailure(Throwable t) {
                locations.postValue(null);
            }
        }, executor);
        return locations;
    }
    public LiveData<Location> getLocationById(int locId){
        MutableLiveData<Location> location = new MutableLiveData<>();
        Futures.addCallback(locationDao.getLocationById(locId), new FutureCallback<Location>() {
            @Override
            public void onSuccess(Location result) {
                location.postValue(result);
            }

            @Override
            public void onFailure(Throwable t) {
                location.postValue(null);
            }
        }, executor);
        return location;
    }

    //User

    public LiveData<Integer> updateUserFcmToken(User user){
        MutableLiveData<Integer> response = new MutableLiveData<>();
        zadrugaApi.patchUser(user).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> res) {
                if(res.isSuccessful()){
                    Futures.addCallback(userDao.updateUserFcmToken(user.getUserId(), user.getFcmToken(), true),
                            new FutureCallback<Integer>() {
                                @Override
                                public void onSuccess(@NullableDecl Integer result) {
                                    if(result!= null && result == 1){
                                        response.postValue(result);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            }, executor);

                } else { //TODO: Sta da radim sto se ovaj kod ponavalja 2 puta i u onFailure i ovde?
                    Futures.addCallback(userDao.updateUserFcmToken(user.getUserId(), user.getFcmToken(), false),
                            new FutureCallback<Integer>() {
                                @Override
                                public void onSuccess(@NullableDecl Integer result) {
                                    if(result!= null && result == 1){
                                        response.postValue(result);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {//TODO: Sta da radim ako ne sacuva lepo u lokalnoj bazi?
                                }
                            }, executor);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Futures.addCallback(userDao.updateUserFcmToken(user.getUserId(), user.getFcmToken(), false),
                        new FutureCallback<Integer>() {
                            @Override
                            public void onSuccess(@NullableDecl Integer result) {
                                if(result!= null && result == 1){
                                    response.postValue(result);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {//TODO: Sta da radim ako ne sacuva lepo u lokalnoj bazi?
                            }
                        }, executor);
            }
        });
        return response;
    }


    public LiveData<Integer> updateUser(User user){
        MutableLiveData<Integer> status = new MutableLiveData<>();
        Futures.addCallback(userDao.updateUser(user), new FutureCallback<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    status.postValue(result);
                }

                @Override
                public void onFailure(Throwable t) {
                    status.postValue(-1);
                }
            }, executor);
        return status;
    }

    public LiveData<Long> insertUser(User user) {
        MutableLiveData<Long> status = new MutableLiveData<>();
        Futures.addCallback(userDao.insertUser(user), new FutureCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                status.postValue(result);
            }

            @Override
            public void onFailure(Throwable t) {
                status.postValue(-1L);
            }
        }, executor);
        return status;
    }

    public void insertQBUser(MutableLiveData<Boolean> isSignedUp, User u, String pass){
        QBUser qbUser = new QBUser();
        qbUser.setLogin(u.getUsername());
        qbUser.setPassword(pass);
        qbUser.setExternalId(Integer.toString(15));//TODO: kad dobijes id od vuka ovde mozes da ga sacuvas
        //TODO: za qbUser sifra mora bude duza od 8 karaktera
        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Log.i("signIn", "braoooo");
                isSignedUp.postValue(true);
            }

            @Override
            public void onError(QBResponseException error) {
                Log.i("signIn", "NE braoooo");
                isSignedUp.postValue(false);
            }
        });
    }

    public void logInQBUser(MutableLiveData<Boolean> isLoggedIn, User u, String pass){
        QBUser qbUser = new QBUser();
        qbUser.setLogin(u.getUsername());
        qbUser.setPassword(pass);

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                Log.i("logIn", "braoooo");
                isLoggedIn.postValue(true);
            }

            @Override
            public void onError(QBResponseException error) {
                Log.i("logIn", "ne braoooo");
                isLoggedIn.postValue(false);

            }
        });
    }

    public MutableLiveData<Boolean> logOutQBUser(User u){
        MutableLiveData<Boolean> isLoggedOut = new MutableLiveData<>();
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                QBAuth.deleteSession().performAsync(new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        Log.i("logOut", "braoooo");
                        isLoggedOut.postValue(true);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.i("logOut", "ne braoooo");
                        isLoggedOut.postValue(false);
                    }
                });

            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("logOut", "ne braoooo");
                isLoggedOut.postValue(false);

            }
        });
        return isLoggedOut;
    }

    //Chat
    //TODO: ovde mora sifra da se ukuca, da probam da odradim ovaj connect na pocetku kad se uloguje pa da vidimo kako ce da radi da ne bih sifru cuvao
    public void connectToChatServer(MutableLiveData<Boolean> isConnected, User u){
        QBUser qbUser = new QBUser();
        qbUser.setLogin(u.getUsername());
        qbUser.setPassword("sifra123");
        qbUser.setId(u.getQbId());
        QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                Log.i("connectToChatServer", "braoooo");
                QBChatService.getInstance().setReconnectionAllowed(true);
                isConnected.postValue(true);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.i("connectToChatServer", "NE braoooo");
                isConnected.postValue(false);
            }
        });
    }

    public MutableLiveData<QBChatDialog> insertChat(ArrayList<Integer> ids, boolean isPrivate){
        MutableLiveData<QBChatDialog> chat = new MutableLiveData<>();
        QBChatDialog dialog = new QBChatDialog();
        if(isPrivate)
            dialog.setType(QBDialogType.PRIVATE);
        else
            dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(ids);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                Log.i("insertChat", "braoooo");
                chat.postValue(result);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.i("insertChat", "NE braoooo");
                chat.postValue(null);
            }
        });
        return chat;
    }

    public MutableLiveData<Boolean> joinGroupChat(){
        MutableLiveData<Boolean> bool = new MutableLiveData<>();
        return bool;
    }

    public void getAllChats(MutableLiveData<List<QBChatDialog>> chats){
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(50);
        requestBuilder.sortAsc("last_message_date_sent");

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> result, Bundle params) {
                chats.postValue(result);
            }

            @Override
            public void onError(QBResponseException responseException) {
                chats.postValue(new ArrayList<>());
            }
        });
    }
    //TODO: je l cemo da brisemo chatove kad bude gotov posa?
    public void deleteChat(){

    }

    public MutableLiveData<QBChatDialog> getChatById(String chatId){
        MutableLiveData<QBChatDialog> chat = new MutableLiveData<>();
        QBRestChatService.getChatDialogById(chatId).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                chat.setValue(qbChatDialog);
            }

            @Override
            public void onError(QBResponseException e) {
                chat.setValue(null);
            }
        });
        return chat;
    }

    public void getMessages(MutableLiveData<List<QBChatMessage>> messages, QBChatDialog chat){
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(100);
        QBRestChatService.getDialogMessages(chat, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                messages.postValue(qbChatMessages);
            }

            @Override
            public void onError(QBResponseException e) {
                messages.postValue(new ArrayList<>());
            }
        });
    }

    public void onGlobalMessageReceived(QBChatDialogMessageListener newMessageListener){
        incomingMessagesManager.addDialogMessageListener(newMessageListener);
    }

    public void removeGlobalMessageReceivedListener(QBChatDialogMessageListener newMessageListener){
        incomingMessagesManager.removeDialogMessageListrener(newMessageListener);
    }

    //TODO: ovo ce vimo da l ce koristis
    public MutableLiveData<QBChatMessage> onChatMessageReceived(QBChatDialog chat){
        MutableLiveData<QBChatMessage> newMessage = new MutableLiveData<>();
        chat.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
                newMessage.postValue(qbChatMessage);
            }

            @Override
            public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer senderId) {
                newMessage.postValue(null);
            }
        });
        return newMessage;
    }

    public void sendMessage(MutableLiveData<Boolean> isSent, QBChatDialog chat, QBChatMessage message){
        chat.sendMessage(message, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                isSent.postValue(true);
            }

            @Override
            public void onError(QBResponseException e) {
                isSent.postValue(false);
            }
        });
    }

    //FIREBASE
    public LiveData<List<Message>> getMessages(String chatId) {
        MutableLiveData<List<Message>> messages = new MutableLiveData<>();
        firebaseDb.collection("chats").document(chatId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                value.toObject(Message.class);
                if (error == null) {
                    Boolean bool = false;
                    if ((bool = (Boolean) value.get("isArchived")) != null) {

                    }
                }
            }
        });
        return messages;
    }

    public LiveData<String> insertChat(Chat chat){
        MutableLiveData<String> newChatId = new MutableLiveData<>();
        firebaseDb.collection("chats").add(chat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference docRef) {
                newChatId.postValue(docRef.getId());
                firebaseDb.collection("chats").document(docRef.getId()).collection("messages");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                newChatId.postValue(null);
            }
        });
        return newChatId;
    }

    public LiveData<String> insertMessage(Message message, String chatId){
        MutableLiveData<String> messageId = new MutableLiveData<>();
        firebaseDb.collection("chats").document(chatId).collection(Constants.MESSAGES).add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference docRef) {
                messageId.postValue(docRef.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                messageId.postValue(null);
            }
        });
        return messageId;
    }

    public LiveData<Boolean> archiveChat(String chatId){
        MutableLiveData<Boolean> status = new MutableLiveData<>();
        Map<String, Boolean> isArchived = new HashMap<>();
        isArchived.put(Constants.IS_ARCHIVED, true);
        firebaseDb.collection("chats").document(chatId).set(isArchived, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                status.postValue(false);
            }
        });
        return status;
    }

    public void populateDb(Activity activity) {
        /*Futures.addCallback(adDao.insertAd(new Ad("naslov1", "opis1", 1)),
                new FutureCallback<Long>() {
                    @Override
                    public void onSuccess(Long result) {
                        //Toast.makeText(activity, "uspesno", Toast.LENGTH_SHORT).show();
                        Log.i("populateDb", "uspesno");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        //Toast.makeText(activity, "neuspesno", Toast.LENGTH_SHORT).show();
                        Log.i("populateDb", "neuspesno");
                    }
                }, executor);*/

        /*Futures.addCallback(userDao.insertUser(new User("milorad")),
                new FutureCallback<Long>() {
                    @Override
                    public void onSuccess(Long result) {
                        //Toast.makeText(activity, "uspesno", Toast.LENGTH_SHORT).show();
                        Log.i("populateDb", "uspesno");
                        Futures.addCallback(locationDao.insertLocation(new Location("nis")),
                                new FutureCallback<Long>() {
                                    @Override
                                    public void onSuccess(Long result) {
                                        //Toast.makeText(activity, "uspesno", Toast.LENGTH_SHORT).show();
                                        Log.i("populateDb", "uspesno");
                                        Futures.addCallback(adDao.insertAd(new Ad("naslov1", "opis1", 1)),
                                                new FutureCallback<Long>() {
                                                    @Override
                                                    public void onSuccess(Long result) {
                                                        //Toast.makeText(activity, "uspesno", Toast.LENGTH_SHORT).show();
                                                        Log.i("populateDb", "uspesno");
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        //Toast.makeText(activity, "neuspesno", Toast.LENGTH_SHORT).show();
                                                        Log.i("populateDb", "neuspesno");
                                                    }
                                                }, executor);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        //Toast.makeText(activity, "neuspesno", Toast.LENGTH_SHORT).show();
                                        Log.i("populateDb", "neuspesno");
                                    }
                                }, executor);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        //Toast.makeText(activity, "neuspesno", Toast.LENGTH_SHORT).show();
                        Log.i("populateDb", "neuspesno");
                    }
                }, executor);*/

        /*adDao.insertAd(new Ad("naslov2", "opis2", 2));
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

    public void addDataFireStore(){
        Ad ad = new Ad("naslov", "opis", 2);
        Map<String, Object> mapica = new HashMap<>();
        /*firebaseDb.collection("kolekcija").document("jfslkadg").set(mapica)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("radi", "radi fb");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("neradi", "neradi fb");
                    }
                });*/
        firebaseDb.collection("chats").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Log.i("radi", "radi fb");
                }
            }
        });
    }

    public void insertDataFireStore(){
        Map<String, Boolean> mapica = new HashMap<>();
        mapica.put("isArchived", Boolean.FALSE);
        firebaseDb.collection("chats").document("RoHmmv1wbZTBNGIJi7gp").set(mapica, SetOptions.merge());
    }
}

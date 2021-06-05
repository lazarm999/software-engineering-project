package com.parovi.zadruga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parovi.zadruga.adapters.NotificationsAdapter;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.nonEntityModels.CommentUser;
import com.parovi.zadruga.models.requestModels.EditAdRequest;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.models.responseModels.LoginResponse;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;
import com.parovi.zadruga.viewModels.LoginViewModel;
import com.parovi.zadruga.viewModels.TmpViewModel;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.LogLevel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**/
public class TmpActivity extends AppCompatActivity {

    /*
    * TODO:
    *  EMPLOYEE
    *  {
            "email": "pera@gmail.com",
            "password": "123"
        }
       EMPLOYER
    *  {
            "email": "vuk.bibic@gmail.com",
            "password": "123"
        }
        * ADMIN
    *  {
            "email": "admin@gmail.com",
            "password": "123"
        }
        *
        *
    * */

    private TmpViewModel tmpViewModel;
    private LoginViewModel loginViewModel;
    private ChatViewModel chatViewModel;
    private ZadrugaRepository rep;

    private String topic = "svi";
    private ArrayList<Notification> notificationList;

    private RecyclerView rvNotifications;
    //cuva niz objekata koje zelis da prikazes i brine o tome da se samo pojedini cuvaju u memoriji a ne cela lista (samo oni koji su trenutno prikazani)
    private NotificationsAdapter rvaNotifications;
    private RecyclerView.LayoutManager rvlmNotifications;

    private ArrayList<CommentUser> commentsList;

    private SwipeRefreshLayout srlNotifications;
    private int tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        tmp = 2;
        tmpViewModel = new ViewModelProvider(this).get(TmpViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        rep = ZadrugaRepository.getInstance(getApplication());
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        FirebaseMessaging.getInstance().subscribeToTopic("nekaDrugaTopic");
        Button tmpBtn1 = (Button) findViewById(R.id.tmpBtn1);
        Button tmpBtn2 = (Button) findViewById(R.id.tmpBtn2);
        Button tmpBtn3 = (Button) findViewById(R.id.tmpBtn3);
        Button tmpBtn4 = (Button) findViewById(R.id.tmpBtn4);
        Button btnPostComment =  (Button) findViewById(R.id.btnPostComment);
        EditText etComment = (EditText) findViewById(R.id.etComment);
        TextView tvTmp= (TextView)  findViewById(R.id.tvTmp);
        ImageView tmpImageView = (ImageView) findViewById(R.id.tmpImageView);

        /*MutableLiveData<CustomResponse<?>> loginData = new MutableLiveData<>();
        loginData.observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> response) {
                if(response != null){
                    if(response.getStatus() == CustomResponse.Status.OK){
                        CustomResponse<LoginResponse> loginResponse = (CustomResponse<LoginResponse>)  response;
                        Log.i("loginResponse", "bravooo");
                        saveLoggedUserInfo(loginResponse.getBody().getId(), loginResponse.getBody().getQbId(),loginResponse.getBody().getToken());
                    } else if (response.getStatus() == CustomResponse.Status.BAD_REQUEST) {
                        CustomResponse<String> loginResponse = (CustomResponse<String>)  response;
                        Log.i("loginResponse", loginResponse.getBody());
                    }
                }
            }
        });*/

        /*rep.getAllBadges(getAccessToken(), new MutableLiveData<>());
        rep.getAllLocations(getAccessToken(), new MutableLiveData<>());
        rep.getAllTags(getAccessToken(), new MutableLiveData<>());
        rep.getFacultiesAndUniversities(getAccessToken());*/

        MutableLiveData<CustomResponse<?>> comments = new MutableLiveData<>();
        comments.observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> response) {
                if(response != null){
                    if(response.getStatus() == CustomResponse.Status.OK){

                    } else if (response.getStatus() == CustomResponse.Status.BAD_REQUEST) {
                    }
                }
            }
        });

        tmpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//etComment.getText().toString())
                //TODO: za qbUser sifra mora bude duza od 8 karaktera
                //rep.loginUser(loginData, new User("vuk.bibic@gmail.com", "sifra123"));
                //rep.getUserById(getAccessToken(), new MutableLiveData<>(), 1);
                //rep.populateData();
                //rep.getProfilePicture(getAccessToken(), image, 3);
                //rep.getAd(getAccessToken(), new MutableLiveData<>(), 24);
                /*ArrayList<Integer> intList = new ArrayList<>();
                intList.add(1);
                intList.add(2);
                intList.add(3);
                rep.postAd(getAccessToken(), new MutableLiveData<>(), new PostAdRequest("opsan naslov", "valjda radi ovo", 50,
                        250, 12, 1, intList));*/
                /*ArrayList<Integer> intList = new ArrayList<>();
                intList.add(21);
                rep.chooseApplicants(getAccessToken(), new MutableLiveData<>(), 11, intList);*/
                //rep.applyForAd(getAccessToken(), new MutableLiveData<>(), 20, 12);
                //rep.deleteAd(getAccessToken(), new MutableLiveData<>(), 29);
                //rep.getComments(getAccessToken(), new MutableLiveData<>(), 18);
                //rep.postRating(getAccessToken(), new MutableLiveData<>(), new Rating(1, 3, 5, "braooo opasna si"));
                /*ArrayList<Integer> addList = new ArrayList<>();
                addList.add(3);
                ArrayList<Integer> removeList = new ArrayList<>();
                removeList.add(1);
                removeList.add(2);
                EditAdRequest tmp = new EditAdRequest("promenjetN", "promenjtoo", 12, 35,
                        7, 1, addList, removeList);
                rep.editAd(getAccessToken(), new MutableLiveData<>(), 28, tmp);*/
                //rep.getAppliedUsers(getAccessToken(), new MutableLiveData<>(), 6);
                //rep.loginUser(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar"));
                /*List<Chat> tmpChats = (List<Chat>) chats.getValue().getBody();
                Chat tmpChat = tmpChats.get(0);
                rep.getMessages(new MutableLiveData<>(), tmpChat.getQbChat(), 0);*/
                //rep.loginUser(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar"));
                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
                rep.getComments(token, comments, 4);
            }
        });
        tmpBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: za qbUser sifra mora bude duza od 8 karaktera
                rep.registerUser(new MutableLiveData<>(), new User("markocari", "fnjkdsnl", "dlkfhaslkjh", "markocar@gmail.com", "markocar"));
                //rep.loginUser(new MutableLiveData<>(), new User("vuk.bibic@gmail.com", "sifra123"));
                //rep.logOutUser(loggedOut);
                //rep.getUserById(getAccessToken(), user, 19);
                /*User u = new User();
                u.setBio("ovo je novi bio");
                u.setPhoneNumber("1223456799");
                u.setUserId(19);
                rep.updateUser(getAccessToken(), new MutableLiveData<>(), u);*/
                //rep.banUser(getAccessToken(), new MutableLiveData<>(),  new Ban("jedan velikii baaaan"), 2);
                //rep.unBanUser(getAccessToken(), new MutableLiveData<>(), 3);//nt raterId, int rateeId, Rating rating
                //rep.editRating(getAccessToken(), new MutableLiveData<>(), new Rating(getUserId(), 3, 1, "jako slabo uradjeno"));
                //rep.getRatingByUserId(getAccessToken(), new MutableLiveData<>(), 11);
                //rep.postProfilePicture(getAccessToken(), new MutableLiveData<>(), 1);
                //rep.getAds(getAccessToken(), new MutableLiveData<>());
                //rep.unApplyForAd(getAccessToken(), new MutableLiveData<>(), 20, 28);
                //rep.postComment(getAccessToken(), new MutableLiveData<>(), 18, 1, "gaaaaaaaaaaaas");
                //rep.deleteComment(getAccessToken(), new MutableLiveData<>(), 5);
                //rep.changePassword(getAccessToken(), new MutableLiveData<>(), 	128330407, "sifra123", "novaaasifraaaa");
                //rep.deleteRating(getAccessToken(), new MutableLiveData<>(), 1, 3);
                //rep.getAd(getAccessToken(), new MutableLiveData<>(), 28);
                //rep.loginUser(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar"));
                //rep.connectToChatServer(new MutableLiveData<>(), new User("vuk.bibic@gmail.com", "novaaasifraaaa", 128330407));
                //rep.connectToChatServer(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar", 128304620));
                /*List<Chat> tmpChats = (List<Chat>) chats.getValue().getBody();
                rep.sendMessage(new MutableLiveData<>(), tmpChats.get(0).getQbChat(), new User(), "porukaa novaaaaa");*/
            }
        });
        tmpBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ArrayList<Integer> list = new ArrayList<>();
                list.add(128330407);
                rep.createChat(new MutableLiveData<>(), list, true, 28, 1);*/
                rep.connectToChatServer(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar", 128304620));
            }
        });
        tmpBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //LOGIKA OKO CHAT-A
        //128119046 user2 id

        /*User u = new User("user1");
        u.setQbId(128119044);
        tmp = 0;

        loginViewModel.observeIsLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggedIn) {
                if(isLoggedIn){
                    Toast.makeText(TmpActivity.this, "login uspesan", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(TmpActivity.this, "login nije uspesan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatViewModel.observeChats().observe(this, new Observer<List<QBChatDialog>>() {
            @Override
            public void onChanged(List<QBChatDialog> qbChatDialogs) {
                if(qbChatDialogs != null){
                    tvTmp.setText(qbChatDialogs.toString());
                }
            }
        });

        chatViewModel.observeNewMessages().observe(this, new Observer<QBChatMessage>() {
            @Override
            public void onChanged(QBChatMessage message) {
                Log.i("dialogId", message.getDialogId());
            }
        });

        chatViewModel.observeIsConnected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if(isConnected){
                    Toast.makeText(TmpActivity.this, "connect uspesan", Toast.LENGTH_SHORT).show();
                    chatViewModel.getAllChats();
                    chatViewModel.onGlobalMessageReceived();
                } else {
                    Toast.makeText(TmpActivity.this, "connect nije uspesan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //loginViewModel.logInQBUser(u, "sifra123");



        tmpBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatViewModel.connectToChatServer(u);
            }
        });*/

        /*tmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpViewModel.logInUser(u, "sifra123").observe(TmpActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoggedIn) {
                        if(isLoggedIn){
                            Toast.makeText(TmpActivity.this, "login uspesan", Toast.LENGTH_SHORT).show();
                            tmpViewModel.connectToChatServer(u).observe(TmpActivity.this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean isConnected) {
                                    if(isConnected){
                                        Toast.makeText(TmpActivity.this, "chat server uspesan", Toast.LENGTH_SHORT).show();
                                        tmp++;

                                        QBChatService chatService = QBChatService.getInstance();
                                        QBIncomingMessagesManager incomingMessagesManager = chatService.getIncomingMessagesManager();
                                        Toast.makeText(TmpActivity.this, Boolean.toString( QBChatService.getInstance().isLoggedIn()), Toast.LENGTH_SHORT).show(); chatService.isLoggedIn();
                                        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
                                            @Override
                                            public void processMessage(String dialogID, QBChatMessage qbChatMessage, Integer senderID) {
                                                if(qbChatMessage != null){
                                                    tvTmp.setText(qbChatMessage.getBody());
                                                }
                                            }

                                            @Override
                                            public void processError(String dialogID, QBChatException e, QBChatMessage qbChatMessage, Integer senderID) {
                                            }
                                        });
                                        QBChatDialog chat = new QBChatDialog();
                                        chat.setDialogId("60a193f0ce5b15002dcf5c17");

                                        chat.addMessageListener(new QBChatDialogMessageListener() {
                                            @Override
                                            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                                                if(qbChatMessage != null){
                                                    tvTmp.setText(qbChatMessage.getBody());
                                                }
                                            }

                                            @Override
                                            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                                                if(qbChatMessage != null){
                                                    tvTmp.setText(qbChatMessage.getBody());
                                                }
                                            }
                                        });
                                        tmpViewModel.getAllChats().observe(TmpActivity.this, new Observer<List<QBChatDialog>>() {
                                            @Override
                                            public void onChanged(List<QBChatDialog> qbChatDialogs) {
                                                if(qbChatDialogs != null && qbChatDialogs.size() != 0){
                                                    tmpViewModel.onChatMessageReceived().observe(TmpActivity.this, new Observer<QBChatMessage>() {
                                                        @Override
                                                        public void onChanged(QBChatMessage qbChatMessage) { if(qbChatMessage != null){
                                                                tvTmp.setText(qbChatMessage.getBody());
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(TmpActivity.this, "chat server nije uspesan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            tmp++;
                        } else {
                            Toast.makeText(TmpActivity.this, "login nije uspesan", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //sendNotification();
                if(tmp == 0){
                    tmpViewModel.logInUser(u, "sifra123").observe(TmpActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isLoggedIn) {
                            if(isLoggedIn){
                                Toast.makeText(TmpActivity.this, "login uspesan", Toast.LENGTH_SHORT).show();
                                tmp++;
                            } else {
                                Toast.makeText(TmpActivity.this, "login nije uspesan", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (tmp == 1){
                    tmpViewModel.connectToChatServer(u).observe(TmpActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isConnected) {
                            if(isConnected){
                                Toast.makeText(TmpActivity.this, "chat server uspesan", Toast.LENGTH_SHORT).show();
                                tmp++;
                            } else {
                                Toast.makeText(TmpActivity.this, "chat server nije uspesan", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (tmp == 2){
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(128119046);
                    tmpViewModel.insertChat(ids, true).observe(TmpActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isCreated) {
                            if(isCreated != null){
                                Toast.makeText(TmpActivity.this, "chat uspesan", Toast.LENGTH_SHORT).show();
                                tmp++;
                            } else {
                                Toast.makeText(TmpActivity.this, "chat nije uspesan", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });*/
        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(etComment.getText().toString());
                chatMessage.setSaveToHistory(true);
                chatViewModel.sendMessage(chatMessage);*/
                if(QBSessionManager.getInstance().getSessionParameters() == null){
                    Log.i("checklogut", "logoutvaon");
                } else {
                    Log.i("checklogut", "nije logoutvaon");
                }
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.i("tokenic", s);
            }
        });

        //LOGIKA OKO NOTIFIKACIJA
        /*notificationList = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            if(i % 4 == 0)
                notificationList.add(new Notification(i, i, i, i, Constants.ACCEPTED));
            if(i % 4 == 1)
                notificationList.add(new Notification(i, i, i, i, Constants.DECLINED));
            if(i % 4 == 2)
                notificationList.add(new Notification(i, i, i, i, Constants.COMMENT));
            if(i % 4 == 3)
                notificationList.add(new Notification(i, i, i, i, Constants.RATING));
        }
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setHasFixedSize(true);
        rvlmNotifications = new LinearLayoutManager(this);
        rvaNotifications = new NotificationsAdapter(notificationList, new NotificationsAdapter.OnNotificationItemClickListener() {
            @Override
            public void onClick(int position) {
                Notification notification = rvaNotifications.getNotification(position);
                if(notification.getType().equals(Constants.RATING)){
                    //intent koji vodi do tvog profila
                } else {
                    //intent koji vodi do ad aktivitija
                }
            }
        });
        rvNotifications.setLayoutManager(rvlmNotifications);
        rvNotifications.setAdapter(rvaNotifications);

        srlNotifications = findViewById(R.id.srlNotifications);
        srlNotifications.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notificationList.add(0, new Notification(tmp, tmp, tmp, tmp, Constants.ACCEPTED));
                rvaNotifications.notifyDataSetChanged();
                srlNotifications.setRefreshing(false);
                tmp++;
            }
        });*/

    }
}
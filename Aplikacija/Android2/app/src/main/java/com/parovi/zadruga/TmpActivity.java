package com.parovi.zadruga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parovi.zadruga.adapters.NotificationsAdapter;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.TmpPost;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.nonEntityModels.CommentUser;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;
import com.parovi.zadruga.viewModels.LoginViewModel;
import com.parovi.zadruga.viewModels.TmpViewModel;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**/
public class TmpActivity extends AppCompatActivity {

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
    private RecyclerView rvComments;
    private RecyclerView.Adapter rvaComments;
    private RecyclerView.LayoutManager rvlmNComments;

    private SwipeRefreshLayout srlNotifications;
    private int tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        initQuickBlox();
        tmp = 2;
        tmpViewModel = new ViewModelProvider(this).get(TmpViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        rep = ZadrugaRepository.getInstance(getApplication());
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        FirebaseMessaging.getInstance().subscribeToTopic("nekaDrugaTopic");
        Button tmpBtn = (Button) findViewById(R.id.tmpBtn);
        Button tmpBtn2 = (Button) findViewById(R.id.tmpBtn2);
        Button btnPostComment =  (Button) findViewById(R.id.btnPostComment);
        EditText etComment = (EditText) findViewById(R.id.etComment);
        TextView tvTmp= (TextView)  findViewById(R.id.tvTmp);


        //LOGIKA OKO CHAT-A
        //128119046 user2 id

        User u = new User("user1");
        u.setQbId(128119044);
        tmp = 0;

        tmpViewModel.observePosts().observe(TmpActivity.this, new Observer<List<TmpPost>>() {
            @Override
            public void onChanged(List<TmpPost> tmpPosts) {
                if(tmpPosts != null)
                    Log.i("getAllPosts", "radiii");
            }
        });

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
        loginViewModel.logInQBUser(u, "sifra123");

        tmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tmpViewModel.getAllPosts();
            }
        });

        tmpBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatViewModel.connectToChatServer(u);
            }
        });

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
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(etComment.getText().toString());
                chatMessage.setSaveToHistory(true);
                chatViewModel.sendMessage(chatMessage);
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.i("tokenic", s);
            }
        });
        /*rep.getAllAds().observe(this, new Observer<List<Ad>>() {
            @Override
            public void onChanged(List<Ad> ads) {
                if(ads != null){
                    Log.i("sviOglasi", "promena");
                }
            }
        });*/

        //LOGIKA OKO NOTIFIKACIJA
        notificationList = new ArrayList<>();
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
        });

        //LOGIKA OKO KOMENTARA
        /*commentsList = new ArrayList<>();
        commentsList.add(new CommentUser("komentaric neki mnogo ludi", "@lazarm999"));
        commentsList.add(new CommentUser("komentaric neki mnogo ludi2", "@lazarm999"));
        commentsList.add(new CommentUser("komentaric neki mnogo lud3", "@lazarm999"));
        commentsList.add(new CommentUser("komentaric neki mnogo lud4", "@lazarm999"));

        rvComments = findViewById(R.id.rvComments);
        rvComments.setHasFixedSize(true);
        rvlmNComments = new LinearLayoutManager(this);
        rvaComments = new CommentsAdapter(commentsList);

        rvComments.setLayoutManager(rvlmNComments);
        rvComments.setAdapter(rvaComments);
        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = etComment.getText().toString();
                if(!commentText.equals("")){ //ovde treba idu podaci vezani za trenutno ulogovanog korisnika i trenutno prikazan oglas
                    //ovaj comment se upisuje u bazu
                    Comment newComm = new Comment(1,1, commentText, System.currentTimeMillis());
                    //ovaj comment se prikazuje korisniku
                    commentsList.add(0, new CommentUser(commentText, "lazarm999"));
                    rvaComments.notifyDataSetChanged();
                }
            }
        });*/
        /*tmpViewModel.getAds().observe(this, new Observer<List<Ad>>() {
            @Override
            public void onChanged(List<Ad> ads) {

            }
        });*/

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initQuickBlox(){
        QBSettings.getInstance().init(getApplicationContext(), Constants.APPLICATION_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
    }

    private void sendNotification() {
        rep.sendPushNotification(new PushNotification(new Notification(Constants.RATING), "/topics/svi")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("ispravno", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("firibaseZadruga", t.getMessage());
            }
        });
    }

    private void sendNotification2(){
        rep.sendPushNotification(new PushNotification(new Notification(Constants.COMMENT), "/topics/nekaDrugaTopic")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("ispravno", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("firibaseZadruga", t.getMessage());
            }
        });
    }

    private void saveLoggedUserId(){
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.LOGGED_USER_ID, "1");//TODO: ovde treba da stoji pravi userId, a ne jedinica
        editor.apply();
    }

    private void checkIfFcmTokenTagIsChanged(){
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String token = "";
        if((token = sp.getString(Constants.FCM_TOKEN_TAG, null)) != null){
            //TODO: Ovu funkciju treba da poziva neki viewModel
            User u = new User();
            u.setUserId(1);
            u.setFcmToken(token);
            rep.updateUserFcmToken(u).observe(this, new Observer<Integer>() { //TODO: ovde treba da stoji ulogovani user, a ne ovaj
                @Override
                public void onChanged(Integer rowsChanged) {
                    if(rowsChanged != -1){
                        Log.w("fcmTokenWarning", "Token nije promenjen");
                    }
                }
            });
            editor.putString(Constants.FCM_TOKEN_TAG, null);
        }
        editor.apply();
    }


}
package com.parovi.zadruga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parovi.zadruga.adapters.NotificationsAdapter;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.nonEntityModels.CommentUser;
import com.parovi.zadruga.repository.AdRepository;
import com.parovi.zadruga.repository.ChatRepository;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.NotificationRepository;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;
import com.parovi.zadruga.viewModels.LoginViewModel;
import com.parovi.zadruga.viewModels.TmpViewModel;

import java.util.ArrayList;
import java.util.Arrays;

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
    private LookUpRepository lookUpRep;
    private UserRepository userRep;
    private AdRepository adRep;
    private NotificationRepository notificationRep = new NotificationRepository();
    private ChatRepository chatRepository = new ChatRepository();

    private String topic = "svi";
    private ArrayList<Notification> notificationList;

    private RecyclerView rvNotifications;
    //cuva niz objekata koje zelis da prikazes i brine o tome da se samo pojedini cuvaju u memoriji a ne cela lista (samo oni koji su trenutno prikazani)
    private NotificationsAdapter rvaNotifications;
    private RecyclerView.LayoutManager rvlmNotifications;

    private ArrayList<CommentUser> commentsList;

    private SwipeRefreshLayout srlNotifications;
    private ActivityResultLauncher<String> getContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        tmpViewModel = new ViewModelProvider(this).get(TmpViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        lookUpRep = new LookUpRepository();
        userRep = new UserRepository();
        adRep = new AdRepository();
        /*FirebaseMessaging.getInstance().subscribeToTopic(topic);
        FirebaseMessaging.getInstance().subscribeToTopic("nekaDrugaTopic");*/
        Button tmpBtn1 = (Button) findViewById(R.id.tmpBtn1);
        Button tmpBtn2 = (Button) findViewById(R.id.tmpBtn2);
        Button tmpBtn3 = (Button) findViewById(R.id.tmpBtn3);
        Button tmpBtn4 = (Button) findViewById(R.id.tmpBtn4);
        Button tmpBtn5 = (Button) findViewById(R.id.tmpBtn5);
        Button btnPostComment =  (Button) findViewById(R.id.btnPostComment);
        EditText etComment = (EditText) findViewById(R.id.etComment);
        TextView tvTmp= (TextView)  findViewById(R.id.tvTmp);
        ImageView tmpImageView = (ImageView) findViewById(R.id.tmpImageView);
        //chatViewModel.adOnGlobalMessageReceived();
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

        lookUpRep.getAllBadges(Utility.getAccessToken(this), new MutableLiveData<>());
        lookUpRep.getAllLocations(Utility.getAccessToken(this), new MutableLiveData<>());
        lookUpRep.getAllTags(Utility.getAccessToken(this), new MutableLiveData<>());
        lookUpRep.getFacultiesAndUniversities(Utility.getAccessToken(this));

        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null)
                            userRep.postProfilePicture(new MutableLiveData<>(), uri);
                    }
                });

        MutableLiveData<CustomResponse<?>> chats = new MutableLiveData<>();
        chatViewModel.observeChats().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> response) {
                if(response != null){
                    if(response.getStatus() == CustomResponse.Status.OK){

                    } else if (response.getStatus() == CustomResponse.Status.BAD_REQUEST) {

                    }
                }
            }
        });

        chatViewModel.observeMessages().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){

                }
            }
        });

        chatViewModel.observeChats().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){

                }
            }
        });

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


        final int[] i = {0};
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }*/
        tmpBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//etComment.getText().toString())
                //TODO: za qbUser sifra mora bude duza od 8 karaktera
                //getContent.launch("image/*");
                /*if(Utility.getUserId(TmpActivity.this) == 1)
                    rep.loginUser(new MutableLiveData<>(), new User("vuk.bibic@gmail.com", "sifra123"));
                else
                    rep.loginUser(new MutableLiveData<>(), new User("tea@gmail.com", "sifra123"));*/
                //rep.getUserById(getAccessToken(), new MutableLiveData<>(), 1);
                //rep.populateData();
                //rep.getAd(Utility.getAccessToken(TmpActivity.this), new MutableLiveData<>(), 24);
                /*ArrayList<Integer> intList = new ArrayList<>();
                intList.add(1);
                intList.add(2);
                intList.add(3);
                rep.postAd(Utility.getAccessToken(TmpActivity.this), new MutableLiveData<>(), new PostAdRequest("opsan naslov", "valjda radi ovo",
                        50,
                        250, 12, 1, intList));*/
                /*ArrayList<Integer> intList = new ArrayList<>();
                intList.add(128304620);
                intList.add(128586493);
                List<User> us = new ArrayList<>();
                us.add(new User(19, 128304620));
                us.add(new User(3, 128586493));
                rep.chooseApplicants(new MutableLiveData<>(), 20, us);*/
                //rep.applyForAd(getAccessToken(), new MutableLiveData<>(), 20, 12);
                //rep.deleteAd(getAccessToken(), new MutableLiveData<>(), 29);
                //adRep.getComments(comments, 4);
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
               /* String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";
                rep.getComments(token, comments, 4);*/
                //rep.getProfilePicture(Utility.getAccessToken(TmpActivity.this), image, 1);
                //rep.loginUser(new MutableLiveData<>(), "tea@gmail.com", "sifra123");
                //chatViewModel.getChatMembers();
                //chatViewModel.getAd();
                //rep.getAdByChatId(new MutableLiveData<>(), "60c1e2cf094ee200482e09f2");
                //userRep.loginUser(new MutableLiveData<>(), "vuk.bibic@gmail.com", "novaaasifraaaa");
                //notificationRep.getNotifications(new MutableLiveData<>());
                //notificationRep.getNotifications(new MutableLiveData<>());
                //adRep.isApplied(new MutableLiveData<>(), 20);
                //userRep.banUser(new MutableLiveData<>(), new BanRequest("bas je bio los"), 1);
            }
        });
        tmpBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userRep.unBanUser(new MutableLiveData<>(), 1);
                //chatViewModel.getMessages();
                //userRep.loginUser(new MutableLiveData<>(), "vuk.bibic@gmail.com", "novaaasifraaaa");
                //userRep.loginUser(new MutableLiveData<>(), "tea@gmail.com", "sifra123");
                //chatViewModel.addOnGlobalMessageReceived();
//                userRep.registerUser(new MutableLiveData<>(), new User("stefan72", "Stefan", "Petrović", "stefan72@gmail.com",
//                                "sifra123", true),"sifra123");
                //rep.getUserById(Utility.getAccessToken(TmpActivity.this), new MutableLiveData<>(), 2);
                /*User u = new User();
                u.setBio("ovo je novi bio");
                u.setPhoneNumber("1223456799");
                u.setFkFacultyId(1);
                u.setUserId(3);
                rep.updateUser("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g",
                        new MutableLiveData<>(), u);*/
                //rep.banUser(getAccessToken(), new MutableLiveData<>(),  new Ban("jedan velikii baaaan"), 2);
                //rep.unBanUser(getAccessToken(), new MutableLiveData<>(), 3);//nt raterId, int rateeId, Rating rating
                //rep.editRating(getAccessToken(), new MutableLiveData<>(), new Rating(getUserId(), 3, 1, "jako slabo uradjeno"));
                //rep.getRatingByUserId(getAccessToken(), new MutableLiveData<>(), 11);
                //rep.postProfilePicture(getAccessToken(), new MutableLiveData<>(), 1);
                /*adRep.getAds(Utility.getAccessToken(TmpActivity.this), new MutableLiveData<>(), null, 4, 150,
                        Arrays.asList(1,3), true);*/
                //adRep.getAds(comments);
                //rep.unApplyForAd(getAccessToken(), new MutableLiveData<>(), 20, 28);
                //adRep.postComment(Utility.getAccessToken(TmpActivity.this), comments, 4,  "gaaaaaaaaaaaas");
                //rep.deleteComment(getAccessToken(), new MutableLiveData<>(), 5);
                //rep.changePassword(getAccessToken(), new MutableLiveData<>(), 	128330407, "sifra123", "novaaasifraaaa");
                //rep.deleteRating(getAccessToken(), new MutableLiveData<>(), 1, 3);
                //rep.getAd(getAccessToken(), new MutableLiveData<>(), 28);
                //rep.loginUser(new MutableLiveData<>(), new User("user124@gmail.com", "sifra123"));
               /* if(Utility.getUserId(TmpActivity.this) == 1)//, new User("vuk.bibic@gmail.com", "novaaasifraaaa", 128330407)
                    rep.connectToChatServer(new MutableLiveData<>());
                else//, new User("tea@gmail.com", "sifra123", 128586493)
                    rep.connectToChatServer(new MutableLiveData<>());*/
                //rep.connectToChatServer(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar", 128304620));
                /*List<Chat> tmpChats = (List<Chat>) chats.getValue().getBody();
                rep.sendMessage(new MutableLiveData<>(), tmpChats.get(0).getQbChat(), new User(), "porukaa novaaaaa");*/
                adRep.getAds(Utility.getAccessToken(TmpActivity.this), new MutableLiveData<>(), null, 4, 150,
                        Arrays.asList(1,3), true, false);
            }
        });
        tmpBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRep.loginUser(new MutableLiveData<>(), "admin@gmail.com", "sifra123");
                //chatViewModel.connectToChatServer();
                //userRep.logOutUser(new MutableLiveData<>());
                //chatViewModel.connectToChatServer();
                /*ArrayList<Integer> list = new ArrayList<>();
                list.add(128330407);
                rep.createChat(new MutableLiveData<>(), list, 28, 1);*/
                //rep.connectToChatServer(new MutableLiveData<>(), new User("markocar@gmail.com", "markocar", 128304620));
                //userRep.logOutUser(new MutableLiveData<>());
            }
        });
        tmpBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String teinToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g";
                rep.logOutUser(teinToken, new MutableLiveData<>(), 3);*/
                //rep.sendMessage(new MutableLiveData<>(), );
                chatViewModel.addOnGlobalMessageReceived();
                //adRep.getAds(new MutableLiveData<>());
                //chatViewModel.updateChat();
            }
        });

        /*userRep.loginUser(new MutableLiveData<>(), "vuk.bibic@gmail.com", "novaaasifraaaa");
        userRep.loginUser(new MutableLiveData<>(), "tea@gmail.com", "sifra123");
        chatViewModel.connectToChatServer();
        * chatViewModel.addOnGlobalMessageReceived();
        * chatViewModel.getAllChats();
        * chatViewModel.sendMessage(etComment.getText().toString());*/
        tmpBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rep.getFinishedJobsByUserId(new MutableLiveData<>());
                //rep.getPostedAdsByUserIdLocal(new MutableLiveData<>());
                //chatRepository.getChatById(new MutableLiveData<>(), "60c1e2cf094ee200482e09f2");
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.i("tokenic", s);
            }
        });

        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chatViewModel.sendMessage(etComment.getText().toString());
                chatViewModel.sendMessage("Hvala na saradnji!");
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
        //loginViewModel.logInQBUser(u, "sifra123");*/

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

    //TODO: ovo da se ubaci da ga vodi do settings za lokaciju
    public void showSettingsAlert(Context mContext){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
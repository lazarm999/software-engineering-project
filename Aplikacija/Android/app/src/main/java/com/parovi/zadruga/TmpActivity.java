package com.parovi.zadruga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parovi.zadruga.adapters.NotificationAdapter;
import com.parovi.zadruga.models.Notification;
import com.parovi.zadruga.models.TmpPost;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.TmpViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TmpActivity extends AppCompatActivity {

    private TmpViewModel tmpViewModel;
    private ZadrugaRepository rep;

    private String topic = "svi";

    private RecyclerView rvNotifications;
    //cuva niz objekata koje zelis da prikazes i brine o tome da se samo pojedini cuvaju u memoriji a ne cela lista (samo oni koji su trenutno prikazani)
    private RecyclerView.Adapter rvaNotifications;
    private RecyclerView.LayoutManager rvlmNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        tmpViewModel = new ViewModelProvider(this).get(TmpViewModel.class);
        rep = ZadrugaRepository.getInstance(getApplication());
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        FirebaseMessaging.getInstance().subscribeToTopic("nekaDrugaTopic");
        Button tmpBtn = (Button) findViewById(R.id.tmpBtn);
        Button tmpBtn2 = (Button) findViewById(R.id.tmpBtn2);
        tmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        tmpBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rep.getAllPosts().observe(TmpActivity.this, new Observer<List<TmpPost>>() {
                    @Override
                    public void onChanged(List<TmpPost> tmpPosts) {
                        if(true){

                        }
                    }
                });
                rep.insertUser(new User(7, "novi user"));
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
        ArrayList<Notification> list = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            if(i % 4 == 0)
                list.add(new Notification(i, i, i, i, Constants.ACCEPTED));
            if(i % 4 == 1)
                list.add(new Notification(i, i, i, i, Constants.DECLINED));
            if(i % 4 == 2)
                list.add(new Notification(i, i, i, i, Constants.COMMENT));
            if(i % 4 == 3)
                list.add(new Notification(i, i, i, i, Constants.RATING));
        }
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setHasFixedSize(true);
        rvlmNotifications = new LinearLayoutManager(this);
        rvaNotifications = new NotificationAdapter(list);

        rvNotifications.setLayoutManager(rvlmNotifications);
        rvNotifications.setAdapter(rvaNotifications);
    }

    @Override
    protected void onStart() {
        super.onStart();

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

package com.parovi.zadruga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.AdViewModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AdViewModel adViewModel;
    private ZadrugaRepository rep;

    private String topic = "svi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adViewModel = new ViewModelProvider(this).get(AdViewModel.class);
        rep = ZadrugaRepository.getInstance(getApplication());
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        FirebaseMessaging.getInstance().subscribeToTopic("nekaDrugaTopic");
        TextView textViewResult = (TextView) findViewById(R.id.tmpView);
        TextView textViewTmpPost = (TextView) findViewById(R.id.tmpPost);
        Button tmpBtn = (Button) findViewById(R.id.tmpBtn);
        tmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tokenic", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Log.w("tokenic", task.getResult());
                    }
                });
    }

    private void sendNotification() {
        RemoteMessage message = new RemoteMessage.Builder("ecySkhcyQDmy1OLNjOCRGj:APA91bGomdhZLmNkxJQBRFVkJge0igJX_F_R4bx9RF0w8NNdJ3gBgPp5Pp_N68N9jy5fw5XuZ3ZYVi0Z4BeaSlS8Uj7S57eM0CgBJUnrceyFCkyojkuFf-cdSszqaD5vu3ImLNINYr1k")
                .addData("body", "nekakav bodi")
                .addData("title", "nekakav title")
                .build();
        rep.sendPushNotification(new PushNotification(new Notification("title", "body"), "/topics/svi")).enqueue(new Callback<ResponseBody>() {
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
        rep.sendPushNotification(new PushNotification(new Notification("title", "body"), "/topics/nekaDrugaTopic")).enqueue(new Callback<ResponseBody>() {
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
}
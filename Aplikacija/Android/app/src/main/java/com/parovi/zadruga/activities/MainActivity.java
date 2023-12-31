package com.parovi.zadruga.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.User;

import java.util.Locale;

public class MainActivity<AlterDialog> extends AppCompatActivity {

    Animation topAnim, bottomAnim, scaleUp, scaleDown;      //variables for animation
    ImageView h1;
    Button logIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user;
        if((user = Utility.getLoggedInUser(this)) != null){
            Intent intent;
            if(user.isAdmin())
                intent = new Intent(MainActivity.this, AdminActivity.class);
            else if(user.isEmployer())
                intent = new Intent(MainActivity.this, MainEmployerActivity.class);
            else
                intent = new Intent(MainActivity.this, MainStudentActivity.class);
            startActivity(intent);
            finish();
        }
        loadLocale();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.btnLogIn);
        signUp = findViewById(R.id.btnSignUp);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, LogInActivity.class);
               startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        h1 = findViewById(R.id.txtAppName);
        h1.setAnimation(topAnim);

        ImageButton changeLang = findViewById(R.id.imgBtnChangeLanguage);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"SR", "EN"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle(R.string.changeLanguage);
        AlertDialog.Builder builder = mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                    {
                        setLocale("sr");
                        recreate();
                    }
                else if (which == 1)
                    {
                        setLocale("en");
                        recreate();
                    }

                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

}
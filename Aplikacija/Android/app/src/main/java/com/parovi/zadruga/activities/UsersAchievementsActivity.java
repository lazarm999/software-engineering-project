package com.parovi.zadruga.activities;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.AchievementAdapter;
import com.parovi.zadruga.items.AchievementItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class UsersAchievementsActivity extends AppCompatActivity {

    ArrayList<AchievementItem> achievements = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_achievements);

        achievements.add(new AchievementItem("Title 1", LocalDate.of(2021,6,17), 4, "Comment 1"));
        achievements.add(new AchievementItem("Title 2", LocalDate.of(2021,4,29), 1, "Comment 2"));
        achievements.add(new AchievementItem("Title 3", LocalDate.of(2021,6,13), 2, "Comment 3"));
        achievements.add(new AchievementItem("Title 4", LocalDate.of(2021,1,19), 5, "Comment 4"));
        achievements.add(new AchievementItem("Title 5", LocalDate.of(2021,2,5), 5, "Comment 5"));
        achievements.add(new AchievementItem("Title 6", LocalDate.of(2021,3,8), 3, "Comment 6"));


        RecyclerView recView = findViewById(R.id.recyclerViewAchievements);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AchievementAdapter adapter = new AchievementAdapter(achievements);
        recView.setAdapter(adapter);

        /*CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas (???)
            }
        });*/
    }
}
package com.parovi.zadruga.activities;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityUsersAchievementsBinding;
import com.parovi.zadruga.items.AchievementItem;
import com.parovi.zadruga.viewModels.AchievementViewModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class UsersAchievementsActivity extends AppCompatActivity {

    private AchievementViewModel model;
    private ActivityUsersAchievementsBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUsersAchievementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(AchievementViewModel.class);

        ArrayList<AchievementItem> achievements = new ArrayList<>();

        achievements.add(new AchievementItem("Title 1", LocalDate.of(2021,6,17), 4, "Comment 1"));
        achievements.add(new AchievementItem("Title 2", LocalDate.of(2021,4,29), 1, "Comment 2"));


        RecyclerView recView = findViewById(R.id.recyclerViewAchievements);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //AchievementAdapter adapter = new AchievementAdapter(achievements);
        //recView.setAdapter(adapter);

        /*CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas (???)
            }
        });*/
    }
}
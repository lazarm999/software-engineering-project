package com.parovi.zadruga.activities;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.AchievementAdapter;
import com.parovi.zadruga.databinding.ActivityUsersAchievementsBinding;
import com.parovi.zadruga.models.responseModels.RatingResponse;
import com.parovi.zadruga.viewModels.AchievementViewModel;

import java.util.ArrayList;

public class UsersAchievementsActivity extends AppCompatActivity implements AchievementAdapter.AchievementListener{

    private AchievementViewModel model;
    private ActivityUsersAchievementsBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUsersAchievementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(AchievementViewModel.class);

        ArrayList<RatingResponse> achievements = new ArrayList<>();

        RecyclerView recView = findViewById(R.id.recyclerViewAchievements);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        AchievementAdapter adapter = new AchievementAdapter(achievements);
        recView.setAdapter(adapter);

        model.getRatings().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAchievements((ArrayList<RatingResponse>) customResponse.getBody());
                }
            }
        });

        adapter.setAchievements(achievements);
    }

    @Override
    public void onAchievementSelected(RatingResponse achievement) {

    }
}
package com.parovi.zadruga.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.parovi.zadruga.R;
import com.parovi.zadruga.data.UserInfoResume;
import com.parovi.zadruga.data.UserRating;
import com.parovi.zadruga.databinding.ActivityRateUserBinding;

import java.util.Formatter;

public class RateUserActivity extends AppCompatActivity {
    private ActivityRateUserBinding binding;
    private UserRating rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rating = new UserRating(new UserInfoResume(1, "Vuk Bibic", "@vukbibic"), (short)0);

        String str1 = getString(R.string.rating_intro_format1, rating.getUser().getName()),
                str2 = getString(R.string.rating_intro_format2, "him");

        binding.tvIntro1.setText(str1);
        binding.tvIntro2.setText(str2);

        binding.btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating.setRating((short) binding.userRatingBar.getNumStars());
            }
        });
    }
}
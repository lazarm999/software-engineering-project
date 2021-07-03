package com.parovi.zadruga.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityGradeUserBinding;
import com.parovi.zadruga.fragments.StudentProfileFragment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.viewModels.AchievementViewModel;

public class GradeUserActivity extends AppCompatActivity {

    AchievementViewModel model;
    ActivityGradeUserBinding binding;

    Animation imageAnim, txtAnim;
    int rating = 0;
    Integer rateeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_user);

        rateeId = getIntent().getIntExtra(StudentProfileFragment.USER_ID, 0);

        binding = ActivityGradeUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(AchievementViewModel.class);

        imageAnim = AnimationUtils.loadAnimation(this, R.anim.image_grade);
        txtAnim = AnimationUtils.loadAnimation(this, R.anim.text_grade);

        model.hasRated(rateeId);

        binding.seekBarGrades.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0)
                {
                    binding.txtGrade.setText(R.string.gradeF);
                    binding.imgGrade.setImageResource(R.drawable.ic_f_grade);
                    binding.txtGrade.startAnimation(txtAnim);
                    binding.imgGrade.startAnimation(imageAnim);
                    rating = 1;
                }
                else if(progress == 1)
                {
                    binding.txtGrade.setText(R.string.gradeD);
                    binding.imgGrade.setImageResource(R.drawable.ic_d_grade);
                    binding.txtGrade.startAnimation(txtAnim);
                    binding.imgGrade.startAnimation(imageAnim);
                    rating = 2;
                }
                else if(progress == 2)
                {
                    binding.txtGrade.setText(R.string.gradeC);
                    binding.imgGrade.setImageResource(R.drawable.ic_c_grade);
                    binding.txtGrade.startAnimation(txtAnim);
                    binding.imgGrade.startAnimation(imageAnim);
                    rating = 3;
                }
                else if(progress == 3)
                {
                    binding.txtGrade.setText(R.string.gradeB);
                    binding.imgGrade.setImageResource(R.drawable.ic_b_grade);
                    binding.txtGrade.startAnimation(txtAnim);
                    binding.imgGrade.startAnimation(imageAnim);
                    rating = 4;
                }
                else if(progress == 4)
                {
                    binding.txtGrade.setText(R.string.gradeA);
                    binding.imgGrade.setImageResource(R.drawable.ic_a_grade);
                    binding.txtGrade.startAnimation(txtAnim);
                    binding.imgGrade.startAnimation(imageAnim);
                    rating = 5;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        model.getIsRated().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK)
                    disableBtnRating();
            }
        });

        binding.btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rating currRate = new Rating(rating, binding.txtComment.getText().toString());
                currRate.setFkRateeId(rateeId);
                model.rateUser(currRate);
                binding.btnRating.setVisibility(View.INVISIBLE);
            }
        });

        model.getHasRated().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    if((Boolean) customResponse.getBody())
                        disableBtnRating();
                }
            }
        });
    }

    private void disableBtnRating(){
        binding.btnRating.setClickable(false);
        binding.btnRating.setText(R.string.alreadyRated);
    }
}
package com.parovi.zadruga.activities;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parovi.zadruga.R;

public class GradeUserActivity extends AppCompatActivity {

    TextView gradeTxt;
    ImageView gradeImg;
    SeekBar range;
    Animation imageAnim, txtAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_user);

        gradeTxt = findViewById(R.id.txtGrade);
        gradeImg = findViewById(R.id.imgGrade);
        range = findViewById(R.id.seekBarGrades);
        imageAnim = AnimationUtils.loadAnimation(this, R.anim.image_grade);
        txtAnim = AnimationUtils.loadAnimation(this, R.anim.text_grade);

        range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 1)
                {
                    gradeTxt.setText(R.string.gradeF);
                    gradeImg.setImageResource(R.drawable.ic_f_grade);
                    gradeTxt.startAnimation(txtAnim);
                    gradeImg.startAnimation(imageAnim);
                }
                else if(progress == 2)
                {
                    gradeTxt.setText(R.string.gradeD);
                    gradeImg.setImageResource(R.drawable.ic_d_grade);
                    gradeTxt.startAnimation(txtAnim);
                    gradeImg.startAnimation(imageAnim);
                }
                else if(progress == 3)
                {
                    gradeTxt.setText(R.string.gradeC);
                    gradeImg.setImageResource(R.drawable.ic_c_grade);
                    gradeTxt.startAnimation(txtAnim);
                    gradeImg.startAnimation(imageAnim);
                }
                else if(progress == 4)
                {
                    gradeTxt.setText(R.string.gradeB);
                    gradeImg.setImageResource(R.drawable.ic_b_grade);
                    gradeTxt.startAnimation(txtAnim);
                    gradeImg.startAnimation(imageAnim);
                }
                else if(progress == 5)
                {
                    gradeTxt.setText(R.string.gradeA);
                    gradeImg.setImageResource(R.drawable.ic_a_grade);
                    gradeTxt.startAnimation(txtAnim);
                    gradeImg.startAnimation(imageAnim);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
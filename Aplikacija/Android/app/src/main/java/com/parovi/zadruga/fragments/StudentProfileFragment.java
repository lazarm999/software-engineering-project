package com.parovi.zadruga.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parovi.zadruga.R;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.UsersAchievementsActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentProfileFragment extends Fragment {


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static ImageView profilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_student_profile_fragment, container, false);

        Button achievements = (Button) layout.findViewById(R.id.btnAchievements);
        Button edit = (Button) layout.findViewById(R.id.btnEdit);
        ImageButton back = (ImageButton) layout.findViewById(R.id.imgBtnArrowBack);
        Button rate = (Button) layout.findViewById(R.id.btnRating);
        TextView fac = (TextView) layout.findViewById(R.id.txtUniversity);
        TextView bio = (TextView) layout.findViewById(R.id.txtMultilineEditBio);
        TextView phone = (TextView) layout.findViewById(R.id.txtNumber);
        TextView name = (TextView) layout.findViewById(R.id.editTextUser);
        TextView username = (TextView) layout.findViewById(R.id.editTextUsername);
        profilePicture = (ImageView) layout.findViewById(R.id.imgUser);


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else
                        pickImageFromGallery();
                }
                else
                    pickImageFromGallery();
            }

        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GradeUserActivity.class);
                startActivity(intent);
            }
        });

        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersAchievementsActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }

    private void pickImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE)
            if(data!=null){
                Uri selectedImage = data.getData();
                if(selectedImage!=null)
                {
                    try{
                        InputStream input = getActivity().getContentResolver().openInputStream(selectedImage);
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        profilePicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == IMAGE_PICK_CODE && grantResults.length > 0)
        {
            if(grantResults[0] == getActivity().getPackageManager().PERMISSION_GRANTED)
                pickImageFromGallery();
                else {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT);
            }
        }
    }
}
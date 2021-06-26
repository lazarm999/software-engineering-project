package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.UsersAchievementsActivity;
import com.parovi.zadruga.databinding.FragmentStudentProfileFragmentBinding;
import com.parovi.zadruga.viewModels.StudentProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileFragment extends Fragment {

    private StudentProfileViewModel model;
    private FragmentStudentProfileFragmentBinding binding;
    private List<String> descriptions = new ArrayList<>();

    public StudentProfileFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentProfileFragmentBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(StudentProfileViewModel.class);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });

        binding.btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GradeUserActivity.class);
                startActivity(intent);
            }
        });

        binding.btnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersAchievementsActivity.class);
                startActivity(intent);
            }
        });

        model.getThisUser().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    Toast.makeText(requireContext(), "You got the user", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), "Error occurred while getting this user.", Toast.LENGTH_SHORT).show();
            }
        });

        model.getProfilePicture().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    Toast.makeText(requireContext(), "Profile picture success!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), "Error occurred while getting profile picture.", Toast.LENGTH_SHORT).show();
            }
        });

        model.getBadges().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    Toast.makeText(requireContext() , "Error", Toast.LENGTH_SHORT).show();
                }
                descriptions.addAll(model.getBadgeNames());
            }
        });

        return binding.getRoot();
    }

}
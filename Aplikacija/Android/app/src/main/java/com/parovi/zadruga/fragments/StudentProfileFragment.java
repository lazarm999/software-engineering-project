package com.parovi.zadruga.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.MainActivity;
import com.parovi.zadruga.databinding.FragmentStudentProfileFragmentBinding;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.StudentProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileFragment extends Fragment {

    private StudentProfileViewModel model;
    private FragmentStudentProfileFragmentBinding binding;
    private List<String> descriptions = new ArrayList<>();
    private UserRepository userRepository = new UserRepository();
    private User u;

    public StudentProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentProfileFragmentBinding.inflate(inflater, container, false);
        //userRepository.loginUser(new MutableLiveData<>(), "tea@gmail.com", "sifra123");
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
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.profileFrame, new RatingFragment());
                fr.commit();
            }
        });

        model.getUserInfo().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    populateViews((User)customResponse.getBody());
                    u = ((User) customResponse.getBody());
                    if(u.getBadges() != null){
                        List<Badge> badges = u.getBadges();
                        for (int i = 0; i < badges.size(); i++) {
                            ((ImageView)binding.linearBagdges.getChildAt(0)).setImageResource(R.drawable.badge1);
                        }
                    }
                }
            }
        });

        model.getProfilePicture().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    loadProfilePicture((Bitmap)customResponse.getBody());
                }
            }
        });

        model.getIsLogedOut().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.logOut();
            }
        });

        return binding.getRoot();
    }

    private void loadProfilePicture(Bitmap pic) {
        binding.imgUser.setImageBitmap(pic);
    }

    private void populateViews(User user) {
        binding.txtFirstName.setText(user.getFirstName());
        binding.txtLastName.setText(user.getLastName());
        binding.txtNumber.setText(user.getPhoneNumber());
        binding.txtUniversity.setText(user.getFaculty() == null ? "" : user.getFaculty().toString());
        binding.editTextUsername.setText(user.getUsername());
        binding.txtMultilineEditBio.setText(user.getBio());
    }
}
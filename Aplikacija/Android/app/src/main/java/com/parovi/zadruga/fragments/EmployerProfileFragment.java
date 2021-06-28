package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.UsersAchievementsActivity;
import com.parovi.zadruga.databinding.FragmentEmployerProfileBinding;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.EmployerProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class EmployerProfileFragment extends Fragment {
    private EmployerProfileViewModel model;
    private FragmentEmployerProfileBinding binding;
    private List<String> descriptions = new ArrayList<>();
    private UserRepository userRepository = new UserRepository();
    private User u;

    public EmployerProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployerProfileBinding.inflate(inflater, container, false);
        userRepository.loginUser(new MutableLiveData<>(), "vuk@gmail.com", "novasifra");
        model = new ViewModelProvider(requireActivity()).get(EmployerProfileViewModel.class);

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

        model.getUserInfo().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    populateViews((User)customResponse.getBody());
                    u = ((User) customResponse.getBody());
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


        model.getBadges().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                } else{
                    List<Badge> badges = u.getBadges();
                    List<Integer> badgesInt = model.getBadgeIds(badges);

                    if(badges.size() == 0)
                    {
                        return;
                    }
                    else {
                        if (model.gotTheBadge(model.getBadgeIds(badges).get(1))) {
                            binding.imgBadge1.setImageResource(R.drawable.badge1);
                        } else {
                            binding.imgBadge1.setImageResource(R.drawable.locker);
                        }
                        if (model.gotTheBadge(model.getBadgeIds(badges).get(2))) {
                            binding.imgBadge2.setImageResource(R.drawable.badge1);
                        }
                        if (model.gotTheBadge(model.getBadgeIds(badges).get(3))) {
                            binding.imgBadge3.setImageResource(R.drawable.badge1);
                        }
                        if (model.gotTheBadge(model.getBadgeIds(badges).get(4))) {
                            binding.imgBadge4.setImageResource(R.drawable.badge1);
                        }
                        if (model.gotTheBadge(model.getBadgeIds(badges).get(5))) {
                            binding.imgBadge5.setImageResource(R.drawable.badge1);
                        }
                    }
                }
                descriptions.addAll(model.getBadgeNames());
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
        binding.txtCompany.setText(user.getCompanyName());
        binding.txtMultilineEditBio.setText(user.getBio());
    }
}
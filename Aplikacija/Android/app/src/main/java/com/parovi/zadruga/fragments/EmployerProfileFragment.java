package com.parovi.zadruga.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.MainActivity;
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
        //userRepository.loginUser(new MutableLiveData<>(), "vuk@gmail.com", "novasifra");
        model = new ViewModelProvider(requireActivity()).get(EmployerProfileViewModel.class);

        binding.btnRating.setVisibility(View.GONE);
        binding.btnBanUser.setVisibility(View.GONE);

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

        binding.btnAchiev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersAchievementsActivity.class);
                startActivity(intent);
            }
        });

        model.getUser().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    populateViews((User)customResponse.getBody());
                    u = ((User) customResponse.getBody());
                    int[] arrayLock = new int[]{R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b6, R.drawable.b7};

                    if(u.getBadges() != null){
                        List<Badge> badges = u.getBadges();
                        for (int i = 0; i < badges.size(); i++) {
                            ((ImageView)binding.linearBagdges.getChildAt((badges.get(i).getBadgeId()-1)%5)).setImageResource(arrayLock[(badges.get(i).getBadgeId()-1)%5]);
                        }
                    }
                }
            }
        });

        model.getIsBanned().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() != CustomResponse.Status.OK){
                    return;
                }
            }
        });

        binding.btnBanUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.banUser();
            }
        });

        binding.imgBadge1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                final TextView etText = new TextView(requireActivity());
                etText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etText.setTextSize(16);
                etText.setText(model.getBadgeDesc(5));
                dialog.setTitle(etText.getText().toString());

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        binding.imgBadge2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                final TextView etText = new TextView(requireActivity());
                etText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etText.setTextSize(16);
                etText.setText(model.getBadgeDesc(6));
                dialog.setTitle(etText.getText().toString());

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        binding.imgBadge3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                final TextView etText = new TextView(requireActivity());
                etText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etText.setTextSize(16);
                etText.setText(model.getBadgeDesc(7));
                dialog.setTitle(etText.getText().toString());

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        binding.imgBadge4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                dialog.setTitle(model.getBadgeDesc(8));

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        binding.imgBadge5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                dialog.setTitle(model.getBadgeDesc(9));

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
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

        int userId = -1;
        if (getArguments() != null) {
            userId = StudentProfileFragmentArgs.fromBundle(getArguments()).getUserId();
            if (userId != Utility.getLoggedInUserId(requireContext())) {
                binding.btnEdit.setVisibility(View.GONE);
                binding.btnLogOut.setVisibility(View.GONE);
            }
        }
        model.loadUser(userId);
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

        if(user.isAdmin())
            updateViewAdmin();

        if (!user.isEmployer())
            updateViewStudent();

    }

    private void updateViewStudent()
    {
        binding.btnRating.setVisibility(View.VISIBLE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setVisibility(View.GONE);
        binding.btnBanUser.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceAsColor")
    private void updateViewAdmin()
    {
        binding.btnRating.setVisibility(View.GONE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setVisibility(View.GONE);
        binding.btnBanUser.setVisibility(View.VISIBLE);
    }
}
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
import androidx.navigation.Navigation;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.MainActivity;
import com.parovi.zadruga.databinding.FragmentEmployerProfileBinding;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.ui.EditProfileActivity;
import com.parovi.zadruga.viewModels.EmployerProfileViewModel;

import java.util.List;

public class EmployerProfileFragment extends Fragment {
    private EmployerProfileViewModel model;
    private FragmentEmployerProfileBinding binding;
    private User u;

    public EmployerProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployerProfileBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(EmployerProfileViewModel.class);

        binding.btnRate.setVisibility(View.INVISIBLE);
        binding.btnBanUser.setVisibility(View.INVISIBLE);
        binding.btnLogOut.setVisibility(View.VISIBLE);
        binding.btnEdit.setVisibility(View.VISIBLE);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.btnAchievements.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                EmployerProfileFragmentDirections.ActionStudentProfileFragment2ToRatingFragment3 action = EmployerProfileFragmentDirections.actionStudentProfileFragment2ToRatingFragment3();
                action.setUserId(model.getId());
                Navigation.findNavController(binding.getRoot()).navigate(action);
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
                    binding.btnRate.setOnClickListener(rateUserListener);
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

        binding.btnLogOut.setOnClickListener(v -> {
            model.logOut();
            requireActivity().finish();
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

        if(user.getUserId() != Utility.getLoggedInUserId(App.getAppContext())) {
            if (Utility.getLoggedInUser(App.getAppContext()).isAdmin())
                updateViewAdmin(user.getBanAdmin() != null);
            else if (!Utility.getLoggedInUser(App.getAppContext()).isEmployer())
                updateViewStudent();
            else if (!Utility.getLoggedInUser(App.getAppContext()).isEmployer())
                updateViewEmployer();
        }
    }

    private void updateViewEmployer() {
        binding.btnRate.setVisibility(View.INVISIBLE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setVisibility(View.GONE);
        binding.btnBanUser.setVisibility(View.INVISIBLE);
    }

    private void updateViewStudent()
    {
        binding.btnRate.setVisibility(View.VISIBLE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setVisibility(View.GONE);
        binding.btnBanUser.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("ResourceAsColor")
    private void updateViewAdmin(boolean isUserBanned)
    {
        binding.btnRate.setVisibility(View.GONE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setVisibility(View.GONE);
        binding.btnBanUser.setVisibility(View.VISIBLE);
        if(isUserBanned) {
            binding.btnBanUser.setClickable(false);
            binding.btnBanUser.setBackgroundColor(R.color.grey);
            binding.btnBanUser.setText(R.string.alreadyBanned);
        }
    }

    View.OnClickListener rateUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), GradeUserActivity.class);
            intent.putExtra(GradeUserActivity.USER_ID, model.getId());
            startActivity(intent);
        }
    };
}
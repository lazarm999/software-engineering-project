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
import com.parovi.zadruga.ui.EditBasicProfileInfoFragment;
import com.parovi.zadruga.viewModels.StudentProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileFragment extends Fragment {

    public static final String USER_ID = "UserId";

    private StudentProfileViewModel model;
    private FragmentStudentProfileFragmentBinding binding;
    private List<String> descriptions = new ArrayList<>();
    private UserRepository userRepository = new UserRepository();
    private User u;
    private int id;

    public StudentProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentProfileFragmentBinding.inflate(inflater, container, false);

        binding.btnRating.setVisibility(View.INVISIBLE);

        //userRepository.loginUser(new MutableLiveData<>(), "tea@gmail.com", "sifra123");
        model = new ViewModelProvider(requireActivity()).get(StudentProfileViewModel.class);
        binding.btnRating.setEnabled(false);

        model.getIsBanned().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() != CustomResponse.Status.OK){
                    return;
                }
            }
        });

        if(model.isProfileAdmin())
        {
            binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.banUser();
                }
            });
        }




        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), EditBasicProfileInfoFragment.class);
                intent.putExtra(StudentProfileFragment.USER_ID, u.getUserId());
                startActivity(intent);
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
                    id = u.getUserId();
                    int[] arrayLock = new int[]{R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5};

                    if(u.getBadges() != null){
                        List<Badge> badges = u.getBadges();
                        for (int i = 0; i < badges.size(); i++) {
                            ((ImageView)binding.linearBagdges.getChildAt((badges.get(i).getBadgeId()-1)%5)).setImageResource(arrayLock[(badges.get(i).getBadgeId()-1)%5]);
                        }
                    }
                }
            }
        });

        binding.imgBadge1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                final TextView etText = new TextView(requireActivity());
                etText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etText.setTextSize(16);
                etText.setText(model.getBadgeDesc(1));
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
                etText.setText(model.getBadgeDesc(1));
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
                etText.setText(model.getBadgeDesc(2));
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
                final TextView etText = new TextView(requireActivity());
                etText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etText.setTextSize(16);
                etText.setText(model.getBadgeDesc(3));
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
        binding.imgBadge5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                final TextView etText = new TextView(requireActivity());
                etText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etText.setTextSize(16);
                etText.setText(model.getBadgeDesc(4));
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

        model.getProfilePicture().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK){
                    loadProfilePicture((Bitmap)customResponse.getBody());
                }
            }
        });

        model.getIsLoggedOut().observe(requireActivity(), new Observer<CustomResponse<?>>() {
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

        if (model.isProfileEmployer())
            updateViewEmployer();

        if(model.isProfileAdmin())
            updateViewAdmin();
    }

    private void updateViewEmployer()
    {
        binding.btnRating.setVisibility(View.VISIBLE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceAsColor")
    private void updateViewAdmin()
    {
        binding.btnRating.setVisibility(View.VISIBLE);
        binding.btnEdit.setVisibility(View.GONE);
        binding.btnLogOut.setText(R.string.ban);
        binding.btnLogOut.setBackgroundColor(R.color.red);
    }
}
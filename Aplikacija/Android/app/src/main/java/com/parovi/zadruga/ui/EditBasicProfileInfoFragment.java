package com.parovi.zadruga.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.databinding.FragmentEditBasicProfileInfoBinding;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.viewModels.UserProfileViewModel;

import java.io.FileDescriptor;

public class EditBasicProfileInfoFragment extends Fragment {
    private UserProfileViewModel model;
    private FragmentEditBasicProfileInfoBinding binding;
    private ActivityResultLauncher<String> getContent;

    public EditBasicProfileInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditBasicProfileInfoBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null)
                            model.updateUserProfilePhoto(getResources(), uri, binding.ivProfilePhoto.getWidth(), binding.ivProfilePhoto.getHeight());
                    }
                });

        model.getUserInfo().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                populateViews((User)customResponse.getBody());
            }
        });

        model.getProfileImage().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                loadProfilePicture((Bitmap)customResponse.getBody());
            }
        });

        model.getIsProfileImageUpdated().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    ;
                }
            }
        });

        model.getIsUserUpdated().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    Toast.makeText(requireContext(), "Changes commited!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readViews((User)model.getUserInfo().getValue().getBody());
                model.updateUser();
                ((BitmapDrawable)binding.ivProfilePhoto.getDrawable()).getBitmap();
            }
        });

        binding.tvEditPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(EditBasicProfileInfoFragmentDirections.actionEditBasicProfileInfoFragmentToSelectPreferencesFragment());
            }
        });

        binding.ivPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContent.launch("image/*");
            }
        });
        return binding.getRoot();
    }

    private void populateViews(User user) {
        binding.etFirstName.setText(user.getFirstName());
        binding.etLastName.setText(user.getLastName());
        binding.etPhoneNo.setText(user.getPhoneNumber());
        binding.etBio.setText(user.getBio());
        binding.etEmail.setText(user.getEmail());
    }

    private void readViews(User user) {
        user.setFirstName(binding.etFirstName.getText().toString().trim());
        user.setLastName(binding.etLastName.getText().toString().trim());
        user.setBio(binding.etBio.getText().toString().trim());
        user.setEmail(binding.etEmail.getText().toString().trim());
        user.setPhoneNumber(binding.etPhoneNo.getText().toString().trim());
    }
    private void loadProfilePicture(Bitmap pic) {
        binding.ivProfilePhoto.setImageBitmap(pic);
    }
}
package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parovi.zadruga.data.UserInfo;
import com.parovi.zadruga.databinding.FragmentEditBasicProfileInfoBinding;
import com.parovi.zadruga.viewModels.UserProfileViewModel;

public class EditBasicProfileInfoFragment extends Fragment {
    private UserProfileViewModel model;
    private FragmentEditBasicProfileInfoBinding binding;

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

        populateViews();

        binding.btnSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readViews();
                Toast.makeText(getContext(), "Changes submitted", Toast.LENGTH_LONG).show();
            }
        });

        binding.tvEditPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(EditBasicProfileInfoFragmentDirections.actionEditBasicProfileInfoFragmentToSelectPreferencesFragment());
            }
        });
        return binding.getRoot();
    }

    private void populateViews() {
        UserInfo userInfo = model.getUserInfo().getValue();
        binding.etFirstName.setText(userInfo.getFirstName());
        binding.etLastName.setText(userInfo.getLastName());
        binding.etPhoneNo.setText(userInfo.getPhoneNo());
    }

    private void readViews() {
        UserInfo userInfo = model.getUserInfo().getValue();
        userInfo.setFirstName(binding.etFirstName.getText().toString().trim());
        userInfo.setLastName(binding.etLastName.getText().toString().trim());
        userInfo.setPhoneNo(binding.etPhoneNo.getText().toString().trim());
    }
}
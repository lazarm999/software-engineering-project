package com.parovi.zadruga.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.ApplicantsAdapter;
import com.parovi.zadruga.databinding.FragmentSelectWorkersBinding;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.viewModels.AdViewModel;

import java.util.List;


public class SelectWorkersFragment extends Fragment implements ApplicantsAdapter.ApplicantListListener {
    private AdViewModel model;
    private FragmentSelectWorkersBinding binding;
    public SelectWorkersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectWorkersBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(AdViewModel.class);
        //ApplicantResumeAdapter adapter = new ApplicantResumeAdapter();
        ApplicantsAdapter adapter = new ApplicantsAdapter(this);

        model.getApplicants().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.submitList((List<User>)customResponse.getBody());
                }
            }
        });

        binding.rvWorkers.setAdapter(adapter);
        binding.rvWorkers.setLayoutManager(new LinearLayoutManager(container.getContext()));

        binding.btnConfirmSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.selectWorkers();
                Navigation.findNavController(binding.getRoot()).navigate(SelectWorkersFragmentDirections.actionSelectWorkersFragmentToJobAdFragment());
            }
        });
        model.loadApplicants();
        return binding.getRoot();
    }

    @Override
    public void onApplicantSelected(User applicant) {
        Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
        intent.putExtra("userId", applicant.getUserId());
        startActivity(intent);
    }

    @Override
    public void onApplicantToggled(User applicant, boolean isChecked) {
        if (isChecked) {
            model.addUserToSelected(applicant);
        }
        else
            model.removeUserFromSelected(applicant);
    }
}
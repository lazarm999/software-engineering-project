package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.ApplicantResumeAdapter;
import com.parovi.zadruga.data.JobAd;
import com.parovi.zadruga.databinding.FragmentSelectWorkersBinding;
import com.parovi.zadruga.viewModels.JobAdViewModel;


public class SelectWorkersFragment extends Fragment {
    private JobAdViewModel model;
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
        model = new ViewModelProvider(requireActivity()).get(JobAdViewModel.class);

        ApplicantResumeAdapter adapter = new ApplicantResumeAdapter();
        model.getJobAd().observe(requireActivity(), new Observer<JobAd>() {
            @Override
            public void onChanged(JobAd jobAd) {
                adapter.setUsers(model.getJobAd().getValue().getApplicants());
            }
        });
        binding.rvWorkers.setAdapter(adapter);
        binding.rvWorkers.setLayoutManager(new LinearLayoutManager(container.getContext()));

        return binding.getRoot();
    }
}
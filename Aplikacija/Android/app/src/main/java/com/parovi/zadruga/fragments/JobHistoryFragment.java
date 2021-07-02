package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.JobHistoryAdapter;
import com.parovi.zadruga.databinding.FragmentJobHistoryBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.JobHistoryViewModel;

import java.util.List;

public class JobHistoryFragment extends Fragment implements JobHistoryAdapter.JobHistoryListListener {
    private FragmentJobHistoryBinding binding;
    private JobHistoryViewModel model;

    public JobHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobHistoryBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(JobHistoryViewModel.class);
        JobHistoryAdapter adapter = new JobHistoryAdapter(this);
        binding.recyclerViewJobHistory.setAdapter(adapter);
        binding.recyclerViewJobHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        model.getJobList().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    adapter.setJobList((List<Ad>)customResponse.getBody());
            }
        });

        model.loadJobs();
        return binding.getRoot();
    }

    @Override
    public void onJobSelected(Ad ad) {
        Intent intent = new Intent(requireContext(), JobAdActivity.class);
        intent.putExtra(JobAdActivity.AD_ID, ad.getAdId());
        startActivity(intent);
    }
}
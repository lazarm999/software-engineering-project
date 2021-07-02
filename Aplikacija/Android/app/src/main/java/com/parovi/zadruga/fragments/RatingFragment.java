package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.AchievementAdapter;
import com.parovi.zadruga.databinding.FragmentRatingBinding;
import com.parovi.zadruga.models.responseModels.RatingResponse;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.AchievementViewModel;

import java.util.ArrayList;
import java.util.List;

public class RatingFragment extends Fragment implements AchievementAdapter.AchievementListener{
    private AchievementViewModel model;
    private FragmentRatingBinding binding;

    public RatingFragment() {
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
        binding = FragmentRatingBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);

        ArrayList<RatingResponse> ach = new ArrayList<>();

        RecyclerView recView = binding.recyclerViewAchievements;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        AchievementAdapter adapter = new AchievementAdapter(this);
        recView.setAdapter(adapter);

        model.getRatings().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAchievements((ArrayList<RatingResponse>) customResponse.getBody());
                }
            }
        });
        if (getArguments() != null) {
            model.loadUser(RatingFragmentArgs.fromBundle(getArguments()).getUserId());
        }
        else
            model.loadUser(-1);

        model.loadRatings();
        return binding.getRoot();
    }

    @Override
    public void onAchievementSelected(RatingResponse achievement) {
        ;
    }
}
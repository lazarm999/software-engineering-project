package com.parovi.zadruga.fragments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.AchievementAdapter;
import com.parovi.zadruga.databinding.FragmentAchievementFragmentBinding;
import com.parovi.zadruga.models.responseModels.RatingResponse;
import com.parovi.zadruga.viewModels.AchievementViewModel;

import java.util.ArrayList;

public class AchievementFragment extends Fragment implements AchievementAdapter.AchievementListener {

    private AchievementViewModel model;
    private FragmentAchievementFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAchievementFragmentBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(this).get(AchievementViewModel.class);

        ArrayList<RatingResponse> achievements = new ArrayList<>();


        RecyclerView recView = binding.recyclerViewAchievements;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        AchievementAdapter adapter = new AchievementAdapter(achievements);
        recView.setAdapter(adapter);

        model.getRatings().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAchievements((ArrayList<RatingResponse>) customResponse.getBody());
                }
            }
        });

        adapter.setAchievements(achievements);

        return binding.getRoot();
    }
    @Override
    public void onAchievementSelected(RatingResponse achievement) {

    }
}

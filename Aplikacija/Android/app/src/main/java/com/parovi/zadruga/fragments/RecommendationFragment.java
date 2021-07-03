package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.AdAdapter;
import com.parovi.zadruga.databinding.FragmentRecommendationFragmentBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.RecommendViewModel;

import java.util.ArrayList;

public class RecommendationFragment extends Fragment implements AdAdapter.AdListListener {
    public RecommendationFragment() {
        // Required empty public constructor
    }

    RecommendViewModel model;
    FragmentRecommendationFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecommendationFragmentBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(RecommendViewModel.class);


        ArrayList<Ad> ads = new ArrayList<>();

        RecyclerView recView = binding.recViewRecommends;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AdAdapter adapter = new AdAdapter(this);
        recView.setAdapter(adapter);

        adapter.setAds(ads);

        model.getAds().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.TAGS_NOT_CHOSEN) {
                    binding.progressBarRecommend.setVisibility(View.GONE);
                }
                if (customResponse.getStatus() == CustomResponse.Status.NO_MORE_DATA) {
                    binding.progressBarRecommend.setVisibility(View.GONE);
                }
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAds((ArrayList<Ad>) customResponse.getBody());
                }
            }
        });

        binding.refreshRecommend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.getRecommendRefresh();
                binding.refreshRecommend.setRefreshing(false);
            }
        });


        model.loadRecommended();

        binding.nestedRecommend.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    model.loadRecommended();
                    binding.progressBarRecommend.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAdSelected(Ad ad) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra(JobAdActivity.AD_ID, ad.getAdId());
        startActivity(intent);
    }
}
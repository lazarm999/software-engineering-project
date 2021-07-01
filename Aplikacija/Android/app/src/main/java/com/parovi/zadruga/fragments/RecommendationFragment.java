package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    int page = 0;
    int limit = 5;

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
                    Toast.makeText(requireActivity(), "That's all the data..", Toast.LENGTH_SHORT).show();
                }
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAds((ArrayList<Ad>) customResponse.getBody());
                }
            }
        });

        getData(page, limit);

        binding.nestedRecommend.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    binding.progressBarRecommend.setVisibility(View.VISIBLE);
                    getData(page, limit);
                }
            }
        });

        return binding.getRoot();
    }

    private void getData(int page, int limit)
    {
        if (page < limit) { binding.progressBarRecommend.setVisibility(View.GONE);
            return;
        }
        model.getAds();
    }

    private void stopPaging()
    {
        binding.progressBarRecommend.setVisibility(View.GONE);
        binding.nestedRecommend.setNestedScrollingEnabled(false);
        binding.recViewRecommends.setVisibility(View.GONE);
    }

    @Override
    public void onAdSelected(Ad ad) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra("AdID", ad.getAdId());
        startActivity(intent);
    }
}
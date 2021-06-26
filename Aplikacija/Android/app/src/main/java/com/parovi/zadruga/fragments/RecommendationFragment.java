package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.adapters.AdAdapter;
import com.parovi.zadruga.databinding.FragmentRecommendationFragmentBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.ui.JobAdActivity;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecommendationFragment extends Fragment implements AdAdapter.AdListListener {
    public RecommendationFragment() {
        // Required empty public constructor
    }

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

        ArrayList<Ad> ads = new ArrayList<>();

        ads.add(new Ad("Delivery worker", "Looking for students up to 26 years of age for food delivery services.", LocalDate.of(2021, 6, 10)));

        RecyclerView recView = binding.recViewRecommends;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AdAdapter adapter = new AdAdapter(this);
        recView.setAdapter(adapter);

        adapter.setAds(ads);


        /*CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas
            }
        });*/

        return binding.getRoot();
    }

    @Override
    public void onAdSelected(Ad ad) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra("AdID", ad.getAdId());
        startActivity(intent);
    }
}
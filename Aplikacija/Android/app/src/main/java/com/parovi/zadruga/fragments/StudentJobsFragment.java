package com.parovi.zadruga.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.JobHistoryAdapter;
import com.parovi.zadruga.databinding.FragmentStudentJobsFragmentBinding;
import com.parovi.zadruga.items.AdWithStudentRatingItem;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

public class StudentJobsFragment extends Fragment {
    private FragmentStudentJobsFragmentBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentJobsFragmentBinding.inflate(inflater, container, false);

        ArrayList<AdWithStudentRatingItem> jobs = getJobList();

        binding.recyclerViewJobHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        JobHistoryAdapter adapter = new JobHistoryAdapter(jobs);
        binding.recyclerViewJobHistory.setAdapter(adapter);

        /*CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas
            }
        });*/

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NotNull
    private ArrayList<AdWithStudentRatingItem> getJobList() {
        ArrayList<AdWithStudentRatingItem> jobs = new ArrayList<>();

        jobs.add(new AdWithStudentRatingItem("Title 1", LocalDate.of(2021, 5, 29), 4));
        jobs.add(new AdWithStudentRatingItem("Title 2", LocalDate.of(2020, 8, 22), 5));
        jobs.add(new AdWithStudentRatingItem("Title 3", LocalDate.of(2021, 10, 30), 4));
        jobs.add(new AdWithStudentRatingItem("Title 4", LocalDate.of(2021, 12, 4), 1));
        jobs.add(new AdWithStudentRatingItem("Title 5", LocalDate.of(2021, 4, 28), 3));
        jobs.add(new AdWithStudentRatingItem("Title 6", LocalDate.of(2021, 1, 15), 2));
        jobs.add(new AdWithStudentRatingItem("Title 7", LocalDate.of(2021, 3, 13), 2));
        jobs.add(new AdWithStudentRatingItem("Title 8", LocalDate.of(2021, 9, 30), 5));
        return jobs;
    }
}
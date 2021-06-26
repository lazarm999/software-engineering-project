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
import com.parovi.zadruga.items.AdWithStudentRatingItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class StudentJobsFragment extends Fragment {

    public StudentJobsFragment() {
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
        View layout = inflater.inflate(R.layout.fragment_student_jobs_fragment, container, false);

        ArrayList<AdWithStudentRatingItem> jobs = new ArrayList<>();

        jobs.add(new AdWithStudentRatingItem("Waiter", LocalDate.of(2021, 5, 29)));
        jobs.add(new AdWithStudentRatingItem("Market worker", LocalDate.of(2020, 8, 22)));

        RecyclerView recView = layout.findViewById(R.id.recyclerViewJobHistory);
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        JobHistoryAdapter adapter = new JobHistoryAdapter(jobs);
        recView.setAdapter(adapter);

        /*CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas
            }
        });*/

        return layout;
    }
}
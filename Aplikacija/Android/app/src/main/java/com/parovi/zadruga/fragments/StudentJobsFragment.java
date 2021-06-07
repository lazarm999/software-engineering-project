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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentJobsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentJobsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment student_jobs_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentJobsFragment newInstance(String param1, String param2) {
        StudentJobsFragment fragment = new StudentJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_student_jobs_fragment, container, false);

        ArrayList<AdWithStudentRatingItem> jobs = new ArrayList<>();

        jobs.add(new AdWithStudentRatingItem("Title 1", LocalDate.of(2021, 5, 29), 4));
        jobs.add(new AdWithStudentRatingItem("Title 2", LocalDate.of(2020, 8, 22), 5));
        jobs.add(new AdWithStudentRatingItem("Title 3", LocalDate.of(2021, 10, 30), 4));
        jobs.add(new AdWithStudentRatingItem("Title 4", LocalDate.of(2021, 12, 4), 1));
        jobs.add(new AdWithStudentRatingItem("Title 5", LocalDate.of(2021, 4, 28), 3));
        jobs.add(new AdWithStudentRatingItem("Title 6", LocalDate.of(2021, 1, 15), 2));
        jobs.add(new AdWithStudentRatingItem("Title 7", LocalDate.of(2021, 3, 13), 2));
        jobs.add(new AdWithStudentRatingItem("Title 8", LocalDate.of(2021, 9, 30), 5));

        RecyclerView recView = layout.findViewById(R.id.recyclerViewJobHistory);
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        JobHistoryAdapter adapter = new JobHistoryAdapter(jobs);
        recView.setAdapter(adapter);

        return layout;
    }
}
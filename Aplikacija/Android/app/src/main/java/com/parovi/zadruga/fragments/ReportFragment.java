package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.ReportAdapter;
import com.parovi.zadruga.databinding.FragmentReportBinding;
import com.parovi.zadruga.models.entityModels.Report;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.ReportViewModel;

import java.util.List;

public class ReportFragment extends Fragment implements ReportAdapter.ReportListener {

    public ReportFragment() {
    }

    private ReportViewModel model;
    private FragmentReportBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

        RecyclerView recView = binding.rvReports;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ReportAdapter adapter = new ReportAdapter(this);
        recView.setAdapter(adapter);


        model.getReports().observe(requireActivity(), customResponse -> {
            if (customResponse.getStatus() == CustomResponse.Status.OK) {
                adapter.setReportList((List<Report>) customResponse.getBody());
            }
        });
        model.getIsDeleted().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.removeAt((int)customResponse.getBody());
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onReportSelected(Report report) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra(JobAdActivity.AD_ID, report.getAdId());
        startActivity(intent);
    }

    @Override
    public void onDelete(Report report, int pos) {
        model.deleteReport(report.getReportId(), pos);
    }

    @Override
    public void onUsernameClicked(int userId) {
        Toast.makeText(requireContext(), String.valueOf(userId), Toast.LENGTH_LONG).show();
    }
}

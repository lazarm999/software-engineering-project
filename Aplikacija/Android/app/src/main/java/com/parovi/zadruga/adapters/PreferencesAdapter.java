package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.data.JobType;
import com.parovi.zadruga.databinding.ItemJobtypePreferenceBinding;

import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.JobTypeViewHolder> {
    private List<Long> jobTypes;

    public PreferencesAdapter() {

    }
    public void setPreferences(List<Long> jobTypes) {
        this.jobTypes = jobTypes;
    }
    @NonNull
    @Override
    public JobTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemJobtypePreferenceBinding binding = ItemJobtypePreferenceBinding.inflate(inflater, parent, false);
        return new JobTypeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobTypeViewHolder holder, int position) {
        JobType jobType = JobType.getJobTypes().get(position);
        holder.bind(jobType, jobTypes.contains(jobType.getId()));
    }

    @Override
    public int getItemCount() {
        return JobType.getJobTypes().size();
    }

    class JobTypeViewHolder extends RecyclerView.ViewHolder {
        private ItemJobtypePreferenceBinding binding;

        public JobTypeViewHolder(@NonNull ItemJobtypePreferenceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(JobType jobType, boolean selected) {
            binding.switchJobType.setText(jobType.getTitle());
            binding.switchJobType.setChecked(jobTypes.contains(jobType.getId()));
            binding.switchJobType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    jobTypes.add(jobType.getId());
                }
                else {
                    jobTypes.remove(jobType.getId());
                }
            });
        }
    }


}

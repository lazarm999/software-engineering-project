package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.data.ApplicantResume;
import com.parovi.zadruga.data.UserInfoResume;
import com.parovi.zadruga.databinding.ApplicantItemBinding;
import com.parovi.zadruga.databinding.UserListItemBinding;

import java.util.List;

public class ApplicantResumeAdapter extends RecyclerView.Adapter<ApplicantResumeAdapter.ApplicantResumeViewHolder> {
    private List<ApplicantResume> applicants;

    public void setUsers(List<ApplicantResume> users) {
        this.applicants = users;
    }


    @NonNull
    @Override
    public ApplicantResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ApplicantItemBinding binding = ApplicantItemBinding.inflate(inflater, parent, false);
        return new ApplicantResumeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantResumeViewHolder holder, int position) {
        holder.bind(applicants.get(position));
    }

    @Override
    public int getItemCount() {
        return applicants.size();
    }

    class ApplicantResumeViewHolder extends RecyclerView.ViewHolder {
        ApplicantItemBinding binding;

        public ApplicantResumeViewHolder(@NonNull ApplicantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ApplicantResume applicant) {
            binding.tvApplicantName.setText(applicant.getName());
            binding.tvApplicantUsername.setText(applicant.getUsername());
            binding.cboxSelected.setChecked(applicant.isSelected());
            binding.cboxSelected.setOnCheckedChangeListener((buttonView, isChecked) -> applicant.setSelected(isChecked));
        }
    }
}

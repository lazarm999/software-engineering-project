package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.data.ApplicantResume;
import com.parovi.zadruga.data.UserInfoResume;
import com.parovi.zadruga.databinding.ApplicantItemBinding;
import com.parovi.zadruga.databinding.UserListItemBinding;
import com.parovi.zadruga.models.entityModels.User;

import java.util.ArrayList;
import java.util.List;

public class ApplicantResumeAdapter extends RecyclerView.Adapter<ApplicantResumeAdapter.ApplicantResumeViewHolder> {
    private List<User> applicants;

    public void setUsers(List<User> users) {
        this.applicants = users;
    }

    public ApplicantResumeAdapter() {
        super();
        applicants = new ArrayList<>();
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

        public void bind(User applicant) {
            binding.tvApplicantName.setText(applicant.getFirstName() + " " + applicant.getLastName());
            binding.tvApplicantUsername.setText(applicant.getUsername());
            binding.cboxSelected.setChecked(false); //
            binding.cboxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
        }
    }
}

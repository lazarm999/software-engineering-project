package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.AdInAListItemBinding;
import com.parovi.zadruga.items.AdWithStudentRatingItem;
import com.parovi.zadruga.models.entityModels.Ad;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class JobHistoryAdapter extends RecyclerView.Adapter<JobHistoryAdapter.JobHistoryViewHolder> {
    private JobHistoryListListener fragment;
    private List<Ad> jobList;
    private AdInAListItemBinding binding;


    public JobHistoryAdapter(JobHistoryListListener fragment)
    {
        super();
        this.fragment = fragment;
        jobList = new ArrayList<Ad>();
    }
    public void setJobList(List<Ad> jobList) {
        this.jobList = jobList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobHistoryAdapter.JobHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = AdInAListItemBinding.inflate(inflater, parent, false);
        return new JobHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHistoryAdapter.JobHistoryViewHolder holder, int position) {
        holder.bindTo(jobList.get(position));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobHistoryViewHolder extends RecyclerView.ViewHolder {
        private AdInAListItemBinding binding;

        public JobHistoryViewHolder(@NonNull AdInAListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bindTo(Ad ad) {
            binding.tvAdTitle.setText(ad.getTitle());
            binding.txtAdDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(ad.getPostTime()));
            binding.imgUserGradeVector.setImageResource(R.drawable.ic_users_grade);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onJobSelected(ad);
                }
            });
        }
    }

    public interface JobHistoryListListener {
        void onJobSelected(Ad ad);
    }
}

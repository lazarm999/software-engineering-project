package com.parovi.zadruga.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.AdInAListItemBinding;
import com.parovi.zadruga.databinding.ChatResumeLayoutBinding;
import com.parovi.zadruga.items.AdWithStudentRatingItem;
import com.parovi.zadruga.models.entityModels.Ad;

import org.jetbrains.annotations.NotNull;

import kotlinx.coroutines.Job;

public class JobsAdapter extends ListAdapter<AdWithStudentRatingItem, JobsAdapter.JobViewHolder> {
    JobListListener fragment;
    public JobsAdapter(JobListListener fragment) {
        super(DIFF_CALLBACK);
        this.fragment = fragment;
    }
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdInAListItemBinding binding = AdInAListItemBinding.inflate(inflater, parent, false);
        return new JobViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JobViewHolder holder, int position) {
        holder.bindTo(getItem(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onJobItemSelected(getItem(position));
            }
        });
    }

    public static final DiffUtil.ItemCallback<AdWithStudentRatingItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<AdWithStudentRatingItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull AdWithStudentRatingItem oldItem, @NonNull @NotNull AdWithStudentRatingItem newItem)
        {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull AdWithStudentRatingItem oldItem, @NonNull @NotNull AdWithStudentRatingItem newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getIcRating() == newItem.getIcRating();
        }
    };

    class JobViewHolder extends RecyclerView.ViewHolder {
        AdInAListItemBinding binding;

        public JobViewHolder(@NonNull AdInAListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(AdWithStudentRatingItem job) {
            binding.txtAdTitle.setText(job.getTitle());
            binding.txtAdDate.setText(job.getDate().toString());
            binding.imgUserGradeVector.setImageResource(R.drawable.ic_users_grade);
        }
    }
    public interface JobListListener {
        void onJobItemSelected(AdWithStudentRatingItem job);
    }
}

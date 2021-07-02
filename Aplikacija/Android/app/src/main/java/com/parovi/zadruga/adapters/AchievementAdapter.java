package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.AchievementItemBinding;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<RatingResponse> ratingList;

    private AchievementListener fragment;
    public  AchievementAdapter(ArrayList<RatingResponse> achievements)
    {

    }

    public AchievementAdapter(AchievementListener fragment)
    {
        super();
        ratingList = new ArrayList<>();
        this.fragment = fragment;
    }

    public void setAchievements(List<RatingResponse> ratings)
    {
        this.ratingList = ratings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AchievementAdapter.AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AchievementItemBinding binding = AchievementItemBinding.inflate(inflater, parent, false);
        return new AchievementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementAdapter.AchievementViewHolder holder, int position) {
        holder.bindTo(ratingList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onAchievementSelected(ratingList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder {
        public AchievementItemBinding binding;

        public AchievementViewHolder(@NonNull AchievementItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(RatingResponse item)
        {
            binding.tvTitleJob.setText(item.getRater().getFirstName() + " " + item.getRater().getLastName());
            binding.tvGradeJob.setText(String.valueOf(item.getRating()));
            binding.imgRatingJob.setImageResource(R.drawable.ic_users_grade);
            binding.tvCommentJob.setText(item.getComment());
        }

    }

    public interface AchievementListener {
        void onAchievementSelected(RatingResponse achievement);
    }
}

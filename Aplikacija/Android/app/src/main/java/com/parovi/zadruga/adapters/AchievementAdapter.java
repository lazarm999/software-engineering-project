package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.AchievementItemBinding;
import com.parovi.zadruga.items.AchievementItem;

import java.util.ArrayList;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private ArrayList<AchievementItem> achievementList;

    private AchievementListener fragment;

    public AchievementAdapter(AchievementListener fragment)
    {
        super();
        this.fragment = fragment;
    }

    public AchievementAdapter(ArrayList<AchievementItem> achievements)
    {
        this.achievementList = achievements;
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
        holder.bindTo(achievementList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onAchievementSelected(achievementList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder {
        public AchievementItemBinding binding;

        public AchievementViewHolder(@NonNull AchievementItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(AchievementItem item)
        {
            binding.txtAdTitle.setText(item.getTitle());
            binding.txtViewGrade.setText(item.getRating());
            binding.imgUserGradeVector.setImageResource(R.drawable.ic_users_grade);
            binding.txtViewComment.setText(item.getComment());
        }

    }

    public interface AchievementListener {
        void onAchievementSelected(AchievementItem achievement);
    }
}

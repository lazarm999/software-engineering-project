package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.databinding.ItemJobtypePreferenceBinding;
import com.parovi.zadruga.models.entityModels.Tag;

import java.util.ArrayList;
import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.JobTagViewHolder> {
    private List<Tag> tags;

    public PreferencesAdapter() {
        tags = new ArrayList<Tag>();
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public JobTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemJobtypePreferenceBinding binding = ItemJobtypePreferenceBinding.inflate(inflater, parent, false);
        return new JobTagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobTagViewHolder holder, int position) {
        holder.bindTo(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class JobTagViewHolder extends RecyclerView.ViewHolder {
        private ItemJobtypePreferenceBinding binding;

        public JobTagViewHolder(@NonNull ItemJobtypePreferenceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Tag tag) {
            binding.switchJobType.setText(tag.getName());
            binding.switchJobType.setChecked(false);
            binding.switchJobType.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // TODO:
                }
                else {
                    // TODO:
                }
            });
        }
    }


}

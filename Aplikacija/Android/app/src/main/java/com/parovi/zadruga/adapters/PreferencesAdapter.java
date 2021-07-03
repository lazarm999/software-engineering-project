package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ItemJobtypePreferenceBinding;
import com.parovi.zadruga.models.entityModels.Tag;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.JobTagViewHolder> {
    public static Dictionary<Integer, Integer> tagToImgRes;
    private PreferencesListener fragment;
    private List<Tag> tags;
    private List<Integer> selectedTagIds;

    static {
        tagToImgRes = new Hashtable<>();
        tagToImgRes.put(1, R.drawable.hostess_prefer);
        tagToImgRes.put(2, R.drawable.promoter_prefer);
        tagToImgRes.put(3, R.drawable.chef_prefer);
        tagToImgRes.put(4, R.drawable.interviewer_pref);
        tagToImgRes.put(5, R.drawable.delivery_prefer);
        tagToImgRes.put(6, R.drawable.waiter);
        tagToImgRes.put(7, R.drawable.lighter_phys_pref);
        tagToImgRes.put(8, R.drawable.heavy_phys_pref);
    }

    public PreferencesAdapter(PreferencesListener listener) {
        fragment = listener;
        tags = new ArrayList<Tag>();
        selectedTagIds = new ArrayList<>();
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }
    public void setSelectedTagIds(List<Integer> selectedTagIds) {
        this.selectedTagIds = selectedTagIds;
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
        holder.binding.switchJobType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fragment.onTagToggled(tags.get(position), isChecked);
            }
        });
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
            Integer imgRes = PreferencesAdapter.tagToImgRes.get(tag.getTagId());
            if (imgRes != null) {
                binding.imageView4.setImageResource(imgRes);
            }
            boolean selected = selectedTagIds.contains(tag.getTagId());
            binding.switchJobType.setChecked(selected);
        }
    }

    public interface PreferencesListener {
        void onTagToggled(Tag tag, boolean checked);
    }

}

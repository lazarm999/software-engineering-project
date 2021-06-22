package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.AdItemBinding;
import com.parovi.zadruga.databinding.ChatResumeLayoutBinding;
import com.parovi.zadruga.items.AdItem;
import com.parovi.zadruga.models.entityModels.Ad;

import java.util.ArrayList;

public class  AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private ArrayList<Ad> adList;

    public AdAdapter(ArrayList<Ad> ads)
    {
        this.adList = ads;
    }

    private AdListListener fragment;

    public AdAdapter(AdListListener fragment) {
        super();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdAdapter.AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdItemBinding binding = AdItemBinding.inflate(inflater, parent, false);
        return new AdViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdAdapter.AdViewHolder holder, int position) {
        holder.bindTo(adList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onAdSelected(adList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        public AdItemBinding binding;


        public AdViewHolder(@NonNull AdItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Ad ad) {
            binding.txtAdItemHeader.setText(ad.getTitle());
            binding.editTxtDescriptionAd.setText(ad.getDescription());
            binding.imgAdItem.setImageResource(R.drawable.ad_item);
            binding.editTxtAdItemDate.setText(ad.getPostTime().toString());
        }
    }

    public interface AdListListener {
        void onAdSelected(Ad ad);
    }
}

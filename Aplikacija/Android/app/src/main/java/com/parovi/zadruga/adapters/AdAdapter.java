package com.parovi.zadruga.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.AdItemBinding;
import com.parovi.zadruga.models.entityModels.Ad;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import de.measite.minidns.record.A;

public class  AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private List<Ad> adList;

    public AdAdapter()
    {

    }

    private AdListListener fragment;

    public AdAdapter(AdListListener fragment) {
        super();
        adList = new ArrayList<Ad>();
        this.fragment = fragment;
    }

    public void setAds(ArrayList<Ad> ads)
    {
        this.adList = ads;
    }

    @NonNull
    @Override
    public AdAdapter.AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdItemBinding binding = AdItemBinding.inflate(inflater, parent, false);
        return new AdViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bindTo(Ad ad) {
            binding.txtDescForAd.setText(ad.getTitle());
            binding.editTxtDescriptionAd.setText(ad.getDescription());
            binding.imgAdIcon.setImageResource(R.drawable.ad_item);
            LocalDate localDate = ad.getPostTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //binding.editTxtAdItemDate.setText(localDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)));
            binding.editTxtAdItemDate.setText(localDate.toString());
        }
    }

    public interface AdListListener {
        void onAdSelected(Ad ad);
    }
}

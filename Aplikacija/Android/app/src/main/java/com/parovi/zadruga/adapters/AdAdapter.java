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
import com.parovi.zadruga.items.AdItem;

import java.util.ArrayList;

public class  AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private ArrayList<AdItem> adList;

    public AdAdapter(ArrayList<AdItem> ads)
    {
        this.adList = ads;
    }

    @NonNull
    @Override
    public AdAdapter.AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item,parent,false);
        AdViewHolder avh = new AdViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdAdapter.AdViewHolder holder, int position) {
        AdItem currAd = adList.get(position);
        holder.tvTitle.setText(currAd.getTitle());
        holder.etDescription.setText(currAd.getDesc());
        holder.imgRes.setImageResource(R.drawable.ad_item);
        holder.etDate.setText(currAd.getDate().toString());

        /*holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //otvoriti odgvovarajuci oglas - getAdById();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView etDescription;
        private TextView etDate;
        private ImageView imgRes;
        private CardView card;


        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.txtAdItemHeader);
            etDescription = itemView.findViewById(R.id.editTxtDescriptionAd);
            etDate = itemView.findViewById(R.id.editTxtAdItemDate);
            imgRes = itemView.findViewById(R.id.imgAdItem);
            card = itemView.findViewById(R.id.cardAdItem);

        }

        public TextView getTvTitle() {
            return tvTitle;
        }
        public TextView getEtDescription() {
            return etDescription;
        }
        public TextView getEtDate() {
            return etDate;
        }
        public ImageView getImgRes() {
            return imgRes;
        }
    }
}

package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.items.AchievementItem;

import java.util.ArrayList;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private ArrayList<AchievementItem> achievementList;

    public AchievementAdapter(ArrayList<AchievementItem> achievements)
    {
        this.achievementList = achievements;
    }

    @NonNull
    @Override
    public AchievementAdapter.AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item, parent, false);
        AchievementAdapter.AchievementViewHolder avh = new AchievementViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementAdapter.AchievementViewHolder holder, int position) {
        AchievementItem currAch = achievementList.get(position);

        holder.tvTitle.setText(currAch.getTitle());
        //holder.tvDate.setText(currAch.getDate().toString());
        holder.tvGrade.setText(Float.toString(currAch.getRating()));
        holder.imgRes.setImageResource(R.drawable.ic_users_grade);
        holder.tvComment.setText(currAch.getComment());
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        //private TextView tvDate;
        private TextView tvGrade;
        private ImageView imgRes;
        private TextView tvComment;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.txtAdTitle);
            //tvDate = itemView.findViewById(R.id.txtAdDate);
            tvGrade = itemView.findViewById(R.id.txtViewGrade);
            imgRes = itemView.findViewById(R.id.imgUserGradeVector);
            tvComment = itemView.findViewById(R.id.txtViewComment);
        }

        public TextView getTvTitle() {return this.tvTitle;}
        //public TextView getTvDate() {return this.tvDate;}
        public TextView getTvGrade() {return this.tvGrade;}
        public ImageView getImgRes() {return this.imgRes;}
        public TextView getTvComment() {return  this.tvComment;}

    }
}

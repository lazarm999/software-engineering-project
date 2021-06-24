package com.parovi.zadruga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.items.AdWithStudentRatingItem;

import java.util.ArrayList;

public class JobHistoryAdapter extends RecyclerView.Adapter<JobHistoryAdapter.JobHistoryViewHolder> {

    private ArrayList<AdWithStudentRatingItem> jobList;

    public JobHistoryAdapter(ArrayList<AdWithStudentRatingItem> jobs)
    {
        this.jobList = jobs;
    }

    @NonNull
    @Override
    public JobHistoryAdapter.JobHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_in_a_list_item, parent, false);
        JobHistoryViewHolder jvh = new JobHistoryViewHolder(v);
        return jvh;
    }

    @Override
    public void onBindViewHolder(@NonNull JobHistoryAdapter.JobHistoryViewHolder holder, int position) {
        AdWithStudentRatingItem currJob = jobList.get(position);

        holder.tvTitle.setText(currJob.getTitle());
        holder.tvDate.setText(currJob.getDate().toString());
        holder.txtGrade.setText(Float.toString(currJob.getRating()));
        holder.imgRes.setImageResource(R.drawable.ic_users_grade);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private TextView txtGrade;
        private ImageView imgRes;

       // public JobHistoryBinding binding;

        public JobHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txtAdTitle);
            tvDate = itemView.findViewById(R.id.txtAdDate);
            txtGrade = itemView.findViewById(R.id.txtViewGrade);
            imgRes = itemView.findViewById(R.id.imgUserGradeVector);
        }

        public TextView getTvTitle() {return this.tvTitle;}
        public TextView getTvDate() {return this.tvDate;}
        public TextView getEtGrade() {return this.txtGrade;}
        public ImageView getImgRes() {return this.imgRes;}

    }

    public interface JobHistoryListListener {
        void onJobSelected(AdWithStudentRatingItem item);
    }
}

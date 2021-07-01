package com.parovi.zadruga.adapters;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ItemReportBinding;
import com.parovi.zadruga.fragments.ReportFragment;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.Report;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportedViewHolder>  {
    private static final int adReport = 1, commentReport = 2;
    private List<Report> reportList;
    private ReportFragment reportFragment;

    public ReportAdapter(ReportFragment reportFragment){
        this.reportList = new ArrayList<>();
        this.reportFragment = reportFragment;
    }

    public void setReportList(List<Report> reportList){
        this.reportList = reportList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ReportedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReportBinding binding = ItemReportBinding.inflate(inflater, parent, false);
        switch (viewType) {
            case adReport:
                return new AdReportedViewHolder(binding);
            default:
                return new CommentReportedViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReportAdapter.ReportedViewHolder holder, int position) {
        holder.bindTo(reportList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFragment.onReportSelected(reportList.get(position));
            }
        });
        holder.binding.tvDeleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFragment.onDelete(reportList.get(position), position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(reportList.get(position).getType().equals(Constants.AD_REPORT))
            return adReport;
        else
            return commentReport;
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    private List<Integer> findUsernamesStart(String s){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '@')
                list.add(i);
        }
        return list;
    }

    abstract public class ReportedViewHolder extends RecyclerView.ViewHolder {
        public ItemReportBinding binding;

        public ReportedViewHolder(@NonNull ItemReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(Report report){
            String reportTitle = App.getAppContext().getString(R.string.reportTitle, "@" + report.getReporter().getUsername(),
                    "@" + report.getReported().getUsername());
            SpannableString spannableString = new SpannableString(reportTitle);
            ClickableSpan reporterSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    reportFragment.onUsernameClicked(report.getFkReporterId(), report.getReporter().isEmployer());
                }
            };
            ClickableSpan reportedSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    reportFragment.onUsernameClicked(report.getFkReporterId(), report.getReported().isEmployer());
                }
            };
            List<Integer> startList = findUsernamesStart(reportTitle);
            spannableString.setSpan(reporterSpan, startList.get(0),startList.get(0) + report.getReporter().getUsername().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(reportedSpan, startList.get(1),startList.get(1) + report.getReported().getUsername().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvReportTitle.setText(spannableString);
            binding.tvReportTitle.setMovementMethod(LinkMovementMethod.getInstance());
            binding.tvElaboration.setText(report.getElaboration());

        }
    }

    public class AdReportedViewHolder extends ReportAdapter.ReportedViewHolder {
        public AdReportedViewHolder(ItemReportBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Report report) {
            super.bindTo(report);
            binding.ivReportIcon.setImageResource(R.drawable.bad_ad);
            binding.tvReportedContent.setText("\"" + report.getAdTitle() + "\"");
        }

    }

    public class CommentReportedViewHolder extends ReportAdapter.ReportedViewHolder {
        public CommentReportedViewHolder(ItemReportBinding binding) {
            super(binding);
        }

        @Override
        void bindTo(Report report) {
            super.bindTo(report);
            binding.ivReportIcon.setImageResource(R.drawable.bad_comment);
            binding.tvReportedContent.setText("\"" + report.getCommentText() + "\"");
        }

    }

    public void removeAt(int position) {
        reportList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, reportList.size());
    }

    public interface ReportListener {
        void onReportSelected(Report report);
        void onDelete(Report report, int pos);
        void onUsernameClicked(int userId, boolean isEmployer);
    }
}

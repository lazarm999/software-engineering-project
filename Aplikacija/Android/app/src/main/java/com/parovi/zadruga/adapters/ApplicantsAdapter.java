package com.parovi.zadruga.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ApplicantItemBinding;
import com.parovi.zadruga.models.entityModels.User;

import org.jetbrains.annotations.NotNull;

public class ApplicantsAdapter extends ListAdapter<User, ApplicantsAdapter.ApplicantViewHolder> {
    ApplicantListListener fragment;

    public ApplicantsAdapter(ApplicantListListener fragment) {
        super(DIFF_CALLBACK);
        this.fragment = fragment;
    }

    @Override
    public ApplicantViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ApplicantItemBinding binding = ApplicantItemBinding.inflate(inflater, parent, false);
        return new ApplicantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ApplicantViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onApplicantSelected(getItem(position));
            }
        });
    }

    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull User oldItem, @NonNull @NotNull User newItem) {
            Log.d("DEBUG", "areItemsTheSame: entered");
            return oldItem.getUserId() == newItem.getUserId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull @NotNull User oldItem, @NonNull @NotNull User newItem) {
            Log.d("DEBUG", "areContentsTheSame: entered");
            return oldItem.getUserId() == newItem.getUserId() &&
                    oldItem.getFirstName().equals(newItem.getFirstName()) &&
                    oldItem.getLastName().equals(newItem.getLastName()) &&
                    oldItem.getUsername().equals(newItem.getUsername()) &&
                    oldItem.getProfileImage() == newItem.getProfileImage();
        }
    };

    class ApplicantViewHolder extends RecyclerView.ViewHolder {
        ApplicantItemBinding binding;

        public ApplicantViewHolder(@NonNull ApplicantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User applicant) {
            binding.tvApplicantName.setText(applicant.getFirstName() + " " + applicant.getLastName());
            binding.tvApplicantUsername.setText(applicant.getUsername());
            if (applicant.getProfileImage() != null)
                binding.imageView5.setImageBitmap(applicant.getProfileImage());
            else
                binding.imageView5.setImageResource(R.drawable.avatar);
            binding.cboxSelected.setChecked(false); //
            binding.cboxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    fragment.onApplicantToggled(applicant);
                }
            });
        }
    }

    public interface ApplicantListListener {
        void onApplicantSelected(User applicant);

        void onApplicantToggled(User applicant);
    }
}

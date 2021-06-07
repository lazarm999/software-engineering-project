package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.CommentsAdapter;
import com.parovi.zadruga.data.JobAd;
import com.parovi.zadruga.databinding.FragmentJobAdvertisementBinding;
import com.parovi.zadruga.viewModels.JobAdViewModel;


public class JobAdInfoFragment extends Fragment {
    private JobAdViewModel model;
    private FragmentJobAdvertisementBinding binding;

    public JobAdInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobAdvertisementBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(JobAdViewModel.class);
        bind(model.getJobAd().getValue());
        return binding.getRoot();
    }

    private void bind(JobAd jobAd) {
        binding.tvJobTitle.setText(jobAd.getTitle());
        binding.tvJobDesc.setText(jobAd.getDescription());
        binding.tvLocation.setText(jobAd.getLocation());
        String feeRange = Float.valueOf(jobAd.getCompensationFrom()).toString() + " - " + Float.valueOf(jobAd.getCompensationTo()).toString();
        binding.tvFeeRange.setText(feeRange);
        if (model.getJobAd().getValue().isMine()) {
            binding.btnApply.setText("Select workers");
            binding.btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(binding.getRoot()).navigate(JobAdInfoFragmentDirections.actionJobAdFragmentToSelectWorkersFragment());
                }
            });
        }
        else {
            // apliciraj za posao
        }
        binding.rvComments.setHasFixedSize(true);
        /*rvlmNComments = new LinearLayoutManager(this);
        rvaComments = new CommentsAdapter(commentsList);

        rvComments.setLayoutManager(rvlmNComments);
        rvComments.setAdapter(rvaComments);*/
    }
}
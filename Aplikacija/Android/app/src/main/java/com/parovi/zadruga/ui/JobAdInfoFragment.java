package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.CommentsAdapter;
import com.parovi.zadruga.data.JobAdInfo;
import com.parovi.zadruga.databinding.FragmentJobAdvertisementBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.viewModels.JobAdViewModel;

import java.util.List;


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

        CommentsAdapter adapter = new CommentsAdapter();
        binding.rvComments.setAdapter(adapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.imgPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.postAComment(binding.etComment.getText().toString().trim());
                binding.etComment.setText("");
            }
        });

        model.getIsPosted().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    Toast.makeText(requireContext(), "Your comment has been successfully posted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), "Your comment failed to be posted", Toast.LENGTH_SHORT).show();
            }
        });
        model.getAd().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    Toast.makeText(requireContext() , "Greska", Toast.LENGTH_SHORT).show();
                    return;
                }
                bindAdInfo(new JobAdInfo((Ad)customResponse.getBody()));
            }
        });
        model.getAppliedTo().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK)
                    return;
                appliedStatusChanged((boolean)customResponse.getBody());
            }
        });
        model.getComments().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    return;
                }
                adapter.setCommentsList((List<CommentResponse>)customResponse.getBody());
            }
        });

        return binding.getRoot();
    }

    private void bindAdInfo(JobAdInfo jobAd) {
        binding.tvJobTitle.setText(jobAd.getTitle());
        binding.tvJobDesc.setText(jobAd.getDescription());
        binding.tvLocation.setText(jobAd.getLocation());
        binding.dtPosted.setText(jobAd.getTime());
        binding.tvPeopleNeeded.setText(Integer.toString(jobAd.getNoApplicantsRequired()));
        String feeRange = Float.valueOf(jobAd.getCompensationFrom()).toString() + " - " + Float.valueOf(jobAd.getCompensationTo()).toString() + " RSD";
        binding.tvFeeRange.setText(feeRange);

        if (model.isAdMine()) {
            binding.btnApply.setText("Select workers");
            binding.btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(binding.getRoot()).navigate(JobAdInfoFragmentDirections.actionJobAdFragmentToSelectWorkersFragment());
                }
            });
        }
        else { // user
            appliedStatusChanged(false);
        }

    }

    private void appliedStatusChanged(boolean applied) {
        if (applied){
            binding.btnApply.setText("Unapply");
            binding.btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.withdrawApplication();
                }
            });
        }
        else {
            binding.btnApply.setText("Apply");
            binding.btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.applyForAd();
                }
            });
        }
    }

}
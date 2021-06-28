package com.parovi.zadruga.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.adapters.CommentsAdapter;
import com.parovi.zadruga.databinding.FragmentJobAdvertisementBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.viewModels.AdViewModel;

import java.text.DateFormat;
import java.util.List;


public class JobAdInfoFragment extends Fragment {
    private AdViewModel model;
    private FragmentJobAdvertisementBinding binding;

    public JobAdInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobAdvertisementBinding.inflate(inflater, container, false);
        // make Ad dependent parts invisible until data arrives
        binding.tvAdNotFound.setVisibility(View.INVISIBLE);
        binding.clAdInfo.setVisibility(View.INVISIBLE);
        binding.clPostComment.setVisibility(View.INVISIBLE);
        binding.rvComments.setVisibility(View.INVISIBLE);

        // obtain the viewModel
        model = new ViewModelProvider(requireActivity()).get(AdViewModel.class);
        // create adapter and bind it to recycler view
        CommentsAdapter adapter = new CommentsAdapter();
        binding.rvComments.setAdapter(adapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        // implement posting a comment
        binding.imgPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.postAComment(binding.etComment.getText().toString().trim());
                binding.etComment.setText("");
            }
        });
        // observe LiveData
        model.getIsPosted().observe(requireActivity(), new Observer<CustomResponse<?>>() { // nicemu ne sluzi
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
                    binding.tvAdNotFound.setVisibility(View.VISIBLE);
                    return;
                }
                binding.tvAdNotFound.setVisibility(View.GONE);
                binding.clAdInfo.setVisibility(View.VISIBLE);
                binding.clPostComment.setVisibility(View.VISIBLE);
                bindAndUpdateScreen((Ad)customResponse.getBody());
            }
        });
        model.getAppliedTo().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK)
                    return;
                appliedStatusChanged((Boolean)customResponse.getBody());
            }
        });
        model.getComments().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    return;
                }
                adapter.setCommentsList((List<CommentResponse>)customResponse.getBody());
                binding.rvComments.setVisibility(View.VISIBLE);
            }
        });
        // return root view
        return binding.getRoot();
    }

    private void bindAndUpdateScreen(Ad ad) {
        binding.tvJobTitle.setText(ad.getTitle());
        binding.tvJobDesc.setText(ad.getDescription());
        binding.tvLocation.setText(ad.getLocation().getCityName());
        binding.dtPosted.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(ad.getPostTime()));
        binding.tvPeopleNeeded.setText(Integer.toString(ad.getNumberOfEmployees()));
        String feeRange = Float.valueOf(ad.getCompensationMin()).toString() + " - " + Float.valueOf(ad.getCompensationMax()).toString() + " RSD";
        binding.tvFeeRange.setText(feeRange);
        binding.tvNoOfApplications.setText(ad.getNumberOfApplied() + " applied");

        if (model.isAdClosed())
            updateViewAdClosed();
        else if (Utility.getLoggedInUser(requireContext()).isEmployer())
            updateViewAdMine(model.isAdMine());
    }

    private void updateViewAdClosed() {
        binding.btnApply.setText("Closed");
        binding.btnApply.setEnabled(false);
        binding.clPostComment.setVisibility(View.GONE);
    }
    private void updateViewAdMine(boolean isMine) {
        if (isMine) {
            binding.btnApply.setText("Select workers");
            binding.btnApply.setOnClickListener(selectWorkersClick);
        }
        else {
            binding.btnApply.setVisibility(View.GONE);
            binding.clPostComment.setVisibility(View.GONE);
        }
    }
    private void appliedStatusChanged(boolean applied) {
        if (applied){
            binding.btnApply.setText("Unapply"); // TODO: koristi String resources
            binding.btnApply.setOnClickListener(unapplyClick);
        }
        else {
            binding.btnApply.setText("Apply");
            binding.btnApply.setOnClickListener(applyClick);
        }
    }
    View.OnClickListener applyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            model.applyForAd();
        }
    }, unapplyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            model.withdrawApplication();
        }
    }, selectWorkersClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(binding.getRoot()).navigate(JobAdInfoFragmentDirections.actionJobAdFragmentToSelectWorkersFragment());
        }
    };


}
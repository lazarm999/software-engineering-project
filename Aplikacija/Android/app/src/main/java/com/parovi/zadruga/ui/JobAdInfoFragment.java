package com.parovi.zadruga.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.adapters.CommentsAdapter;
import com.parovi.zadruga.databinding.FragmentJobAdvertisementBinding;
import com.parovi.zadruga.fragments.StudentProfileFragment;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.viewModels.AdViewModel;

import java.text.DateFormat;
import java.util.List;


public class JobAdInfoFragment extends Fragment implements CommentsAdapter.CommentListListener{
    private final static int EDIT_ITEM = 0, REPORT_ITEM = 1, DELETE_ITEM = 2;

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
        CommentsAdapter adapter = new CommentsAdapter(this);
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
        binding.toolbar.setTitle("Ads");
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itReportAd:
                        onAdReport();
                        break;
                    case R.id.itEditAd:
                        onAdEdit();
                        break;
                    case R.id.itDeleteAd:
                        onAdDelete();
                        break;
                    default:
                        break;
                }
                return true;
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
                Ad ad = (Ad)customResponse.getBody();
                adapter.setAd(ad);
                bindAndUpdateScreen(ad);
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
        model.getApplicantsSelected().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK && (boolean)customResponse.getBody())
                    updateViewAdClosed();
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
        model.getIsDeleted().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    requireActivity().finish();
                    // ili na neki drugi nacin
                }
            }
        });
        model.getIsDeletedComment().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK)
                    adapter.removeItem((int)customResponse.getBody()); // TODO: podseti Lazara da azurira lokalnu bazu
            }
        });
        model.getIsReported().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    return;
                }
                // TODO: sta se desava kad oglas bude prijavljen
            }
        });

        model.getIsReportedComment().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() != CustomResponse.Status.OK) {
                    return;
                }
                // TODO: sta se desava kad komentar bude prijavljen? Neki dialog da mu javi?
            }
        });


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
        updateToolBar();
        if (model.isAdClosed())
            updateViewAdClosed();
        else if (Utility.getLoggedInUser(requireContext()).isEmployer())
            updateViewAdMine(model.isAdMine());
    }

    private void updateToolBar() {
        User loggedUser = Utility.getLoggedInUser(requireContext());
        Ad ad = model.getAd().getValue() == null ? null : (Ad)model.getAd().getValue().getBody();
        if (loggedUser.isAdmin()) {
            binding.toolbar.getMenu().getItem(EDIT_ITEM).setVisible(false);
            binding.toolbar.getMenu().getItem(REPORT_ITEM).setVisible(false);
            binding.toolbar.getMenu().getItem(DELETE_ITEM).setVisible(true);
        }
        else if (ad != null && loggedUser.isEmployer() && loggedUser.getUserId() == ad.getEmployer().getUserId()) {
            binding.toolbar.getMenu().getItem(EDIT_ITEM).setVisible(true);
            binding.toolbar.getMenu().getItem(REPORT_ITEM).setVisible(false);
            binding.toolbar.getMenu().getItem(DELETE_ITEM).setVisible(true);
        }
        else {
            binding.toolbar.getMenu().getItem(EDIT_ITEM).setVisible(false);
            binding.toolbar.getMenu().getItem(REPORT_ITEM).setVisible(true);
            binding.toolbar.getMenu().getItem(DELETE_ITEM).setVisible(false);
        }
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
    },
            unapplyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            model.withdrawApplication();
        }
    },
            selectWorkersClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(binding.getRoot()).navigate(JobAdInfoFragmentDirections.actionJobAdInfoFragmentToSelectWorkersFragment());
        }
    };

    private void onAdReport() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle(R.string.dialogAd);

        final EditText etReportText = new EditText(requireActivity());
        etReportText.setInputType(InputType.TYPE_CLASS_TEXT);
        etReportText.setTextSize(16);
        dialog.setView(etReportText);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.reportAd(etReportText.getText().toString().trim());
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void onAdEdit() {
        JobAdInfoFragmentDirections.ActionJobAdInfoFragmentToNewAdFragment action = JobAdInfoFragmentDirections.actionJobAdInfoFragmentToNewAdFragment();
        action.setAdId(model.getAdId());
        Navigation.findNavController(requireActivity(), R.id.job_ad_nav_host_fragment).navigate(action);
    }
    private void onAdDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle("Delete the Ad");

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.deleteAd();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onCommentAuthorSelected(CommentResponse comment) {
        Intent intent = new Intent(requireActivity(), EditBasicProfileInfoFragment.class);
        //TODO: proveri da li ovo radi
        intent.putExtra(StudentProfileFragment.USER_ID, comment.getUser().getUserId());
        startActivity(intent);
    }
    @Override
    public void onCommentReported(CommentResponse comment) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle(R.string.dialogComment);

        final EditText etReportText = new EditText(requireActivity());
        etReportText.setInputType(InputType.TYPE_CLASS_TEXT);
        etReportText.setTextSize(16);
        dialog.setView(etReportText);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String elaboration = etReportText.getText().toString();
                model.reportComment(comment, elaboration);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    @Override
    public void onCommentDeleted(CommentResponse comment, int pos) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle("Delete comment");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.deleteComment(comment, pos);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
package com.parovi.zadruga.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.databinding.FragmentNewAdBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.PreferredTag;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.requestModels.EditAdRequest;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.viewModels.AdViewModel;
import com.parovi.zadruga.viewModels.NewAdViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewAdFragment extends Fragment {
    private FragmentNewAdBinding binding;

    //boolean[] selectedItems = new boolean[categoryArray.length];
    //ArrayList<Integer> categoryList = new ArrayList<>();
    //private static String[] categoryArray = {"Hostess", "Promoter", "Kitchen support staff", "Interviewer", "Collection operations", "Waiter", "Lighter physical jobs", "Heavier physical jobs"};
    private NewAdViewModel editModel;
    private AdViewModel adModel = null;
    boolean isUpdated = false;
    //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    public NewAdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adModel = new ViewModelProvider(requireActivity()).get(AdViewModel.class);
            adModel.initializeTagLists();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewAdBinding.inflate(inflater, container, false);

        ArrayAdapter<Location> adapterLoc = new ArrayAdapter<Location>(container.getContext(), android.R.layout.simple_list_item_1, new ArrayList<Location>());
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLocation.setAdapter(adapterLoc);

        binding.btnChooseCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adModel == null) {
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_newAdFragment2_to_selectPreferencesFragment3);
                }
                else {
                    NewAdFragmentDirections.ActionNewAdFragmentToSelectPreferencesFragment22 action = NewAdFragmentDirections.actionNewAdFragmentToSelectPreferencesFragment22();
                    action.setForAd(true);
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            }
        });

        editModel = new ViewModelProvider(requireActivity()).get(NewAdViewModel.class);

        editModel.getLocations().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK)
                {
                    adapterLoc.clear();
                    adapterLoc.addAll(editModel.getAllCities());
                   // makeToast(R.string.successfulLocation);
                }
//                else if(customResponse.getStatus() == CustomResponse.Status.BAD_REQUEST)
//                {
//                    Toast.makeText(requireContext(), "You got the locations", Toast.LENGTH_SHORT).show();
//                }
//                else if(customResponse.getStatus() == CustomResponse.Status.SERVER_ERROR)
//                {
//                    Toast.makeText(requireContext(), "Error with server", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        editModel.getIsPosted().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK)
                {
                    //makeToast(R.string.successfulNewAd);
                }
                else if(customResponse.getStatus() == CustomResponse.Status.BAD_REQUEST)
                {
                    //makeToast(R.string.badRequestNewAd);
                }
                else if(customResponse.getStatus() == CustomResponse.Status.SERVER_ERROR)
                {
                    //makeToast(R.string.serverErrorNewAd);
                }
            }
        });

        loadViews();
        return binding.getRoot();
    }

    private void makeToast(int string) {
        LayoutInflater inflaterToast = getLayoutInflater();
        View layoutToast = inflaterToast.inflate(R.layout.toast_layout, (ViewGroup) getView().findViewById(R.id.toast));
        Toast toast = new Toast(getContext());
        TextView txtToast = (TextView) layoutToast.findViewById(R.id.txtToast);
        txtToast.setText(string);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layoutToast);
        toast.show();
    }

    private PostAdRequest createPostAdRequest() {
        return new PostAdRequest(binding.txtNewAdTitle.getText().toString(),
                binding.editTxtNewAdDescription.getText().toString(),
                Integer.parseInt(binding.editTxtNewAdFeeFrom.getText().toString().isEmpty() ? "0" : binding.editTxtNewAdFeeFrom.getText().toString()),
                Integer.parseInt(binding.editTxtNewAdFeeTo.getText().toString().isEmpty() ? "0" : binding.editTxtNewAdFeeTo.getText().toString()),
                Integer.parseInt(binding.editTxtNewAdPeopleNeeded.getText().toString()),
                ((Location)binding.spinnerLocation.getSelectedItem()).getLocId(), editModel.getNewSelectedTags());
    }
    private EditAdRequest createEditAdRequest() {
        return new EditAdRequest(binding.txtNewAdTitle.getText().toString(),
                binding.editTxtNewAdDescription.getText().toString(),
                Integer.parseInt(binding.editTxtNewAdFeeFrom.getText().toString().isEmpty() ? "0" : binding.editTxtNewAdFeeFrom.getText().toString()),
                Integer.parseInt(binding.editTxtNewAdFeeTo.getText().toString().isEmpty() ? "0" : binding.editTxtNewAdFeeTo.getText().toString()),
                Integer.parseInt(binding.editTxtNewAdPeopleNeeded.getText().toString()),
                ((Location)binding.spinnerLocation.getSelectedItem()).getLocId(),
                PreferredTag.ListDiff(adModel.getNewSelectedTags(), adModel.getCurrentlySelectedTags()),
                PreferredTag.ListDiff(adModel.getCurrentlySelectedTags(), adModel.getNewSelectedTags()));
    }
    /*
    private void updateTagLists() {
        builder.setMultiChoiceItems(categoryArray, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    categoryList.add(which);
                } else {
                    categoryList.remove(which);
                }
            }
        });
    }
    private void loadTags() {
        builder.setTitle(R.string.dialogMultipleChoiceTitle);
        builder.setCancelable(false);
        updateTagLists();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < categoryList.size(); j++) {
                    stringBuilder.append(categoryArray[categoryList.get(j)]);

                    if (j != categoryList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }

                binding.txtCategoryList.setText(stringBuilder);
            }
        });

        builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(R.string.btnNeutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int j = 0; j < selectedItems.length; j++) {
                    selectedItems[j] = false;
                    categoryList.clear();
                    binding.txtCategoryList.clearComposingText();
                }
            }
        });
    }*/
    private void loadViews() {
        if (adModel != null && adModel.getAd().getValue() != null) {
            Ad ad = (Ad)adModel.getAd().getValue().getBody();
            binding.txtNewAdTitle.setText(ad.getTitle());
            binding.editTxtNewAdDescription.setText(ad.getDescription());
            binding.editTxtNewAdPeopleNeeded.setText(Integer.toString(ad.getNumberOfEmployees()));
            binding.editTxtNewAdFeeFrom.setText(Integer.toString(ad.getCompensationMin()));
            binding.editTxtNewAdFeeTo.setText(Integer.toString(ad.getCompensationMax()));
        }
        adjustButtons();
    }
    private void adjustButtons() {
        if (adModel != null) {
            binding.btnNewAdAdd.setText(R.string.submitChanges);
            binding.btnNewAdAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adModel.updateAd(createEditAdRequest());
                }
            });
            binding.btnNewAdCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(binding.getRoot()).navigate(NewAdFragmentDirections.actionNewAdFragmentToJobAdInfoFragment());
                }
            });
            adModel.getIsUpdated().observe(requireActivity(), new Observer<CustomResponse<?>>() {
                @Override
                public void onChanged(CustomResponse<?> customResponse) {
                    if (customResponse.getStatus() == CustomResponse.Status.OK && !isUpdated) {
                        adModel.load();
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.job_ad_nav_host_fragment);
                        /*while(navController.popBackStack())
                            ;
                        navController.navigate(NewAdFragmentDirections.actionNewAdFragmentToJobAdInfoFragment());*/
                        navController.popBackStack();
                        isUpdated = true;
                        customResponse.setStatus(null);
                    }
                }
            });
        }
        else {
            binding.btnNewAdAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editModel.postAd(Utility.getAccessToken(App.getAppContext()), createPostAdRequest());
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_adsFragment);
                    /*FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                    fr.replace(R.id.bottom_nav_employer, new AdsFragment());
                    fr.commit();*/
                }
            });
            binding.btnNewAdCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_adsFragment);
                    /*FragmentTransaction fr = requireActivity().getSupportFragmentManager().beginTransaction();
                    fr.replace(R.id.bottom_nav_employer, new AdsFragment());
                    fr.commit();*/
                }
            });
        }
        /*binding.btnChooseCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(NewAdFragmentDirections.actionNewAdFragmentToJobAdFragment());
            }
        });*/
    }
}
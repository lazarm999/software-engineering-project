package com.parovi.zadruga.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.activities.MainActivity;
import com.parovi.zadruga.adapters.AdAdapter;
import com.parovi.zadruga.databinding.FragmentAdsFragmentBinding;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.ui.ChatActivity;
import com.parovi.zadruga.ui.JobAdActivity;
import com.parovi.zadruga.viewModels.FeedViewModel;

import java.util.ArrayList;

public class AdsFragment extends Fragment implements AdAdapter.AdListListener {

    FragmentAdsFragmentBinding binding;
    FeedViewModel model;
    int page = 0;
    int limit = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdsFragmentBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);

        binding.topBarAdmin.setVisibility(View.GONE);

        Spinner spinnerLocations = binding.spinnerLocationFilter;
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(adapterLoc);


        RecyclerView recView = binding.recViewAds;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        AdAdapter adapter = new AdAdapter(this);
        recView.setAdapter(adapter);

        model.getUser().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK)
                {
                    if(((User)customResponse.getBody()).isAdmin())
                    {
                        setAdminView();
                    }
                }
            }
        });

        model.getIsLoggedOut().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK) {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.topBarAdmin.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                model.logOut();
                return true;
            }
        });

        model.getAds().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.NO_MORE_DATA) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "That's all the data..", Toast.LENGTH_SHORT).show();
                }
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAds((ArrayList<Ad>) customResponse.getBody());
                }
            }
        });

        getData(page, limit);

        binding.nested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    binding.progressBar.setVisibility(View.VISIBLE);
                    getData(page, limit);
                    }
                }
        });

        model.getLocations().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK)
                {
                    adapterLoc.clear();
                    adapterLoc.addAll(model.getAllCities());
                }
            }
        });

        binding.btnChooseCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.filterChips.setVisibility(View.VISIBLE);
                binding.linearCategories.setVisibility(View.GONE);
            }
        });

        binding.btnOKFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.filterChips.setVisibility(View.GONE);
                binding.linearCategories.setVisibility(View.VISIBLE);
            }
        });

        ArrayList<String> selectedChips = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedChips.add(buttonView.getText().toString());
                }
                else
                {
                    selectedChips.remove(buttonView.getText().toString());
                }
            }
        };

        binding.chipHostess.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipPromoter.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipKitchenSupport.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipInterviewer.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipCollectionOperations.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipWaiter.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipLighterPhysicalJobs.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipHeavierPhysicalJobs.setOnCheckedChangeListener(checkedChangeListener);
        binding.chipHeavierPhysicalJobs.setOnCheckedChangeListener(checkedChangeListener);

        binding.btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
                model.sort();
            }
        });

        binding.btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
                String loc = spinnerLocations.getSelectedItem().toString();
                int id = model.getIdByLocationName(loc);
                model.sortAds(model.getIdByLocationName(loc));
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = spinnerLocations.getSelectedItem().toString();
                int id = model.getIdByLocationName(loc);
                //List<Integer> tagIds = model.selectedTags(selectedChips);
                String min = binding.editTxtFeeFromFilter.getText().toString();
                String max = binding.editTxtFeeToFilter.getText().toString();
                model.filterAds(model.getIdByLocationName(loc), min.equals("") ? null : Integer.parseInt(min),
                        max.equals("") ? null : Integer.parseInt(max), model.selectedTags(selectedChips));
            }
        });

        binding.topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(requireContext(), ChatActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return binding.getRoot();
    }

    private void getData(int page, int limit)
    {
        if (page > limit) {
            Toast.makeText(requireActivity(), "That's all the data..", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        model.loadAdsDefault();
    }

    private void setAdminView()
    {
        if(Utility.getLoggedInUser(App.getAppContext()).isAdmin())
        {
            binding.topBar.setVisibility(View.GONE);
            binding.topBarAdmin.setVisibility(View.VISIBLE);
            binding.linearFee.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAdSelected(Ad ad) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra(JobAdActivity.AD_ID, ad.getAdId());
        startActivity(intent);
    }
}
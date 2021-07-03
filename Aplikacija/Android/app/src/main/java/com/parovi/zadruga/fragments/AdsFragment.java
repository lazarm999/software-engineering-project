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

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.List;

public class AdsFragment extends Fragment implements AdAdapter.AdListListener {

    FragmentAdsFragmentBinding binding;
    FeedViewModel model;
    boolean isFiltered = false;
    boolean isSorted = false;
    Integer min = null;
    Integer max = null;
    List<Integer> tagIds = new ArrayList<>();
    Integer locId = null;

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
                    //adapter.setAds((ArrayList<Ad>) customResponse.getBody());
                }
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.setAds((ArrayList<Ad>) customResponse.getBody());
                }
                binding.progressBar.setVisibility(View.GONE);
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
                if(isFiltered)
                    model.filterAds(locId, min, max, tagIds, true, true);
                else
                    model.sortAds();
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selChips = syncChips(selectedChips);
                List<Integer> currTagIds = new ArrayList<>();
                if(selChips == null) {
                    for (String s : selectedChips
                    ) {
                        currTagIds.add(model.getIdByTagName(s));
                    }
                }
                else
                {
                    for (String s : selChips
                    ) {
                        currTagIds.add(model.getIdByTagName(s));
                    }
                }
                String sMin = binding.editTxtFeeFromFilter.getText().toString();
                String sMax = binding.editTxtFeeToFilter.getText().toString();
                min = sMin.equals("") ? null : Integer.parseInt(sMin);
                max = sMax.equals("") ? null : Integer.parseInt(sMax);
                tagIds = currTagIds;
                String loc = "";
                if(spinnerLocations.getSelectedItem() != null)
                    loc = spinnerLocations.getSelectedItem().toString();
                locId = model.getIdByLocationName(loc);
                isFiltered = true;
                model.filterAds(locId,  min,max, tagIds, false, true);
            }
        });

        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isFiltered)
                    model.loadAdsDefault(true);
                else
                    model.filterAds(locId,  min,max, tagIds, isSorted, true);
                binding.refresh.setRefreshing(false);
            }
        });

        binding.nested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if(!isFiltered)
                        model.loadAdsDefault(false);
                    else
                        model.filterAds(locId,  min,max, tagIds, isSorted, false);
                    binding.progressBar.setVisibility(View.VISIBLE);
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

    @Override
    public void onStart() {
        super.onStart();
        model.loadAdsDefault(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.getAds().removeObservers(requireActivity());
        model.getIsLoggedOut().removeObservers(requireActivity());
        model.getLocations().removeObservers(requireActivity());
        model.getTags().removeObservers(requireActivity());
        model.getUser().removeObservers(requireActivity());
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

    private ArrayList<String> syncChips(ArrayList<String> list)
    {
        ArrayList<String> newList = new ArrayList<>();
            for (String s : list
            ) {
                if (s.equals("Hostess"))
                    newList.add("Hostesa");
                else if (s.equals("Interviewer"))
                    newList.add("Anketar");
                else if (s.equals("Kitchen support stuff"))
                    newList.add("Pomoćno osoblje u kuhinji");
                else if (s.equals("Collection operations"))
                    newList.add("Inkasantski poslovi");
                else if (s.equals("Waiter"))
                    newList.add("Konobar");
                else if (s.equals("Lighter physical jobs"))
                    newList.add("Lakši fizički poslovi");
                else if (s.equals("Heavier physical jobs"))
                    newList.add("Teži fizički poslovi");
                else if (s.equals("Promoter"))
                    newList.add("Promoter");
            }
        return newList;
    }

    @Override
    public void onAdSelected(Ad ad) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra(JobAdActivity.AD_ID, ad.getAdId());
        startActivity(intent);
    }
}
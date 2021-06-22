package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.AdAdapter;
import com.parovi.zadruga.databinding.FragmentAdsFragmentBinding;
import com.parovi.zadruga.items.AdItem;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.ui.JobAdActivity;

import java.time.LocalDate;
import java.util.ArrayList;

public class AdsFragment extends Fragment implements AdAdapter.AdListListener {
    FragmentAdsFragmentBinding binding;
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
        Spinner spinnerLocations = binding.spinnerLocationFilter;
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.locations));
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(adapterLoc);

        ArrayList<AdItem> ads = new ArrayList<>();
        ads.add(new AdItem("Title 1", "Description 1", LocalDate.now()));
        ads.add(new AdItem("Title 2", "Description 2", LocalDate.of(2021, 06, 23)));
        ads.add(new AdItem("Title 3", "Description 3", LocalDate.of(2020,12,04)));
        ads.add(new AdItem("Title 4", "Description 4", LocalDate.of(2021, 10, 29)));
        ads.add(new AdItem("Title 5", "Description 5", LocalDate.of(2020,04,22)));
        ads.add(new AdItem("Title 1", "Description 1", LocalDate.now()));
        ads.add(new AdItem("Title 2", "Description 2", LocalDate.of(2021, 06, 23)));
        ads.add(new AdItem("Title 3", "Description 3", LocalDate.of(2020,12,04)));
        ads.add(new AdItem("Title 4", "Description 4", LocalDate.of(2021, 10, 29)));
        ads.add(new AdItem("Title 5", "Description 5", LocalDate.of(2020,04,22)));

        RecyclerView recView = binding.recViewAds;
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AdAdapter adapter = new AdAdapter(this);
        recView.setAdapter(adapter);

        binding.btnChooseCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.relLayoutChips.setVisibility(View.VISIBLE);
                binding.linearCategories.setVisibility(View.GONE);
            }
        });

        binding.btnOKFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.relLayoutChips.setVisibility(View.GONE);
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
                //sortByLocation(spinnerLocations.getSelectedItem().toString());
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //filterByLocation(spinnerLocations.getSelectedItem().toString());
                //if(!(etFeeFrom.getText().toString().equals("")) && !(etFeeTo.getText().toString().equals("")))
                    //filterByFeeInRange(etFeeFrom.getText(), etFeeTo.getText());
               // selectedChips.forEach(s -> {
                 //   filterByCategory(s);
                //});
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onAdSelected(Ad ad) {
        Intent intent = new Intent(requireActivity(), JobAdActivity.class);
        intent.putExtra("AdID", ad.getAdId());
        startActivity(intent);

    }

   /* private void showToast() {
        LayoutInflater inflaterToast = getLayoutInflater();
        View layoutToast = inflaterToast.inflate(R.layout.toast_layout, (ViewGroup) layout.findViewById(R.id.toast));
        Toast toast = new Toast(getContext());
        TextView txtToast = (TextView) layoutToast.findViewById(R.id.txtToast);
        txtToast.setText("");
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layoutToast);
    }*/

}
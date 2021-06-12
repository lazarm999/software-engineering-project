package com.parovi.zadruga.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.AdAdapter;
import com.parovi.zadruga.items.AdItem;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ads_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdsFragment newInstance(String param1, String param2) {
        AdsFragment fragment = new AdsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_ads_fragment, container, false);
        Spinner spinnerLocations = (Spinner) layout.findViewById(R.id.spinnerLocationFilter);
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

        RecyclerView recView = layout.findViewById(R.id.recViewAds);
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AdAdapter adapter = new AdAdapter((ads));
        recView.setAdapter(adapter);

        Button btnCategory = (Button) layout.findViewById(R.id.btnChooseCateg);
        Button btnOK = (Button) layout.findViewById(R.id.btnOKFilter);
        LinearLayout layoutChips = (LinearLayout) layout.findViewById(R.id.filterChips);
        LinearLayout layoutCategory = (LinearLayout) layout.findViewById(R.id.linearCategories);
        Button btnSort = (Button) layout.findViewById(R.id.btnSort);
        Button btnFilter = (Button) layout.findViewById(R.id.btnFilter);
        EditText etFeeFrom = (EditText) layout.findViewById(R.id.editTxtFeeFromFilter);
        EditText etFeeTo = (EditText) layout.findViewById(R.id.editTxtFeeToFilter);
        Chip chipHostess = (Chip) layout.findViewById(R.id.chipHostess);
        Chip chipPromoter = (Chip) layout.findViewById(R.id.chipPromoter);
        Chip chipKitchenSupport = (Chip) layout.findViewById(R.id.chipKitchenSupport);
        Chip chipInterviewer = (Chip) layout.findViewById(R.id.chipInterviewer);
        Chip chipCollection = (Chip) layout.findViewById(R.id.chipCollectionOperations);
        Chip chipWaiter = (Chip) layout.findViewById(R.id.chipWaiter);
        Chip chipLighterPhysicalJobs = (Chip) layout.findViewById(R.id.chipLighterPhysicalJobs);
        Chip chipHeavierPhysicalJobs = (Chip) layout.findViewById(R.id.chipHeavierPhysicalJobs);

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutChips.setVisibility(View.VISIBLE);
                layoutCategory.setVisibility(View.GONE);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutChips.setVisibility(View.GONE);
                layoutCategory.setVisibility(View.VISIBLE);
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

        chipHostess.setOnCheckedChangeListener(checkedChangeListener);
        chipPromoter.setOnCheckedChangeListener(checkedChangeListener);
        chipKitchenSupport.setOnCheckedChangeListener(checkedChangeListener);
        chipInterviewer.setOnCheckedChangeListener(checkedChangeListener);
        chipCollection.setOnCheckedChangeListener(checkedChangeListener);
        chipWaiter.setOnCheckedChangeListener(checkedChangeListener);
        chipLighterPhysicalJobs.setOnCheckedChangeListener(checkedChangeListener);
        chipHeavierPhysicalJobs.setOnCheckedChangeListener(checkedChangeListener);
        chipHeavierPhysicalJobs.setOnCheckedChangeListener(checkedChangeListener);

        /*btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sortByLocation(spinnerLocations.getSelectedItem().toString());
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
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

        CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas
            }
        });*/
        return layout;
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
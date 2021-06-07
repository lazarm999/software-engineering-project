package com.parovi.zadruga.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.AdAdapter;
import com.parovi.zadruga.items.AdItem;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recommendation_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendationFragment newInstance(String param1, String param2) {
        RecommendationFragment fragment = new RecommendationFragment();
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
        View layout = inflater.inflate(R.layout.fragment_recommendation_fragment, container, false);

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

        RecyclerView recView = layout.findViewById(R.id.recViewRecommends);
        recView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AdAdapter adapter = new AdAdapter((ads));
        recView.setAdapter(adapter);


        /*CardView cardAd = (CardView) layout.findViewById(R.id.cardAdItem);
        cardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pozovi Urosev activity za oglas
            }
        });*/

        return layout;
    }
}
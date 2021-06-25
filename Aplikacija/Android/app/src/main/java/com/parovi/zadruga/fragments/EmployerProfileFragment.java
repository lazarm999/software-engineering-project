package com.parovi.zadruga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parovi.zadruga.R;
import com.parovi.zadruga.activities.GradeUserActivity;
import com.parovi.zadruga.activities.UsersAchievementsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployerProfileFragment newInstance(String param1, String param2) {
        EmployerProfileFragment fragment = new EmployerProfileFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_employer_profile, container, false);

        Button achievements = (Button) layout.findViewById(R.id.btnAchievements);
        Button edit = (Button) layout.findViewById(R.id.btnEdit);
        ImageButton back = (ImageButton) layout.findViewById(R.id.imgBtnArrowBack);
        Button rate = (Button) layout.findViewById(R.id.btnRating);
        TextView fac = (TextView) layout.findViewById(R.id.txtCompany);
        TextView bio = (TextView) layout.findViewById(R.id.txtMultilineEditBio);
        TextView phone = (TextView) layout.findViewById(R.id.txtNumber);
        TextView name = (TextView) layout.findViewById(R.id.editTextUser);
        TextView username = (TextView) layout.findViewById(R.id.editTextUsername);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Urosev ekran za edit korisnika
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GradeUserActivity.class);
                startActivity(intent);
            }
        });

        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UsersAchievementsActivity.class);
                startActivity(intent);
            }
        });

        return layout;
    }
}
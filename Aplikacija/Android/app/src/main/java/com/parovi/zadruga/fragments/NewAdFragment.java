package com.parovi.zadruga.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.parovi.zadruga.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAdFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewAdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAdFragment newInstance(String param1, String param2) {
        NewAdFragment fragment = new NewAdFragment();
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

    EditText etNewAdTitle;
    EditText etNewAdDescription;
    EditText etNewAdFeeFrom;
    EditText etNewAdFeeTo;
    TextView txtSelectedChoices;
    boolean[] selectedItems;
    ArrayList<Integer> categoryList = new ArrayList<>();
    String[] categoryArray = {"Hostess", "Promoter", "Kitchen support staff", "Interviewer", "Collection operations", "Waiter", "Lighter physical jobs", "Heavier physical jobs"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_new_ad, container, false);

        Spinner spinnerLocations = (Spinner) layout.findViewById(R.id.spinnerLocation);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.locations));
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(adapterLoc);

        etNewAdFeeFrom = (EditText) layout.findViewById(R.id.editTxtNewAdFeeFrom);
        etNewAdFeeTo = (EditText) layout.findViewById(R.id.editTxtNewAdFeeTo);
        etNewAdDescription = (EditText) layout.findViewById(R.id.editTxtNewAdDescription);
        etNewAdTitle = (EditText) layout.findViewById(R.id.txtNewAdTitle);
        txtSelectedChoices = (TextView) layout.findViewById(R.id.txtCategoryList);
        selectedItems = new boolean[categoryArray.length];

        Button btnAdd = (Button) layout.findViewById(R.id.btnNewAdAdd);
        Button btnCancel = (Button) layout.findViewById(R.id.btnNewAdCancle);
        Button btnCategory = (Button) layout.findViewById(R.id.btnChooseCateg);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.bottom_nav_employer, new AdsFragment());
                fr.commit();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNewAdTitle.getText().toString().equals("Title") || etNewAdDescription.getText().toString().equals("") || etNewAdFeeFrom.getText().toString().equals("") || etNewAdFeeTo.getText().toString().equals(""))
                {
                    LayoutInflater inflaterToast = getLayoutInflater();
                    View layoutToast = inflaterToast.inflate(R.layout.toast_layout, (ViewGroup) layout.findViewById(R.id.toast));
                    Toast toast = new Toast(getContext());
                    TextView txtToast = (TextView) layoutToast.findViewById(R.id.txtToast);
                    txtToast.setText(R.string.missingData);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layoutToast);
                    toast.show();
                }
                else {
                    //dijalog da li smo sigurni da zelimo da dodamo oglas
                    //createNewAd(...);

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.bottom_nav_employer, new AdsFragment());
                    fr.commit();
                }

            }
        });
                btnCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setTitle(R.string.dialogMultipleChoiceTitle);
                        builder.setCancelable(false);
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

                                txtSelectedChoices.setText(stringBuilder);
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
                                    txtSelectedChoices.setText("");
                                }
                            }
                        });

                        builder.show();
                    }
                });
        return layout;
    }
}
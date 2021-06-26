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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.viewModels.NewAdViewModel;

import java.util.ArrayList;

public class NewAdFragment extends Fragment {

    public NewAdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    EditText etNewAdTitle;
    EditText etNewAdDescription;
    EditText etNewAdFeeFrom;
    EditText etNewAdFeeTo;
    EditText etNewAdPeopleNeeded;
    TextView txtSelectedChoices;
    boolean[] selectedItems;
    ArrayList<Integer> categoryList = new ArrayList<>();
    String[] newArrayCategory = {};
    //String[] categoryArray = {"Hostess", "Promoter", "Kitchen support staff", "Interviewer", "Collection operations", "Waiter", "Lighter physical jobs", "Heavier physical jobs"};
    private NewAdViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_new_ad, container, false);
        Spinner spinnerLocations = (Spinner) layout.findViewById(R.id.spinnerLocation);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(adapterLoc);
        model = new ViewModelProvider(requireActivity()).get(NewAdViewModel.class);

        model.getLocations().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK)
                {
                    adapterLoc.clear();
                    adapterLoc.addAll(model.getAllCities());
                   // makeToast(R.string.successfulLocation);
                }
                else if(customResponse.getStatus() == CustomResponse.Status.BAD_REQUEST)
                {
                    Toast.makeText(requireContext(), "You got the locations", Toast.LENGTH_SHORT).show();
                }
                else if(customResponse.getStatus() == CustomResponse.Status.SERVER_ERROR)
                {
                    Toast.makeText(requireContext(), "Error with server", Toast.LENGTH_SHORT).show();
                }
            }
        });

        model.getTags().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    newArrayCategory = null;
                    newArrayCategory = model.getAllTagNames().toArray(new String[0]);
                    makeToast(R.string.successfulLocation);
                } else if (customResponse.getStatus() == CustomResponse.Status.BAD_REQUEST) {
                    makeToast(R.string.badRequestLocation);
                } else if (customResponse.getStatus() == CustomResponse.Status.SERVER_ERROR) {
                    makeToast(R.string.serverErrorNewAd);
                }
            }
        });

        etNewAdFeeFrom = (EditText) layout.findViewById(R.id.editTxtNewAdFeeFrom);
        etNewAdFeeTo = (EditText) layout.findViewById(R.id.editTxtNewAdFeeTo);
        etNewAdDescription = (EditText) layout.findViewById(R.id.editTxtNewAdDescription);
        etNewAdTitle = (EditText) layout.findViewById(R.id.txtNewAdTitle);
        etNewAdPeopleNeeded = (EditText) layout.findViewById(R.id.editTxtNewAdPeopleNeeded);
        txtSelectedChoices = (TextView) layout.findViewById(R.id.txtCategoryList);
        selectedItems = new boolean[newArrayCategory.length];

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

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle(R.string.dialogMultipleChoiceTitle);
                builder.setCancelable(false);
                builder.setMultiChoiceItems(newArrayCategory, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
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
                            stringBuilder.append(newArrayCategory[categoryList.get(j)]);

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

        model.getIsPosted().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if(customResponse.getStatus() == CustomResponse.Status.OK)
                {
                    //makeToast(R.string.successfulNewAd);
                }
                else if(customResponse.getStatus() == CustomResponse.Status.BAD_REQUEST)
                {
                    makeToast(R.string.badRequestNewAd);
                }
                else if(customResponse.getStatus() == CustomResponse.Status.SERVER_ERROR)
                {
                    makeToast(R.string.serverErrorNewAd);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //dijalog da li smo sigurni da zelimo da dodamo oglas

                    model.postAd(Utility.getAccessToken(container.getContext()), new PostAdRequest(etNewAdTitle.getText().toString(), etNewAdDescription.getText().toString(), Integer.parseInt(etNewAdFeeFrom.getText().toString()), Integer.parseInt(etNewAdFeeTo.getText().toString()), Integer.parseInt(etNewAdPeopleNeeded.getText().toString()), Integer.parseInt(spinnerLocations.getSelectedItem().toString()), categoryList));
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.bottom_nav_employer, new AdsFragment());
                    fr.commit();
            }
        });

        return layout;
    }

    public void makeToast(int string)
    {
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
}
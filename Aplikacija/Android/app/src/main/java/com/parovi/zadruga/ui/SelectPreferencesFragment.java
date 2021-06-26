package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.PreferencesAdapter;
import com.parovi.zadruga.data.UserInfo;
import com.parovi.zadruga.databinding.FragmentSelectPreferencesBinding;
import com.parovi.zadruga.viewModels.UserProfileViewModel;

import java.util.LinkedList;


public class SelectPreferencesFragment extends Fragment {
    private UserProfileViewModel model;
    private FragmentSelectPreferencesBinding binding;

    public SelectPreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectPreferencesBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        PreferencesAdapter adapter = new PreferencesAdapter();
        binding.rvPrefList.setAdapter(adapter);
        model.getUserInfo().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                adapter.setPreferences(new LinkedList<Long>());
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(container.getContext(), 2);
        binding.rvPrefList.setLayoutManager(layoutManager);

        return binding.getRoot();
    }
}
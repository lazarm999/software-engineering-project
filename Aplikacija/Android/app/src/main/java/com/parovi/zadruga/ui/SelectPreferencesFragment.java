package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.PreferencesAdapter;
import com.parovi.zadruga.databinding.FragmentSelectPreferencesBinding;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.viewModels.EditProfileViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SelectPreferencesFragment extends Fragment implements PreferencesAdapter.PreferencesListener {
    private EditProfileViewModel model;
    private FragmentSelectPreferencesBinding binding;
    private boolean tagsArrived = false;

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
        model = new ViewModelProvider(requireActivity()).get(EditProfileViewModel.class);
        PreferencesAdapter adapter = new PreferencesAdapter(this);
        adapter.setSelectedTagIds(model.getSelectedTags());
        binding.rvPrefList.setAdapter(adapter);
        model.getTags().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK && !tagsArrived) {
                    adapter.setTags((List<Tag>) customResponse.getBody());
                    tagsArrived = true;
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(container.getContext(), 2);
        binding.rvPrefList.setLayoutManager(layoutManager);

        model.loadTags();
        return binding.getRoot();
    }

    @Override
    public void onTagToggled(Tag tag, boolean isChecked) {
        int tagId = tag.getTagId();
        if (isChecked && !model.getSelectedTags().contains(tagId)) {
            model.getSelectedTags().add(tag.getTagId());
        }
        else if (!isChecked && model.getSelectedTags().contains(tag.getTagId())){
            model.getSelectedTags().remove((Integer)tag.getTagId());
        }
    }
}
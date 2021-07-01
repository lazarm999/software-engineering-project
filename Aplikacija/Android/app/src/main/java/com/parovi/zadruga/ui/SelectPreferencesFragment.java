package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.PreferencesAdapter;
import com.parovi.zadruga.databinding.FragmentSelectPreferencesBinding;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.viewModels.AdViewModel;
import com.parovi.zadruga.viewModels.EditProfileViewModel;

import java.util.List;


public class SelectPreferencesFragment extends Fragment implements PreferencesAdapter.PreferencesListener {
    private TagsTracker model;
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
        if (getArguments() != null) {
            if (SelectPreferencesFragmentArgs.fromBundle(getArguments()).getForAd())
                model = new ViewModelProvider(requireActivity()).get(AdViewModel.class);
            else
                model = new ViewModelProvider(requireActivity()).get(EditProfileViewModel.class);
        }
        else
            model = new ViewModelProvider(requireActivity()).get(EditProfileViewModel.class);
        binding = FragmentSelectPreferencesBinding.inflate(inflater, container, false);
        PreferencesAdapter adapter = new PreferencesAdapter(this);
        adapter.setSelectedTagIds(model.getNewSelectedTags());
        binding.rvPrefList.setAdapter(adapter);
        model.getTags().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK && !tagsArrived) {
                    List<Tag> tags = (List<Tag>) customResponse.getBody();
                    adapter.setTags(tags);
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
        if (isChecked && !model.getNewSelectedTags().contains(tagId))
            model.getNewSelectedTags().add(tagId);
        else if (!isChecked && model.getNewSelectedTags().contains(tagId))
            model.getNewSelectedTags().remove(Integer.valueOf(tagId));
    }

    public interface TagsTracker {
        List<Integer> getNewSelectedTags();
        List<Integer> getCurrentlySelectedTags();
        void loadTags();
        MutableLiveData<CustomResponse<?>> getTags();
    }
}
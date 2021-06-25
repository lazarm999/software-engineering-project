package com.parovi.zadruga.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.activities.MainStudentActivity;
import com.parovi.zadruga.adapters.UserAdapter;
import com.parovi.zadruga.adapters.UserResumesAdapter;
import com.parovi.zadruga.databinding.FragmentChatInfoBinding;
import com.parovi.zadruga.fragments.EmployerProfileFragment;
import com.parovi.zadruga.fragments.StudentProfileFragment;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.viewModels.ChatViewModel;

import java.util.List;


public class ChatInfoFragment extends Fragment implements UserAdapter.UserListListener {
    private ChatViewModel model;
    private FragmentChatInfoBinding binding;

    public ChatInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatInfoBinding.inflate(inflater, container, false);

        model = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        UserAdapter adapter = new UserAdapter(this);
        //UserResumesAdapter adapter = new UserResumesAdapter();
        model.observeMembers().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.submitList((List<User>)customResponse.getBody());
                }
            }
        });
        model.observeAd().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    binding.tvRelatedJobTitle.setText(((Ad)customResponse.getBody()).getTitle());
                }
            }
        });
        binding.rvParticipants.setAdapter(adapter);
        binding.rvParticipants.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //NavController navController = Navigation.findNavController(requireActivity(), R.id.chat_nav_host_fragment);

        binding.tvRelatedJobTitle.setText(model.getActiveChat().getChatTitle());
        if (model.getActiveChat().getType() == Utility.ChatType.GROUP) {
            binding.tvRelatedJobTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireActivity(), JobAdActivity.class);
                    intent.putExtra("adID", model.getAdId());
                    startActivity(intent);
                }
            });
        }
        return binding.getRoot();
    }

    @Override
    public void onUserSelected(User user) {
        Intent intent = new Intent(requireActivity(), MainStudentActivity.class);
        intent.putExtra("userID", user.getUserId());
        startActivity(intent);
    }
}
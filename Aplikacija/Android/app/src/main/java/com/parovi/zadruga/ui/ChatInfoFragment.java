package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.ChatResumesAdapter;
import com.parovi.zadruga.adapters.UserResumesAdapter;
import com.parovi.zadruga.data.Chat;
import com.parovi.zadruga.data.ChatResume;
import com.parovi.zadruga.databinding.FragmentChatInfoBinding;
import com.parovi.zadruga.databinding.FragmentChatListBinding;
import com.parovi.zadruga.viewModels.ChatsViewModel;

import java.util.List;


public class ChatInfoFragment extends Fragment {
    private ChatsViewModel model;
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

        model = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        UserResumesAdapter adapter = new UserResumesAdapter();
        model.getChatInFocus().observe(requireActivity(), new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                adapter.setUsers(chat.getParticipants());
                binding.tvRelatedJobTitle.setText(chat.getChatTitle());
            }
        });
        binding.rvParticipants.setAdapter(adapter);
        binding.rvParticipants.setLayoutManager(new LinearLayoutManager(container.getContext()));
        NavController navController = Navigation.findNavController(requireActivity(), R.id.chat_nav_host_fragment);
        return binding.getRoot();
    }
}
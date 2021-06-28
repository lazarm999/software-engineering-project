package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.adapters.ChatsAdapter;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.databinding.FragmentChatListBinding;
import com.parovi.zadruga.viewModels.ChatViewModel;

import java.util.List;


public class ChatListFragment extends Fragment implements ChatsAdapter.ChatListListener {
    private ChatViewModel model;
    private FragmentChatListBinding binding;

    public ChatListFragment() {
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
        binding = FragmentChatListBinding.inflate(inflater, container, false);

        model = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        ChatsAdapter adapter = new ChatsAdapter(this);
        // ChatResumesAdapter adapter = new ChatResumesAdapter(this);
        model.observeChats().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    adapter.submitList((List<Chat>) customResponse.getBody());
                    //adapter.notifyDataSetChanged();
                }
            }
        });
        binding.rvChatList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        binding.rvChatList.setLayoutManager(layoutManager);
        return binding.getRoot();
    }

    @Override
    public void onChatResumeSelected(Chat chat) {
        model.setActiveChat(chat);
        model.setActiveQbChat(chat.getQbChat());
        Navigation.findNavController(binding.getRoot()).navigate(ChatListFragmentDirections.actionChatListFragmentToChatMessagesFragment());
    }
}
package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.ChatResumesAdapter;
import com.parovi.zadruga.data.ChatResume;
import com.parovi.zadruga.databinding.FragmentChatListBinding;
import com.parovi.zadruga.viewModels.ChatsViewModel;

import java.util.List;


public class ChatListFragment extends Fragment implements ChatResumesAdapter.ChatListListener {
    private ChatsViewModel model;
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

        model = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        ChatResumesAdapter adapter = new ChatResumesAdapter(this);
        model.getChatList().observe(requireActivity(), new Observer<List<ChatResume>>() {
            @Override
            public void onChanged(List<ChatResume> chatResumes) {
                adapter.setChats(chatResumes);
            }
        });
        binding.rvChatList.setAdapter(adapter);
        binding.rvChatList.setLayoutManager(new LinearLayoutManager(container.getContext()));

        return binding.getRoot();
    }

    @Override
    public void onChatResumeSelected(long id) {
        Toast.makeText(getContext(), "Chat with id = " + Long.toString(id), Toast.LENGTH_LONG).show();
        Navigation.findNavController(binding.getRoot()).navigate(ChatListFragmentDirections.actionChatListFragmentToChatMessagesFragment());
    }
}
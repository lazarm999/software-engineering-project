package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.adapters.MessagesAdapter;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.databinding.FragmentChatMessagesBinding;
import com.parovi.zadruga.viewModels.ChatViewModel;
import com.parovi.zadruga.viewModels.ChatsViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatMessagesFragment extends Fragment {
    private ChatViewModel model;
    private FragmentChatMessagesBinding binding;
    public ChatMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatMessagesBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        MessagesAdapter adapter = new MessagesAdapter();
        model.getMessages().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK && customResponse.getBody() != null)
                    adapter.setMessages((List<Message>)customResponse.getBody());
            }
        });
        model.observeIsConnected().observe(requireActivity(), new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                ;
            }
        });
        binding.rvMessages.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        binding.rvMessages.setLayoutManager(layoutManager);

        binding.imgbtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.sendMessage(binding.etmlNewMessage.getText().toString());
                binding.etmlNewMessage.setText("");
            }
        });
        binding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(requireActivity(), R.id.chat_nav_host_fragment));
            }
        });

        model.loadMessages(0);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        model.clearMessages();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_overflow_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chatInfoFragment:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(requireActivity(), R.id.chat_nav_host_fragment));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
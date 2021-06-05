package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import com.parovi.zadruga.R;
import com.parovi.zadruga.adapters.MessagesAdapter;
import com.parovi.zadruga.data.Chat;
import com.parovi.zadruga.data.Message;
import com.parovi.zadruga.databinding.FragmentChatMessagesBinding;
import com.parovi.zadruga.viewModels.ChatsViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatMessagesFragment extends Fragment {
    private ChatsViewModel model;
    private FragmentChatMessagesBinding binding;
    public ChatMessagesFragment() {
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
        binding = FragmentChatMessagesBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        MessagesAdapter adapter = new MessagesAdapter();
        model.getChatInFocus().observe(requireActivity(), new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                adapter.setMessages(chat.getMessages());
            }
        });
        binding.rvMessages.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        binding.rvMessages.setLayoutManager(layoutManager);

        binding.imgbtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etmlNewMessage.setText("");
                Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
            }
        });

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_overflow_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(requireActivity(), R.id.chat_nav_host_fragment))
                || super.onOptionsItemSelected(item);
    }
}
package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityChatBinding;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.repository.ChatRepository;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;

public class ChatActivity extends AppCompatActivity{
    public static final String CHAT_QB_ID = "chatQbId";

    private ChatViewModel model;
    private ActivityChatBinding binding;
    MutableLiveData<CustomResponse<?>> isLoggedIn = new MutableLiveData<>();
    private UserRepository userRepository = new UserRepository();

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.chat_nav_host_fragment);
        return navController.navigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(ChatViewModel.class);

        model.connectToChatServer();
        model.observeIsConnected().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    model.addOnGlobalMessageReceived();
                    receiveIntent();
                }
            }
        });

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_nav_host_fragment, ChatMessagesFragment.class, null)
                .addToBackStack(null).commit();/*
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Chats");

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_container, ChatListFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }*/
    }

    private void receiveIntent() {
        String qbChatId = getIntent().getStringExtra(CHAT_QB_ID);
        if (qbChatId != null) {
            MutableLiveData<CustomResponse<?>> chatToOpen = new MutableLiveData<>();
            chatToOpen.observe(this, new Observer<CustomResponse<?>>() {
                @Override
                public void onChanged(CustomResponse<?> customResponse) {
                    if (customResponse.getStatus() == CustomResponse.Status.OK) {
                        model.setActiveChat((Chat)customResponse.getBody());
                        Navigation.findNavController(binding.chatNavHostFragment).navigate(ChatListFragmentDirections.actionChatListFragmentToChatMessagesFragment());
                    }
                }
            });
            new ChatRepository().getChatById(chatToOpen, qbChatId);
        }
        model.loadAllChats();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //NavController navController = Navigation.findNavController(this, R.id.chat_nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController);
    }
}
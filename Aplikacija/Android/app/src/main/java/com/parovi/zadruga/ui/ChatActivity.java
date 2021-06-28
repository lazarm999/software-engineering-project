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
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;

public class ChatActivity extends AppCompatActivity{
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

        userRepository.loginUser(isLoggedIn, "vuk.bibic@gmail.com", "novaaasifraaaa");
        isLoggedIn.observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    model.connectToChatServer();
                }
            }
        });
        model.observeIsConnected().observe(this, new Observer<CustomResponse<?>>() {
            @Override
            public void onChanged(CustomResponse<?> customResponse) {
                if (customResponse.getStatus() == CustomResponse.Status.OK) {
                    model.addOnGlobalMessageReceived();
                    model.loadAllChats();
                }
            }
        });

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Chats");

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_container, ChatListFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this, R.id.chat_nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController);
    }
}
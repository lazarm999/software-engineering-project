package com.parovi.zadruga.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityChatBinding;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;
import com.parovi.zadruga.viewModels.ChatViewModel;
import com.parovi.zadruga.viewModels.ChatsViewModel;

import java.util.zip.Inflater;

public class ChatActivity extends AppCompatActivity{
    private ChatViewModel model;
    private ActivityChatBinding binding;
    MutableLiveData<CustomResponse<?>> isLoggedIn = new MutableLiveData<>();

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

        ZadrugaRepository.getInstance(this.getApplication()).loginUser(isLoggedIn, new User("vuk.bibic@gmail.com", "novaaasifraaaa"));
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
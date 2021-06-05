package com.parovi.zadruga.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.parovi.zadruga.R;
import com.parovi.zadruga.databinding.ActivityChatBinding;
import com.parovi.zadruga.viewModels.ChatsViewModel;

import java.util.zip.Inflater;

public class ChatActivity extends AppCompatActivity{
    private ChatsViewModel model;
    private ActivityChatBinding binding;

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

        model = new ViewModelProvider(this).get(ChatsViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chats");

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
        NavigationUI.setupActionBarWithNavController(this, navController);
    }
}
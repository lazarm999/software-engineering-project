package com.parovi.zadruga.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.parovi.zadruga.R;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.UserProfileViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private UserProfileViewModel model;
    private UserRepository userRepository = new UserRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository.loginUser(new MutableLiveData<>(), "tea@gmail.com", "sifra123");
        setContentView(R.layout.activity_edit_profile);
        model = new ViewModelProvider(this).get(UserProfileViewModel.class);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //NavController navController = Navigation.findNavController(this, R.id.edit_profile_nav_host_frag);
        //NavigationUI.setupActionBarWithNavController(this, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.edit_profile_nav_host_frag);
        return navController.navigateUp();
    }
}
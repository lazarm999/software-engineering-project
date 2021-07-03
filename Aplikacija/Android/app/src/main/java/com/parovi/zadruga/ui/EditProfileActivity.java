package com.parovi.zadruga.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.parovi.zadruga.R;
import com.parovi.zadruga.repository.UserRepository;
import com.parovi.zadruga.viewModels.EditProfileViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private EditProfileViewModel model;
    private UserRepository userRepository = new UserRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        model = new ViewModelProvider(this).get(EditProfileViewModel.class);
    }
    @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this, R.id.edit_profile_nav_host_frag);
        //NavigationUI.setupActionBarWithNavController(this, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.edit_profile_nav_host_frag);
        return navController.navigateUp();
    }
}
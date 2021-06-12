package com.parovi.zadruga.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.parovi.zadruga.R;

public class NewAdActivityV2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad_v2);

        Spinner spinnerLocations = (Spinner) findViewById(R.id.spinnerLocation);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(NewAdActivityV2.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.locations));
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(adapterLoc);
    }
}
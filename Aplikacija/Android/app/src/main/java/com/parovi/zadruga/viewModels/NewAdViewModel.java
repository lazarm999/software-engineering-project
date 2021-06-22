package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.repository.ZadrugaRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewAdViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isPosted, locations, tags;
    private ZadrugaRepository rep;
    private ArrayList<Location> locs;
    private ArrayList<Tag> tag;

    public NewAdViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<CustomResponse<?>> getIsPosted(){

        if(isPosted == null)
            isPosted = new MutableLiveData<CustomResponse<?>>();
        return isPosted;
    }

    public MutableLiveData<CustomResponse<?>> getLocations()
    {
        if(locations == null)
            locations = new MutableLiveData<CustomResponse<?>>();

        return locations;
    }

    public MutableLiveData<CustomResponse<?>> getTags(){
        return tags;
    }

    public void postAd(String token, PostAdRequest ad){
        rep.postAd(token, isPosted, ad);
    }

    public ArrayList<String> getCities(String token)
    {
        rep.getAllLocations(token, locations);

        ArrayList<String> cities = new ArrayList<>();

        for (Location lok : locs
             ) {

            cities.add(lok.getCityName());
        }
        return cities;
    }
}

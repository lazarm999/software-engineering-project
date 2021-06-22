package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.repository.ZadrugaRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewAdViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isPosted, locations, tags;
    private ZadrugaRepository rep;
    private ArrayList<Location> locs;
    //private ArrayList<Tag> tag;

    public NewAdViewModel(@NonNull @NotNull Application application) {
        super(application);
        rep = ZadrugaRepository.getInstance(application);
        isPosted = new MutableLiveData<>();
        locations = new MutableLiveData<>();
        //tag = new MutableLiveData<>();
        rep.getAllLocations(Utility.getAccessToken(App.getAppContext()), locations);
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

    public void getCities(String token)
    {
        rep.getAllLocations(token, locations);



    }

    public List<String> getAllCities()
    {
        List<Location> locs = (List<Location>)locations.getValue().getBody();
        List<String> strings = new ArrayList<String >();
        if (locs == null)
            return strings;
        for (Location loc : locs)
            strings.add(loc.getCityName());
        return strings;
    }
}

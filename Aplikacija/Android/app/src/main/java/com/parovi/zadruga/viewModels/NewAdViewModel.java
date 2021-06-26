package com.parovi.zadruga.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.requestModels.PostAdRequest;
import com.parovi.zadruga.repository.AdRepository;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewAdViewModel extends AndroidViewModel {
    private MutableLiveData<CustomResponse<?>> isPosted, locations, tags;
    private LookUpRepository lookUpRepository;
    private AdRepository adRepository;
    private ArrayList<Location> locs;
    private ArrayList<Tag> tag;

    public NewAdViewModel(@NonNull @NotNull Application application) {
        super(application);
        lookUpRepository = new LookUpRepository();
        adRepository = new AdRepository();
        isPosted = new MutableLiveData<>();
        locations = new MutableLiveData<>();
        //tag = new MutableLiveData<>();
        lookUpRepository.getAllLocations(Utility.getAccessToken(App.getAppContext()), locations);
        tags = new MutableLiveData<>();
        lookUpRepository.getAllLocations(Utility.getAccessToken(App.getAppContext()), locations);
        lookUpRepository.getAllTags(Utility.getAccessToken(App.getAppContext()), tags);
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
        if(tags == null)
            tags = new MutableLiveData<CustomResponse<?>>();

        return tags;
    }

    public void postAd(String token, PostAdRequest ad){
        adRepository.postAd(token, isPosted, ad);
    }

    public void getCities(String token)
    {
        lookUpRepository.getAllLocations(token, locations);
        lookUpRepository.getAllLocations(token, locations);
    }

    public void getTagNames(String token)
    {
        lookUpRepository.getAllTags(token, tags);
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

    public List<String> getAllTagNames()
    {
        List<Tag> tagNames = (List<Tag>)tags.getValue().getBody();
        List<String> strings = new ArrayList<String>();
        if (tagNames == null)
            return strings;
        for (Tag tag : tagNames)
            strings.add(tag.getName());
        return strings;
    }
}

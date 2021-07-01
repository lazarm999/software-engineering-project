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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewAdViewModel extends AndroidViewModel {
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.Gg7A5swYP1yf3_lPg4OyvMUYv6VNKYtl0L2r8WAhfqA";

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
        lookUpRepository.getAllLocations(Utility.getAccessToken(App.getAppContext()), locations);
        tags = new MutableLiveData<>();
        lookUpRepository.getAllLocations(token, locations);
        lookUpRepository.getAllTags(token, tags);
    }

    public MutableLiveData<CustomResponse<?>> getIsPosted(){
        return isPosted;
    }

    public MutableLiveData<CustomResponse<?>> getLocations()
    {
        return locations;
    }

    public MutableLiveData<CustomResponse<?>> getTags(){
        return tags;
    }

    public void postAd(String token, PostAdRequest ad){
        adRepository.postAd(token, isPosted, ad);
    }

    public void getCities(String token)
    {
        lookUpRepository.getAllLocations(token, locations);
    }

    public void getTagNames(String token)
    {
        lookUpRepository.getAllTags(token, tags);
    }

    public List<Location> getAllCities()
    {
        List<Location> locs = (List<Location>)locations.getValue().getBody();
        return locs;
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

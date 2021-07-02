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
import com.parovi.zadruga.repository.AdRepository;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends AndroidViewModel{
    MutableLiveData<CustomResponse<?>> ads;//All ads
    MutableLiveData<CustomResponse<?>> locations;//locations
    MutableLiveData<CustomResponse<?>> tags;//tags
    MutableLiveData<CustomResponse<?>> user;
    MutableLiveData<CustomResponse<?>> isLoggedOut;

    AdRepository adRepository;
    LookUpRepository lookUpRepository;
    UserRepository userRepository;

    public FeedViewModel(@NonNull Application application) {
        super(application);
        adRepository = new AdRepository();
        lookUpRepository = new LookUpRepository();
        userRepository = new UserRepository();
        ads = new MutableLiveData<>();
        user = new MutableLiveData<>();
        locations = new MutableLiveData<>();
        tags = new MutableLiveData<>();
        isLoggedOut = new MutableLiveData<>();

        lookUpRepository.getAllLocations(Utility.getAccessToken(App.getAppContext()), locations);
        lookUpRepository.getAllTags(Utility.getAccessToken(App.getAppContext()), tags);

        loadAdsDefault(true);
        loadUserInfo();
    }

    public MutableLiveData<CustomResponse<?>> getAds() {  return ads; }

    public MutableLiveData<CustomResponse<?>> getUser() { return user; }

    public MutableLiveData<CustomResponse<?>> getIsLoggedOut() {
        return isLoggedOut;
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

    public void loadAdsDefault(boolean isRefresh) { adRepository.getAds(ads, isRefresh); }

    public void loadRecommended() { adRepository.getRecommendedAds(ads, false); }

    public void loadUserInfo() { userRepository.getUserById(Utility.getAccessToken(App.getAppContext()), user, Utility.getLoggedInUser(App.getAppContext()).getUserId()); }

    public void sortAds() { adRepository.getAds(Utility.getAccessToken(App.getAppContext()), ads,
            null, null, null, null, true, true);}

    public void filterAds(Integer locId, Integer min, Integer max, List<Integer> tagIds, boolean sortByLoc, boolean isRefresh) {
        adRepository.getAds(Utility.getAccessToken(App.getAppContext()), ads, locId, min,
                max, tagIds, sortByLoc, isRefresh);
    }

    private void loadLocations() { lookUpRepository.getAllLocations(Utility.getAccessToken(App.getAppContext()),locations); }

    private void loadTags()  {lookUpRepository.getAllTags(Utility.getAccessToken(App.getAppContext()), tags); }

    public void logOut() { userRepository.logOutUser(isLoggedOut, false); }

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

    public int getIdByLocationName(String locName)
    {
        int id = -1;

        List<Location> locs = (List<Location>)locations.getValue().getBody();
        for (Location loc : locs)
            if(loc.getCityName().equals(locName))
                id = loc.getLocId();
        return id;
    }

    public Integer getIdByTagName(String locName)
    {
        int id = -1;

        List<Tag> tagNames = (List<Tag>)tags.getValue().getBody();
        for (Tag tag : tagNames)
            if(tag.getName().equals(locName))
                id = tag.getTagId();
        return id;
    }

    private Tag getTagByName(String str)
    {
        List<Tag> tagNames = (List<Tag>)tags.getValue().getBody();
        for(Tag tag : tagNames)
        {
            if(tag.getName().equals(str))
                return tag;
        }
        return null;
    }

    public List<Integer> selectedTags(ArrayList<String> chosen)
     {
        List<Integer> tagNames = new ArrayList<>();
        Tag tag;
        for(String str : chosen)
        {
            if((tag = this.getTagByName(str)) != null)
                tagNames.add(tag.getTagId());
        }

        return tagNames;
    }

}

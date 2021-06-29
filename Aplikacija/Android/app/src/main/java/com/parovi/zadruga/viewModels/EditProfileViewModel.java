package com.parovi.zadruga.viewModels;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.CpuUsageInfo;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.LookUpRepository;
import com.parovi.zadruga.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfileViewModel extends AndroidViewModel {
    private final int USER_ID = Utility.getLoggedInUser(App.getAppContext()).getUserId();
    private final String TOKEN = Utility.getAccessToken(App.getAppContext()); // "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g";

    private MutableLiveData<CustomResponse<?>> userInfo;
    private MutableLiveData<CustomResponse<?>> profileImage;
    private MutableLiveData<CustomResponse<?>> isProfileImageUpdated;
    private MutableLiveData<CustomResponse<?>> isUserUpdated;
    private MutableLiveData<CustomResponse<?>> faculties;
    private MutableLiveData<CustomResponse<?>> tags;
    private UserRepository userRepository;
    private LookUpRepository lookUpRepository;

    private List<Integer> selectedTags;

    public EditProfileViewModel(@NonNull Application app) {
        super(app);
        userRepository = new UserRepository();
        lookUpRepository = new LookUpRepository();
        selectedTags = new ArrayList<Integer>();
        userInfo = new MutableLiveData<>();
        profileImage = new MutableLiveData<>();
        isProfileImageUpdated = new MutableLiveData<>();
        isUserUpdated = new MutableLiveData<>();
        faculties = new MutableLiveData<>();
        tags = new MutableLiveData<>();
        loadUserInfo();
        loadUserProfileImage();
    }

    public List<Integer> getSelectedTags() {
        return selectedTags;
    }

    public MutableLiveData<CustomResponse<?>> getIsProfileImageUpdated() {
        return isProfileImageUpdated;
    }

    public MutableLiveData<CustomResponse<?>> getTags() {
        return tags;
    }

    public MutableLiveData<CustomResponse<?>> getIsUserUpdated() {
        return isUserUpdated;
    }

    public MutableLiveData<CustomResponse<?>> getProfileImage() {
        return profileImage;
    }

    public MutableLiveData<CustomResponse<?>> getFaculties() { return faculties; }

    public int getId() {
        return USER_ID;
    }

    public String getToken() {
        return TOKEN;
    }

    public MutableLiveData<CustomResponse<?>> getUserInfo() {
        return userInfo;
    }

    public void updateUser() {
        userRepository.updateUser(Utility.getAccessToken(App.getAppContext()), isUserUpdated, (User)userInfo.getValue().getBody(), null, null);
    }
    public void updateUserProfilePhoto(Resources res, Uri uri, int reqWidth, int reqHeight) {
        userRepository.postProfilePicture(
                profileImage,
                uri);
    }

    public void loadUserInfo() {
        userRepository.getUserById(TOKEN, userInfo, USER_ID);
    }
    public void loadUserProfileImage() {
        userRepository.getProfilePicture(profileImage, USER_ID);
    }
    public void loadFaculties() {
        lookUpRepository.getAllFaculties(TOKEN, faculties);
    }
    public void loadTags() {
        lookUpRepository.getAllTags(TOKEN, tags);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight, Resources res) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String path = uri.toString();
        try {
            return App.getAppContext().getContentResolver().loadThumbnail(uri, new Size(reqWidth,reqHeight),null);
        }
        catch (IOException e) {
            Log.d("Exception", "decodeSampledBitmapFromUri:");
            return null;
        }
        /*BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, R.drawable.avatar, options);*/
    }
}

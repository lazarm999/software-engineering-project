package com.parovi.zadruga.viewModels;

import android.app.Application;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.R;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.repository.ZadrugaRepository;

import java.io.File;
import java.io.IOException;

public class UserProfileViewModel extends AndroidViewModel {
    private final int id = 3;
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6M30.-DAg63c0vAJaWZBypL9axfrQ2p2eO8ihM84Mdi4pt4g";
    private MutableLiveData<CustomResponse<?>> userInfo;
    private MutableLiveData<CustomResponse<?>> profileImage;
    private MutableLiveData<CustomResponse<?>> isProfileImageUpdated;
    private MutableLiveData<CustomResponse<?>> isUserUpdated;
    private ZadrugaRepository repository;

    public UserProfileViewModel(@NonNull Application app) {
        super(app);
        repository = ZadrugaRepository.getInstance(app);
        userInfo = new MutableLiveData<>();
        profileImage = new MutableLiveData<>();
        isProfileImageUpdated = new MutableLiveData<>();
        isUserUpdated = new MutableLiveData<>();
        loadUserInfo();
        loadUserProfileImage();
    }

    public MutableLiveData<CustomResponse<?>> getIsProfileImageUpdated() {
        return isProfileImageUpdated;
    }

    public MutableLiveData<CustomResponse<?>> getIsUserUpdated() {
        return isUserUpdated;
    }

    public MutableLiveData<CustomResponse<?>> getProfileImage() {
        return profileImage;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public MutableLiveData<CustomResponse<?>> getUserInfo() {
        return userInfo;
    }

    public void updateUser() {
        repository.updateUser(token, isUserUpdated, (User)userInfo.getValue().getBody());
    }
    public void updateUserProfilePhoto(Resources res, Uri uri, int reqWidth, int reqHeight) {
        repository.postProfilePicture(token,
                isProfileImageUpdated,
                uri,
                decodeSampledBitmapFromUri(uri, reqWidth, reqHeight, res));
    }

    private void loadUserInfo() {
        repository.getUserById(token, userInfo, id);
    }
    private void loadUserProfileImage() {
        repository.getProfilePicture(profileImage, id);
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

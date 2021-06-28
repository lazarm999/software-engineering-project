package com.parovi.zadruga.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.parovi.zadruga.App;
import com.parovi.zadruga.Constants;
import com.parovi.zadruga.CustomResponse;
import com.parovi.zadruga.Utility;
import com.parovi.zadruga.factories.ApiFactory;
import com.parovi.zadruga.factories.DaoFactory;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.responseModels.CommentResponse;
import com.parovi.zadruga.models.responseModels.RatingResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class NotificationRepository extends BaseRepository {

    public NotificationRepository(){
        super();
    }

    private String getNotificationType(Notification notif){
        if(notif.getAd() != null && notif.getAccepted()  != null && notif.getAccepted()) return Constants.NOTIF_ACCEPTED;
        if(notif.getAd() != null && notif.getAccepted()  != null && !notif.getAccepted()) return Constants.NOTIF_DECLINED;
        if(notif.getComment() != null && notif.getTagged() )return Constants.NOTIF_AD_COMMENT;
        if(notif.getComment() != null && !notif.getTagged()) return Constants.NOTIF_TAGGED;
        if(notif.getRating() != null) return Constants.NOTIF_RATING;
        return "";
    }

    public void getNotifications(MutableLiveData<CustomResponse<?>> notifications){
        String token = Utility.getAccessToken(App.getAppContext());
        Boolean[] isSynced = {false};
        int pageSkip = getListSize(notifications);
        getNotificationsLocal(notifications, isSynced, pageSkip);
        Utility.getExecutorService().execute(() -> {
            try {
                Response<List<Notification>> notifResponse = ApiFactory.getNotificationApi().getNotifications(token, Constants.pageSize, pageSkip).execute();
                if(notifResponse.isSuccessful()){
                    if(notifResponse.body() != null){
                        for (Notification notif : notifResponse.body()) {
                            notif.setType(getNotificationType(notif));
                        }
                        List<Notification> tmpNotificationList;
                        if(notifications.getValue() != null && notifications.getValue().getBody() != null)
                            tmpNotificationList = (List<Notification>) notifications.getValue().getBody();
                        else
                            tmpNotificationList = new ArrayList<>();
                        tmpNotificationList.addAll(notifResponse.body());
                        synchronized (isSynced[0]){
                            notifications.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpNotificationList));
                            isSynced[0] = true;
                        }
                        saveNotificationsLocally(notifResponse.body());
                    }
                } else
                    responseNotSuccessful(notifResponse.code(), notifications);
                Log.i("ne", "run: ");
            } catch (IOException e) {
                e.printStackTrace();
                apiCallOnFailure(e.getMessage(), notifications);
            }
        });
    }

    private void saveNotificationsLocally(List<Notification> notifications) {
        for (Notification notification: notifications) {
            saveRatingLocally(notification.getRating());
            if(notification.getAd() != null){
                saveAdLocally(notification.getAd());
                notification.setFkAdId(notification.getAd().getAdId());
            }
            if(notification.getComment() != null){
                saveCommentLocally(notification.getComment());
                notification.setFkCommentId(notification.getComment().getId());
            }
            if(notification.getRating() != null){
                saveRatingLocally(notification.getRating());
                notification.setFkRatingId(notification.getRating().getRating());
            }
            DaoFactory.getNotificationDao().insertOrUpdate(notification);
        }
    }

    public void getNotificationsLocal(MutableLiveData<CustomResponse<?>> notifications, Boolean[] isSynced, int pageSkip) {
        Utility.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                List<Notification> localNotifications = DaoFactory.getNotificationDao().getNotifications(Constants.pageSize, pageSkip);
                if(localNotifications != null){
                    for (Notification notification : localNotifications) {
                        if(notification.getFkAdId() != null){
                            Ad ad = DaoFactory.getAdDao().getAdById(notification.getFkAdId());
                            notification.setAd(ad);
                        }
                        if(notification.getFkCommentId() != null){
                            Comment comment = DaoFactory.getCommentDao().getCommentById(notification.getFkCommentId());
                            User u = DaoFactory.getUserDao().getUserById(comment.getFkUserId());
                            CommentResponse commentResponse = commentToCommentResponse(comment);
                            commentResponse.setUser(u);
                            notification.setComment(commentResponse);
                        }
                        if(notification.getFkRatingId() != null){
                            Rating rating = DaoFactory.getRatingDao().getRatingById(notification.getFkRatingId());
                            User rater = DaoFactory.getUserDao().getUserById(rating.getFkRaterId());
                            RatingResponse ratingResponse = ratingToRatingResponse(rating);
                            ratingResponse.setRater(rater);
                            notification.setRating(ratingResponse);
                        }
                        notification.setType(getNotificationType(notification));
                    }
                    List<Notification> tmpNotificationList;
                    if(notifications.getValue() != null && notifications.getValue().getBody() != null)
                        tmpNotificationList = (List<Notification>) notifications.getValue().getBody();
                    else
                        tmpNotificationList = new ArrayList<>();
                    tmpNotificationList.addAll(localNotifications);
                    synchronized (isSynced[0]){
                        if(!isSynced[0])
                            notifications.postValue(new CustomResponse<>(CustomResponse.Status.OK, tmpNotificationList));
                    }
                }
            }
        });
    }

    public CommentResponse commentToCommentResponse(Comment comment){
        if(comment == null) return null;
        return new CommentResponse(comment.getCommentId(), comment.getFkAdId(), comment.getComment(), comment.getPostTime());
    }

    public RatingResponse ratingToRatingResponse(Rating rating){
        if(rating == null) return null;
        return new RatingResponse(rating.getRatingId(), rating.getPostTime(), rating.getRating(), rating.getComment());
    }
}

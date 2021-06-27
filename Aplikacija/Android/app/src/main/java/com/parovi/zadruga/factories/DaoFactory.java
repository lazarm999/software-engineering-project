package com.parovi.zadruga.factories;

import com.parovi.zadruga.App;
import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.AdTagDao;
import com.parovi.zadruga.daos.AppliedDao;
import com.parovi.zadruga.daos.ChatDao;
import com.parovi.zadruga.daos.CommentDao;
import com.parovi.zadruga.daos.MessageDao;
import com.parovi.zadruga.daos.NotificationDao;
import com.parovi.zadruga.daos.RatingDao;
import com.parovi.zadruga.daos.TagDao;
import com.parovi.zadruga.daos.TaggedDao;
import com.parovi.zadruga.daos.UserBadgeDao;
import com.parovi.zadruga.daos.UserChatDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.daos.lookupDaos.BadgeDao;
import com.parovi.zadruga.daos.lookupDaos.FacultyDao;
import com.parovi.zadruga.daos.lookupDaos.LocationDao;
import com.parovi.zadruga.daos.lookupDaos.UniversityDao;
import com.parovi.zadruga.databases.ZadrugaDatabase;

public class DaoFactory {
    private static ZadrugaDatabase localDb;
    private static AdDao adDao;
    private static UserDao userDao;
    private static RatingDao ratingDao;
    private static AppliedDao appliedDao;
    private static CommentDao commentDao;
    private static ChatDao chatDao;
    private static MessageDao messageDao;
    private static BadgeDao badgeDao;
    private static UserBadgeDao userBadgeDao;
    private static TagDao tagDao;
    private static AdTagDao adTagDao;
    private static LocationDao locationDao;
    private static FacultyDao facultyDao;
    private static UniversityDao universityDao;
    private static UserChatDao userChatDao;
    private static TaggedDao taggedDao;
    private static NotificationDao notificationDao;

    /*Retrofit notifications = new Retrofit.Builder()
            .baseUrl(Constants.FCM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    notificationApi = notifications.create(NotificationApi .class);*/

    private static ZadrugaDatabase getLocalDb(){
        if(localDb == null){
            localDb = ZadrugaDatabase.getInstance(App.getAppContext());
        }
        return localDb;
    }

    public static AdDao getAdDao(){
        if(adDao == null){
            adDao = getLocalDb().adDao();
        }
        return adDao;
    }

    public static UserDao getUserDao(){
        if(userDao == null){
            userDao = getLocalDb().userDao();
        }
        return userDao;
    }

    public static RatingDao getRatingDao(){
        if(ratingDao == null){
            ratingDao = getLocalDb().ratingDao();
        }
        return ratingDao;
    }

    public static AppliedDao getAppliedDao(){
        if(appliedDao == null){
            appliedDao = getLocalDb().appliedDao();
        }
        return appliedDao;
    }

    public static CommentDao getCommentDao(){
        if(commentDao == null){
            commentDao = getLocalDb().commentDao();
        }
        return commentDao;
    }

    public static ChatDao getChatDao(){
        if(chatDao == null){
            chatDao = getLocalDb().chatDao();
        }
        return chatDao;
    }

    public static MessageDao getMessageDao(){
        if(messageDao == null){
            messageDao = getLocalDb().messageDao();
        }
        return messageDao;
    }

    public static BadgeDao getBadgeDao(){
        if(badgeDao == null){
            badgeDao = getLocalDb().badgeDao();
        }
        return badgeDao;
    }

    public static UserBadgeDao getUserBadgeDao(){
        if(userBadgeDao == null){
            userBadgeDao = getLocalDb().userBadgeDao();
        }
        return userBadgeDao;
    }

    public static TagDao getTagDao(){
        if(tagDao == null){
            tagDao = getLocalDb().tagDao();
        }
        return tagDao;
    }

    public static AdTagDao getAdTagDao(){
        if(adTagDao == null){
            adTagDao = getLocalDb().adTagDao();
        }
        return adTagDao;
    }

    public static LocationDao getLocationDao(){
        if(locationDao == null){
            locationDao = getLocalDb().locationDao();
        }
        return locationDao;
    }

    public static FacultyDao getFacultyDao(){
        if(facultyDao == null){
            facultyDao = getLocalDb().facultyDao();
        }
        return facultyDao;
    }

    public static UniversityDao getUniversityDao() {
        if(universityDao == null){
            universityDao = getLocalDb().universityDao();
        }
        return universityDao;
    }

    public static UserChatDao getUserChatDao() {
        if(userChatDao == null){
            userChatDao = getLocalDb().userChatDao();
        }
        return userChatDao;
    }

    public static TaggedDao getTaggedDao() {
        if(taggedDao == null){
            taggedDao = getLocalDb().taggedDao();
        }
        return taggedDao;
    }

    public static NotificationDao getNotificationDao() {
        if(notificationDao == null){
            notificationDao = getLocalDb().notificationDao();
        }
        return notificationDao;
    }
}

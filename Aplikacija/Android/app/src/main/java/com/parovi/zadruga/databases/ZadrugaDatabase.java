package com.parovi.zadruga.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.AdTagDao;
import com.parovi.zadruga.daos.AppliedDao;
import com.parovi.zadruga.daos.NotificationDao;
import com.parovi.zadruga.daos.PreferredTagDao;
import com.parovi.zadruga.daos.ReportDao;
import com.parovi.zadruga.daos.TaggedDao;
import com.parovi.zadruga.daos.UserChatDao;
import com.parovi.zadruga.daos.lookupDaos.BadgeDao;
import com.parovi.zadruga.daos.ChatDao;
import com.parovi.zadruga.daos.CommentDao;
import com.parovi.zadruga.daos.lookupDaos.FacultyDao;
import com.parovi.zadruga.daos.lookupDaos.LocationDao;
import com.parovi.zadruga.daos.MessageDao;
import com.parovi.zadruga.daos.RatingDao;
import com.parovi.zadruga.daos.TagDao;
import com.parovi.zadruga.daos.lookupDaos.UniversityDao;
import com.parovi.zadruga.daos.UserBadgeDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.models.entityModels.PreferredTag;
import com.parovi.zadruga.models.converters.Converters;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Chat;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Message;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.Report;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.Tagged;
import com.parovi.zadruga.models.entityModels.University;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserBadge;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Applied;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserChat;

@Database(entities = {Ad.class, Location.class, User.class, UserBadge.class, Faculty.class, University.class, Notification.class,
        Tag.class, Badge.class, AdTag.class, Rating.class, Applied.class, Comment.class,
        Chat.class, Message.class, UserChat.class, Tagged.class, PreferredTag.class, Report.class},
        version = 68, exportSchema = false)
@TypeConverters({Converters.class})
abstract public class ZadrugaDatabase extends RoomDatabase {
    private static ZadrugaDatabase db;
    public abstract AdDao adDao();
    public abstract UserDao userDao();
    public abstract RatingDao ratingDao();
    public abstract AppliedDao appliedDao();
    public abstract CommentDao commentDao();
    public abstract ChatDao chatDao();
    public abstract MessageDao messageDao();
    public abstract TagDao tagDao();
    public abstract AdTagDao adTagDao();
    public abstract BadgeDao badgeDao();
    public abstract UserBadgeDao userBadgeDao();
    public abstract LocationDao locationDao();
    public abstract FacultyDao facultyDao();
    public abstract UserChatDao userChatDao();
    public abstract UniversityDao universityDao();
    public abstract TaggedDao taggedDao();
    public abstract NotificationDao notificationDao();
    public abstract PreferredTagDao preferredTagDao();
    public abstract ReportDao reportDao();

    public static synchronized ZadrugaDatabase getInstance(Context context){
        if(db == null){
            db = Room.databaseBuilder(context.getApplicationContext(),
                    ZadrugaDatabase.class, "ZadrugaDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}

package com.parovi.zadruga.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.daos.TmpDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.models.entityModels.Ad;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Notification;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.TmpPost;
import com.parovi.zadruga.models.entityModels.University;
import com.parovi.zadruga.models.entityModels.User;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.NotificationReceiver;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserBadge;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Applied;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Rating;
import com.parovi.zadruga.models.entityModels.manyToManyModels.Selected;

@Database(entities = {Ad.class, Location.class, User.class, UserBadge.class, Faculty.class, University.class, TmpPost.class, Notification.class,
        NotificationReceiver.class, Tag.class, Badge.class, AdTag.class, Rating.class, Applied.class, Selected.class},
        version = 19, exportSchema = false)
abstract public class ZadrugaDatabase extends RoomDatabase {
    private static ZadrugaDatabase db;
    public abstract AdDao adDao();
    public abstract LocationDao locationDao();
    public abstract TmpDao tmpDao();
    public abstract UserDao userDao();
    private static final boolean shouldInsert = false;

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

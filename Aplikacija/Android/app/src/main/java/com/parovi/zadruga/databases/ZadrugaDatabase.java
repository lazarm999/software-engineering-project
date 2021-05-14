package com.parovi.zadruga.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.daos.TmpDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.Faculty;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.Notification;
import com.parovi.zadruga.models.Tag;
import com.parovi.zadruga.models.TmpPost;
import com.parovi.zadruga.models.University;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.models.manyToManyModels.AdTag;
import com.parovi.zadruga.models.manyToManyModels.NotificationReceiver;
import com.parovi.zadruga.models.manyToManyModels.UserBadge;
import com.parovi.zadruga.models.manyToManyModels.Applied;
import com.parovi.zadruga.models.manyToManyModels.Rating;
import com.parovi.zadruga.models.manyToManyModels.Selected;

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

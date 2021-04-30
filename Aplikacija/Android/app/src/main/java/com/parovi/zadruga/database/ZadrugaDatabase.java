package com.parovi.zadruga.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.daos.TmpDao;
import com.parovi.zadruga.daos.UserDao;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Badge;
import com.parovi.zadruga.models.Employee;
import com.parovi.zadruga.models.Employer;
import com.parovi.zadruga.models.Faculty;
import com.parovi.zadruga.models.Location;
import com.parovi.zadruga.models.Tag;
import com.parovi.zadruga.models.University;
import com.parovi.zadruga.models.User;
import com.parovi.zadruga.models.crossRefModels.AdTagCrossRef;
import com.parovi.zadruga.models.crossRefModels.UserBadgeCrossRef;
import com.parovi.zadruga.models.manyToManyModels.Applied;
import com.parovi.zadruga.models.manyToManyModels.Rate;
import com.parovi.zadruga.models.manyToManyModels.Selected;

@Database(entities = {Ad.class, Location.class, User.class, UserBadgeCrossRef.class, Faculty.class, University.class,
                        Tag.class, Badge.class, AdTagCrossRef.class, Rate.class, Applied.class, Selected.class, Employee.class, Employer.class},
        version = 8)
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

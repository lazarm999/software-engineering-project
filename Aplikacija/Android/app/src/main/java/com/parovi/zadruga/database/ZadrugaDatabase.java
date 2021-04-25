package com.parovi.zadruga.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.parovi.zadruga.daos.AdDao;
import com.parovi.zadruga.daos.LocationDao;
import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Location;

@Database(entities = {Ad.class, Location.class}, version = 3)
abstract public class ZadrugaDatabase extends RoomDatabase {
    private static ZadrugaDatabase db;
    public abstract AdDao adDao();
    public abstract LocationDao locationDao();

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

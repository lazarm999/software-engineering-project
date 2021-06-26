package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Long insert(T obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract List<Long> insert(List<T> obj);

    @Update
    public abstract void update(T obj);

    @Update
    public abstract void update(List<T> objList);

    @Transaction
    public void insertOrUpdate(T obj) {
        Long res = insert(obj);
        if (res == -1L){
            update(obj);
        }
    }

    @Transaction
    public void insertOrUpdate(List<T> objList) {
        List<Long> insertResult = insert(objList);
        List<T> updateList = new ArrayList<T>();

        for (int i = 0; i < objList.size(); i++) {
            if (insertResult.get(i) == -1L) updateList.add(objList.get(i));
        }

        if (!updateList.isEmpty()) update(updateList);
    }
}

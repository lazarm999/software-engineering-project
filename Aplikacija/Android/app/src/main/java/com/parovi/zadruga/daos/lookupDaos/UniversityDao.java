package com.parovi.zadruga.daos.lookupDaos;

import androidx.room.Dao;

import com.parovi.zadruga.daos.BaseDao;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.University;

@Dao
public abstract class UniversityDao extends BaseDao<University> {
}

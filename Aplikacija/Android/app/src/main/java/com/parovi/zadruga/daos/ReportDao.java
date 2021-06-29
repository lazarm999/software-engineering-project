package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.parovi.zadruga.models.entityModels.Report;

@Dao
public abstract class ReportDao extends BaseDao<Report> {
    @Query("DELETE FROM Report WHERE Report.reportId = :reportId")
    public abstract Integer deleteReport(int reportId);
}

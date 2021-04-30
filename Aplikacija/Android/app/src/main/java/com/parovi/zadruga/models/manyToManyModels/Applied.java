package com.parovi.zadruga.models.manyToManyModels;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.Employee;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkUserId", "fkAdId"},
        foreignKeys = {@ForeignKey(entity = Employee.class,
                        parentColumns = "userId",
                        childColumns = "fkUserId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = Ad.class,
                        parentColumns = "adId",
                        childColumns = "fkAdId",
                        onDelete = CASCADE)})
public class Applied {
    private int fkUserId;
    private int fkAdId;

    public int getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(int fkUserId) {
        this.fkUserId = fkUserId;
    }

    public int getFkAdId() {
        return fkAdId;
    }

    public void setFkAdId(int fkAdId) {
        this.fkAdId = fkAdId;
    }
}


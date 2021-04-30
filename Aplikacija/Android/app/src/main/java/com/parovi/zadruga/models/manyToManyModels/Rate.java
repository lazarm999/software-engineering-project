package com.parovi.zadruga.models.manyToManyModels;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.Ad;
import com.parovi.zadruga.models.User;

import static androidx.room.ForeignKey.CASCADE;

//TODO: je l smes vise puta da nekog ocenis?
@Entity(primaryKeys = {"fkRaterId", "fkRatedId"},
        foreignKeys = {@ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkRaterId",
                        onDelete = CASCADE),
                        @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "fkRatedId",
                        onDelete = CASCADE)})
public class Rate {
    private int fkRaterId;
    private int fkRatedId;

    public int getFkRaterId() {
        return fkRaterId;
    }

    public void setFkRaterId(int fkRaterId) {
        this.fkRaterId = fkRaterId;
    }

    public int getFkRatedId() {
        return fkRatedId;
    }

    public void setFkRatedId(int fkRatedId) {
        this.fkRatedId = fkRatedId;
    }
}

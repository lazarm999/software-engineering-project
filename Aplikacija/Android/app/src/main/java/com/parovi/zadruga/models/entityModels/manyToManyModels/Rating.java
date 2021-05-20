package com.parovi.zadruga.models.entityModels.manyToManyModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.User;

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
public class Rating {
    @ColumnInfo(index = true)
    private int fkRaterId;
    @ColumnInfo(index = true)
    private int fkRatedId;
    private int rating;
    private String comment;
    private boolean isSynced;

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

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

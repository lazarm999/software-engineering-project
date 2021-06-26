package com.parovi.zadruga.models.entityModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.parovi.zadruga.models.entityModels.manyToManyModels.Comment;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"fkCommentId", "tagIndex"},
        foreignKeys = {@ForeignKey(entity = Comment.class,
                        parentColumns = "commentId",
                        childColumns = "fkCommentId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE)})
public class Tagged {
    @ColumnInfo(index = true)
    private int fkCommentId;
    private int tagIndex;

    public Tagged(int fkCommentId, int tagIndex) {
        this.fkCommentId = fkCommentId;
        this.tagIndex = tagIndex;
    }

    public int getFkCommentId() {
        return fkCommentId;
    }

    public void setFkCommentId(int fkCommentId) {
        this.fkCommentId = fkCommentId;
    }

    public int getTagIndex() {
        return tagIndex;
    }

    public void setTagIndex(int tagIndex) {
        this.tagIndex = tagIndex;
    }
}

package com.parovi.zadruga.models.entityModels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PreferredTag {
    @PrimaryKey
    private int tagId;

    public PreferredTag(int tagId) {
        this.tagId = tagId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public static List<Integer> ListDiff(List<Integer> listA, List<Integer> listB) {
        List<Integer> result = new ArrayList<>();
        for (Integer integer : listA) {
            if (!listB.contains(integer))
                result.add(integer);
        }
        return result;
    }
}

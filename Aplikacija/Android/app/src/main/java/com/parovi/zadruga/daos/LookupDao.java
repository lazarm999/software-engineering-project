package com.parovi.zadruga.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.parovi.zadruga.models.entityModels.Badge;
import com.parovi.zadruga.models.entityModels.Faculty;
import com.parovi.zadruga.models.entityModels.Location;
import com.parovi.zadruga.models.entityModels.Tag;
import com.parovi.zadruga.models.entityModels.University;
import com.parovi.zadruga.models.entityModels.manyToManyModels.AdTag;
import com.parovi.zadruga.models.entityModels.manyToManyModels.UserBadge;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface LookupDao {

    //Location
    @Insert
    ListenableFuture<Long> insertLocation(Location location);

    @Insert
    ListenableFuture<List<Long>> insertLocations(List<Location> locations);

    @Query("SELECT * FROM Location")
    ListenableFuture<List<Location>> getAllLocations();

    @Query("SELECT * FROM Location WHERE Location.locId = :locId")
    ListenableFuture<Location> getLocationById(int locId);

    //Tag
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertAdTag(AdTag adTag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertTag(Tag tag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertTags(List<Tag> tags);

    @Query("SELECT Tag.* FROM AdTag " +
            "INNER JOIN Tag ON AdTag.fkTagId = Tag.tagId " +
            "WHERE AdTag.fkAdId = :adId")
    ListenableFuture<List<Tag>> getTagsByAdId(int adId);

    @Query("DELETE FROM AdTag WHERE AdTag.fkTagId = :tagId AND AdTag.fkAdId = :adId")
    ListenableFuture<Integer> deleteAdTag(int tagId, int adId);

    //Badge
    @Query("SELECT Badge.* " +
            "FROM UserBadge " +
            "INNER JOIN Badge ON UserBadge.fkBadgeId = Badge.badgeId " +
            "WHERE UserBadge.fkUserId = :userId")
    ListenableFuture<List<Badge>> getUserBadges(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertBadge(Badge badge);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertBadges(List<Badge> badges);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertUserBadges(List<UserBadge> badges);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertFaculty(Faculty faculty);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertFaculties(List<Faculty> faculties);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insertUniversity(University university);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertUniversities(List<University> universities);
}

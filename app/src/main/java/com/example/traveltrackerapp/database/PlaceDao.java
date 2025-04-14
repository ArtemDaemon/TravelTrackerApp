package com.example.traveltrackerapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.traveltrackerapp.entities.Place;

import java.util.List;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM places")
    LiveData<List<Place>> getAllPlaces();

    @Insert
    void insert(Place place);

    @Delete
    void delete(Place place);

    @Query("SELECT * FROM places WHERE id = :placeId LIMIT 1")
    Place getPlaceById(int placeId);
}

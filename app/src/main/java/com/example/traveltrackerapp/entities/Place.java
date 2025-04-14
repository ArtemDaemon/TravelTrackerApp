package com.example.traveltrackerapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "places")
public class Place {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String address;

    @NonNull
    public String imageUri;

    public Place(@NonNull String name, @NonNull String address, @NonNull String imageUri) {
        this.name = name;
        this.address = address;
        this.imageUri = imageUri;
    }
}
